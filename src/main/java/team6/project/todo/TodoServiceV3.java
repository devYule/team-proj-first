package team6.project.todo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team6.project.common.ResVo;
import team6.project.common.exception.BadInformationException;
import team6.project.common.exception.NoSuchDataException;
import team6.project.common.exception.NotEnoughInformationException;
import team6.project.common.exception.TodoIsFullException;
import team6.project.common.utils.CommonUtils;
import team6.project.todo.model.*;
import team6.project.todo.model.proc.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static team6.project.common.Const.*;

@Slf4j
//@Service
@RequiredArgsConstructor
public class TodoServiceV3 implements TodoServiceRef {

    private final TodoRepositoryRef repository;

    public ResVo regTodo(TodoRegDto dto) {
        // 투두 는 10개까지만 저장.


        if (repository.getListCountById(dto.getIuser(), dto.getStartDate()) >= TODO_MAX_SIZE) {
            throw new TodoIsFullException(TODO_IS_FULL_EX_MESSAGE);
        }

        try {
            // repeatType, repeatNum 둘중 하나만 값 있는 경우 체크
            // repeatEndDate != null 인데, repeatType 이나 repeatNum 이 null 인 경우 체크
            // ㄴ> 그냥 repeat 정보를 저장하지 않아도 되지만, 오류 메시지 출력을 위해 따로 검증
            // repeatType == week && repeatNum >= 1 && repeatNum <= 7 과,
            // repeatType == month && repeatNum >= 1 && repeatNum <= 31 여부 체크
            // NPE 유발
            CommonUtils.checkRepeatNumWithRepeatType(dto.getRepeatType(), CommonUtils.toJavaFrom(dto.getRepeatNum()));
            // 반복 있을때 로직
            log.debug("todo service in try");
            CommonUtils.checkRepeatInfo(dto.getRepeatEndDate(), dto.getRepeatType(), dto.getRepeatNum());

            // 검증 (주반복일때는 주정보일치, 월반복일때는 일정보 일치 여부 , endDate + endTime 이 startDate + startTime 보다 같거나 이후인지 여부 체크)
            // 주 반복일경우 startDateTime 이 주 반복 숫자와 동일한 숫자인지 체크,
            // 월 반복일경우 startDateTime 이 월 반복 숫자(일) 와 동일한 숫자인지 체크.
            CommonUtils.checkIsBefore(
                    LocalDateTime.of(dto.getEndDate(),
                            dto.getEndTime() == null ? LocalTime.of(23, 59, 59) : dto.getEndTime()),
                    LocalDateTime.of(dto.getStartDate(),
                            dto.getStartTime() == null ? LocalTime.of(0, 0, 0) : dto.getStartTime()),
                    dto.getRepeatType(),
                    CommonUtils.toJavaFrom(dto.getRepeatNum())
            );

            InsertTodoDto insertTodoDto = new InsertTodoDto(dto);
            if (repository.saveTodo(insertTodoDto) == 0) {
                throw new RuntimeException(RUNTIME_EX_MESSAGE);
            }

            RepeatInsertDto insRepeatInfoDto = new RepeatInsertDto(
                    dto, insertTodoDto.getItodo(),
                    dto.getRepeatType().equalsIgnoreCase(WEEK) ?
                            // resolve week format from JS to JAVA
                            CommonUtils.toJavaFrom(dto.getRepeatNum()) :
                            dto.getRepeatNum()
            );
            if (repository.saveRepeat(insRepeatInfoDto) == 0) {
                throw new RuntimeException(RUNTIME_EX_MESSAGE);
            }

            return new ResVo(insRepeatInfoDto.getItodo());

        } catch (NullPointerException e) {
            // 반복 없을때 로직
            log.debug("todo service in catch");
            // startDate & endDate 그리고 startTime & endTime 오류 검증 -> startDate & endDate 는 무조건 null 이 아님이 Controller 에서 검증됨.
            // (@Validated)
            CommonUtils.checkIsBefore(
                    LocalDateTime.of(dto.getEndDate(),
                            dto.getEndTime() == null ? LocalTime.of(23, 59, 59) : dto.getEndTime()),
                    LocalDateTime.of(dto.getStartDate(),
                            dto.getStartTime() == null ? LocalTime.of(0, 0, 0) : dto.getStartTime())
            );

            InsertTodoDto insertTodoDto = new InsertTodoDto(dto);

            if (repository.saveTodo(insertTodoDto) == 0) {
                throw new RuntimeException(RUNTIME_EX_MESSAGE);
            }
            return new ResVo(insertTodoDto.getItodo());
        }
    }

    public TodoSelectVo getTodo(TodoSelectTransVo dto) {

        // 정제 전
        // 10개만 가져옴 (LIMIT 0, 10 in QUERY)
        List<TodoSelectTmpResult> allTodos = repository.findTodoAndRepeatBy(dto);
        // 해당 날의 감정, 감정태그 가져오기
        EmotionSelectTmpResult emotionSelectTmpResult = repository.findEmotionAndEmotionTagBy(dto);

//        // 조회된 결과가 하나도 없을경우 throw - (?)
//        if (allTodos.isEmpty() && emotionSelectTmpResult == null) {
//            throw new NoSuchDataException(NO_SUCH_DATA_EX_MESSAGE);
//        }

        TodoSelectVo todoSelectVo = new TodoSelectVo();
        if (emotionSelectTmpResult != null) {
            todoSelectVo.setEmotionGrade(emotionSelectTmpResult.getEmotion());
            todoSelectVo.setEmotionTag(emotionSelectTmpResult.getEmotionTag());
        }
        log.debug("allTodos size = {}", allTodos.size());
        log.debug("allTodos = {}", allTodos);
        // 정제
        List<TodoInfo> result = new ArrayList<>();

        allTodos.forEach(todo -> {
            try {
                // 주반복
                // repeat 정보가 없는경우 NPE 유발.
                if (todo.getRepeatType().equalsIgnoreCase(WEEK)) {
                    // 1: 월 ~ 7: 일 ('JAVA format') 로 DB 에 저장되어 있는것 사용.
                    // 현재 update 는 front 로 요일정보를 넘기지 않기 때문에 WeekFormatResolver 사용 하지 않음.
                    LocalDate weekWalker = LocalDate.of(dto.getSelectedDate().getYear(), dto.getSelectedDate().getMonth(), FIRST_DAY);
                    while (weekWalker.getDayOfWeek().getValue() != todo.getRepeatNum()) {
                        // 해당 달의 첫번째 요일 (repeatNum 으로 db에 저장된것 기준)
                        weekWalker = weekWalker.plusDays(PLUS_ONE_MONTH_OR_WEEK_OR_DAY);
                    }
                    // 첫번째 요일 (자바기준 week) 획득 - weekWalker

                    while (true) {
                        // 요일 체크 날짜가 해당 월의 마지막날과 같거나, 마지막날 보다 크면 다음 객체(forEach)로 넘어감.
//                        if (weekWalker.isEqual(dto.getSelectedDate().withDayOfMonth(dto.getSelectedDate().lengthOfMonth()))
//                                || weekWalker.isAfter(dto.getSelectedDate())) {
//                            return;
//                        }
                        // 요일 체크 날짜가 요청 온 날짜와 같다면 result 에 추가, break;
                        if (weekWalker.isEqual(dto.getSelectedDate())) {
                            result.add(new TodoInfo(todo.getItodo(), todo.getTodoContent(),
                                    todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime(),
                                    todo.getRepeatEndDate(), todo.getRepeatType(), todo.getRepeatNum()));
                            return;
                        }
                        // 1주씩 추가
                        weekWalker = weekWalker.plusWeeks(PLUS_ONE_MONTH_OR_WEEK_OR_DAY);

                        // 요일 체크 날짜가 선택된 날짜 보다 크면 다음 객체(forEach)로 넘어감.
                        if (weekWalker.isAfter(dto.getSelectedDate())) {
                            return;
                        }
                    }
                }
                if (todo.getRepeatType().equalsIgnoreCase(MONTH)) {
                    if (todo.getRepeatNum() == dto.getSelectedDate().getDayOfMonth() &&
                    todo.getRepeatNum() <= dto.getSelectedDate().lengthOfMonth()) {
                        result.add(new TodoInfo(todo.getItodo(), todo.getTodoContent(),
                                todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime()
                        , todo.getRepeatEndDate(), todo.getRepeatType(), todo.getRepeatNum() //반복시 주석 제거
                        ));
                    }
                }
            } catch (NullPointerException e) {
                /*
                    반복이 없는경우 로직
                    (endDate 는 무조건 해당 날짜보다 같거나 이후이고, startDate 는 무조건 해당날짜와 같거나 이전임이 보장된 상황.)
                 */

                result.add(new TodoInfo(todo.getItodo(), todo.getTodoContent(),
                        todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime()));
            }
        });
        log.debug("result size = {}", result.size());
        todoSelectVo.setTodos(result);
        return todoSelectVo;
    }

    public ResVo patchTodo(PatchTodoDto dto) {

        // 모든 일정관련 데이터가 null 이면 아래 작업들을 수행할 필요가 없음.
        CommonUtils.checkObjectAllNullThrow(NotEnoughInformationException.class, NOT_ENOUGH_INFO_EX_MESSAGE,
                dto.getTodoContent(), dto.getStartDate(), dto.getEndDate(), dto.getStartTime(),
                dto.getEndTime(), dto.getRepeatEndDate(), dto.getRepeatType(), dto.getRepeatNum());

        /*
        일단 다 가져와서 검증하는 모델 생성 (병합)
        검증
        문제없으면 PatchTodoDto 를 <if> 이용하여 update
         */
        TodoSelectTmpResult selectResult;
        try {
            selectResult = repository.findTodoAndRepeatBy(new TodoSelectVoForUpdate(dto.getItodo(), dto.getIuser())).get(0);
        } catch (IndexOutOfBoundsException e) {
            // DB에 해당 _TODO 가 있는지 여부 체크 (수정이므로 있음이 보장되어야 함)
            throw new NoSuchDataException(NO_SUCH_DATA_EX_MESSAGE);
        }

        // 두 데이터 병합 (넘어온 수정 데이터에서 null 인 부분은 DB에서 가져온 데이터로 채움)
        MergedTodoAndRepeatDto mergedTodoAndRepeat = mkTodoObject(dto, selectResult);

        /*
        수정에서는 진짜 수정만 한다.
        repeat 데이터의 삭제는 delete 에 따로 존재한다.
        만약 repeat 데이터의 삭제를 수정에서 함께 한다면 다음과 같은 문제가 생긴다.
        수정 데이터에서 repeat 정보가 null 로 넘어올 경우 이게 삭제를 위한 null 인지, 아무런 수정을 하지 않아서 null 인지 알 수가 없다.
        다만, db에 반복데이터가 없는 상태에서 dto에 repeat 데이터가 넘어온다면 insert 해야 한다.
         */

        // DB 에 repeat 정보가 저장되어 있는지 여부 체크.
        boolean hasRepeatInfoInDB = selectResult.getRepeatEndDate() != null;

        // 따라서 병합된 데이터 기준, repeat 정보가 존재 한다면,
        // 무조건 repeatEndDate, repeatType, repeatNum 모두 null 이 아님. (db에 저장할때 전부 값이 세팅된 상태)
        // 다만 db에는 없고, dto 에서는 repeatType 혹은 repeatNum 둘중 하나만 들어올 경우에 대한 예외 처리는 아래 검증부분에서 수행.
        LocalDate startDateWalker = LocalDate.of(
                mergedTodoAndRepeat.getStartDate().getYear(),
                mergedTodoAndRepeat.getStartDate().getMonth(),
                mergedTodoAndRepeat.getStartDate().getDayOfMonth()
        );
        try {
            // db, dto 모두 repeat 정보가 없는 경우 NPE
            if (mergedTodoAndRepeat.getRepeatType().equalsIgnoreCase(WEEK)) {
                startDateWalker = startDateWalker.plusWeeks(1);
            } else {
                startDateWalker = startDateWalker.plusMonths(1);
            }

            // 검증
            // db에 있든, dto 에 있든 repeatEndDate 가 있다면 검증용 LocalDate 생성.
            final LocalDate repeatEndDate = mergedTodoAndRepeat.getRepeatEndDate();

            // repeatType, repeatNum 둘중 하나만 값 있는 경우 체크
            // repeatEndDate != null 인데, repeatType 이나 repeatNum 이 null 인 경우 체크
            // repeatType == week && repeatNum >= 1 && repeatNum <= 7 과,
            // repeatType == month && repeatNum >= 1 && repeatNum <= 31 여부 체크
            // 3개 중 1개라도 null 일경우 예외 유발.
            checkRepeatInfo(
                    mergedTodoAndRepeat.getRepeatEndDate(),
                    mergedTodoAndRepeat.getRepeatType(),
                    mergedTodoAndRepeat.getRepeatNum()
            );

            // repeatEndDate 가 startDate 의 1달 혹은 1주 후 보다 before 이면 예외 유발.
            CommonUtils.checkIsBefore(
                    LocalDateTime.of(repeatEndDate, LocalTime.of(23, 59, 59)),
                    LocalDateTime.of(startDateWalker, mergedTodoAndRepeat.getStartTime())
            );
            // 검증 (주반복일때는 주정보일치, 월반복일때는 일정보 일치 여부 , endDate + endTime 이 startDate + startTime 보다 같거나 이후인지 여부 체크)
            // 주 반복일경우 startDateTime 이 주 반복 숫자와 동일한 숫자인지 체크,
            // 월 반복일경우 startDateTime 이 월 반복 숫자(일) 와 동일한 숫자인지 체크.
            CommonUtils.checkIsBefore(
                    LocalDateTime.of(mergedTodoAndRepeat.getEndDate(),
                            mergedTodoAndRepeat.getEndTime() == null ? LocalTime.of(23, 59, 59) : mergedTodoAndRepeat.getEndTime()),
                    LocalDateTime.of(mergedTodoAndRepeat.getStartDate(),
                            mergedTodoAndRepeat.getStartTime() == null ? LocalTime.of(0, 0, 0) : mergedTodoAndRepeat.getStartTime()),
                    mergedTodoAndRepeat.getRepeatType(),
                    mergedTodoAndRepeat.getRepeatNum()
            );

            // 로직 - repeat 데이터가 dto 에만 있는 경우 - repeat 데이터를 insert
            if (!hasRepeatInfoInDB) {
                repository.saveRepeat(new RepeatInsertDto(dto, CommonUtils.toJavaFrom(mergedTodoAndRepeat.getRepeatNum())));
                // 새로 insert 했으니까 update 될 필요 없기 때문에 null 로 세팅.
                mergedTodoAndRepeat.setRepeatEndDate(null);
                mergedTodoAndRepeat.setRepeatType(null);
                mergedTodoAndRepeat.setRepeatNum(null);
            }

            // repeat 데이터가 db에 이미 있고, dto 에도 들어온 경우는 catch 아래에서 _todo update 와 함께 한번에 처리
        } catch (NullPointerException e) {
            // startDate & endDate 오류 검증
            // startTime & endTime 오류 검증
            CommonUtils.checkIsBefore(
                    LocalDateTime.of(mergedTodoAndRepeat.getEndDate(), mergedTodoAndRepeat.getEndTime()),
                    LocalDateTime.of(mergedTodoAndRepeat.getStartDate(), mergedTodoAndRepeat.getStartTime())
            );

            // 로직 - repeat 데이터가 없는 경우

            // dto 와 db 모두 repeat 정보가 없는 경우.
            // NPE 가 터져서 catch 로 온 상황에 repeat 정보중 하나라도 null 이 아니라면 충분한 정보가 제공되지 않은것. (dto 에서)
            CommonUtils.checkObjectAnyNotNullThrow(
                    BadInformationException.class,
                    NOT_ENOUGH_INFO_EX_MESSAGE,
                    mergedTodoAndRepeat.getRepeatEndDate(),
                    mergedTodoAndRepeat.getRepeatType(),
                    mergedTodoAndRepeat.getRepeatNum()
            );
        }

        // t_todo update !  (공통 사항) - repeat 도 <if> 로 다 작성.
        return new ResVo(repository.updateTodoAndRepeatIfExists(dto));
    }

    public ResVo deleteTodo(TodoDeleteDto dto, Integer delOnlyRepeat) {
        // 예외를 위한 백업
        RepeatInsertDto backUpRepeat = repository.findRepeatBy(dto.getItodo());
        // repeat 유무 관계 없이 repeat 정보를 지우는 delete query 실행.
        Integer delRepeatResult = repository.deleteRepeat(dto);
        if (delOnlyRepeat != null && delOnlyRepeat == 1) {
            // 요청받은 iuser, itodo 로 삭제되는 repeat 이 없을경우 NoSuchDataException 발생.
            // ㄴ> 반복정보가 없는 일정일수도 있고, iuser, itodo 로 조회되는 데이터가 없을수도 있음.
            checkResultIfNullOrZeroThrow(delRepeatResult);
            // 반복정보만 지울경우 바로 리턴.
            return new ResVo(delRepeatResult);
        }

        // 백업한 데이터 활용할 수 있는 예외발생
//        dto.setIuser(1000);

        // 투두까지 전부 지우는 경우 추가 로직
        Integer result = repository.deleteTodo(dto);
        // 요청받은 iuser, itodo 로 삭제되는 일정이 없을경우 NoSuchDataException 발생.
        checkResultIfNullOrZeroThrow(result, backUpRepeat);

        return new ResVo(result);
    }

    /*
     * ------- Extracted Methods -------
     */

    private void checkResultIfNullOrZeroThrow(Integer result) {
        if (result == null || result == 0) {
            throw new NoSuchDataException(NO_SUCH_DATA_EX_MESSAGE);
        }
    }

    private void checkResultIfNullOrZeroThrow(Integer result, RepeatInsertDto backUpRepeat) {

        if (result == null || result == 0) {
            if (backUpRepeat != null) {
                repository.saveRepeat(backUpRepeat);
            }
            throw new NoSuchDataException(NO_SUCH_DATA_EX_MESSAGE);
        }
    }

    private void checkRepeatInfo(LocalDate repeatEndDate, String repeatType, Integer repeatNum) {
        CommonUtils.checkObjectAnyNullThrow(NotEnoughInformationException.class,
                NOT_ENOUGH_INFO_EX_MESSAGE,
                repeatEndDate, repeatType, repeatNum);

        // checkObjectAnyNullThrow 로 인해 3가지 객체가 모두 null 이 아님이 검증됨.
        CommonUtils.checkRepeatNumWithRepeatType(repeatType, repeatNum);
    }


    private MergedTodoAndRepeatDto mkTodoObject(PatchTodoDto dto, TodoSelectTmpResult selectResult) {
        MergedTodoAndRepeatDto mergedTodoAndRepeatDto = new MergedTodoAndRepeatDto(
                dto.getTodoContent() == null ? selectResult.getTodoContent() : dto.getTodoContent(),
                dto.getStartDate() == null ? selectResult.getStartDate() : dto.getStartDate(),
                dto.getEndDate() == null ? selectResult.getEndDate() : dto.getEndDate(),
                dto.getStartTime() == null ? selectResult.getStartTime() : dto.getStartTime(),
                dto.getEndTime() == null ? selectResult.getEndTime() : dto.getEndTime(),
                dto.getRepeatEndDate() == null ? selectResult.getRepeatEndDate() : dto.getRepeatEndDate(),
                dto.getRepeatType() == null ? selectResult.getRepeatType() : dto.getRepeatType(),
                dto.getRepeatNum() == null ? selectResult.getRepeatNum() : dto.getRepeatNum()

        );
        if (mergedTodoAndRepeatDto.getRepeatType() != null && mergedTodoAndRepeatDto.getRepeatType().equalsIgnoreCase(WEEK) && dto.getRepeatNum() != null) {
            mergedTodoAndRepeatDto.setRepeatNum(CommonUtils.toJavaFrom(mergedTodoAndRepeatDto.getRepeatNum()));
        }
        return mergedTodoAndRepeatDto;

    }
}
