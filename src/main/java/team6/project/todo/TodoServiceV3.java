package team6.project.todo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team6.project.common.ResVo;
import team6.project.common.exception.*;
import team6.project.common.utils.CommonUtils;
import team6.project.todo.model.*;
import team6.project.todo.model.proc.CheckTodoAndRepeatDto;
import team6.project.todo.model.proc.InsertTodoDto;
import team6.project.todo.model.proc.TodoSelectTmpResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static team6.project.common.Const.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceV3 implements TodoServiceInter {

    private final TodoRepository repository;
    private final CommonUtils commonUtils;


    @Transactional
    public ResVo regTodo(TodoRegDto dto) {
        // 투두 는 10개까지만 저장.
        if (repository.getListCountById(dto.getIuser()) >= TODO_MAX_SIZE) {
            throw new TodoIsFullException(TODO_IS_FULL_EX_MESSAGE);
        }


        // startDate & endDate 그리고 startTime & endTime 오류 검증

        checkIsBefore(LocalDateTime.of(dto.getEndDate(), dto.getEndTime()),
                LocalDateTime.of(dto.getStartDate(), dto.getStartTime()));

        try {
            checkRepeatTypeAndRepeatNum(dto.getRepeatType(), dto.getRepeatNum());
            // 반복 있을때 로직
            log.debug("todo service in try");
            InsertTodoDto insertTodoDto = new InsertTodoDto(dto);
            if (repository.saveTodo(insertTodoDto) == 0) {
                throw new TodoSaveException(TODO_SAVE_FAIL_EX_MESSAGE);
            }

            RepeatInsertDto insRepeatInfoDto = new RepeatInsertDto(dto, insertTodoDto.getItodo(),
                    dto.getRepeatType().equalsIgnoreCase(WEEK) ?
                            // resolve week format from JS to JAVA
                            commonUtils.toJavaFrom(dto.getRepeatNum()) :
                            dto.getRepeatNum());
            if (repository.saveRepeat(insRepeatInfoDto) == 0) {
                throw new RepeatSaveException(REPEAT_SAVE_FAIL_EX_MESSAGE);
            }

            return new ResVo(insRepeatInfoDto.getItodo());

        } catch (NullPointerException e) {
            // 반복 없을때 로직
            log.debug("todo service in catch");
            checkRepeatNumInCatch(dto.getRepeatNum());
            InsertTodoDto insertTodoDto = new InsertTodoDto(dto);
            if (repository.saveTodo(insertTodoDto) == 0) {
                throw new TodoSaveException(TODO_SAVE_FAIL_EX_MESSAGE);
            }
            return new ResVo(insertTodoDto.getItodo());
        }
    }

    public List<TodoSelectVo> getTodo(TodoSelectDto dto) {

        // 정제 전
        List<TodoSelectTmpResult> allTodos = repository.findTodoAndRepeatBy(dto);

        // 정제
        List<TodoSelectVo> result = new ArrayList<>();
        allTodos.forEach(todo -> {
            try {
                // 주반복
                if (todo.getRepeatType().equalsIgnoreCase(WEEK)) {
                    // 1: 월 ~ 7: 일 ('JAVA format') 로 DB 에 저장되어 있는것 사용.
                    // 현재 update 는 front 로 요일정보를 넘기지 않기 때문에 WeekFormatResolver 사용 하지 않음.
                    LocalDate weekWalk = LocalDate.of(dto.getSelectedDate().getYear(), dto.getSelectedDate().getMonth(), FIRST_DAY);
                    while (weekWalk.getDayOfWeek().getValue() != todo.getRepeatNum()) {
                        // 첫번째 요일
                        weekWalk = weekWalk.plusDays(PLUS_ONE_MONTH_OR_WEEK_OR_DAY);
                    }
                    // 첫번째 요일 (자바기준 week) 획득 - weekWalk
                    while (true) {
                        // 1주씩 추가
                        weekWalk = weekWalk.plusWeeks(PLUS_ONE_MONTH_OR_WEEK_OR_DAY);
                        // 요일 체크 날짜가 해당 월의 마지막날과 같거나, 마지막날 보다 크면 break;
                        if (weekWalk.isEqual(dto.getSelectedDate().withDayOfMonth(dto.getSelectedDate().lengthOfMonth()))
                                || weekWalk.isAfter(dto.getSelectedDate())) {
                            return;
                        }
                        // 요일 체크 날짜가 요청 온 날짜와 같다면 result 에 추가, break;
                        if (weekWalk.isEqual(dto.getSelectedDate())) {
                            result.add(new TodoSelectVo(todo.getItodo(), todo.getTodoContent(),
                                    todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime()));
                            return;
                        }
                    }
                }
                if (todo.getRepeatType().equalsIgnoreCase(MONTH)) {
                    if (todo.getRepeatNum() == dto.getSelectedDate().getDayOfMonth()) {
                        result.add(new TodoSelectVo(todo.getItodo(), todo.getTodoContent(),
                                todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime()));
                    }
                }
            } catch (NullPointerException e) {
                /*
                    반복이 없는경우 로직
                    (endDate 는 무조건 해당 날짜보다 같거나 이후이고, startDate 는 무조건 해당날짜와 같거나 이전임이 보장된 상황.)
                 */

                result.add(new TodoSelectVo(todo.getItodo(), todo.getTodoContent(),
                        todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime()));
            }
        });

        // 10개로 줄이기
        return result.subList(TODO_SELECT_FROM_NUM, result.size() > TODO_MAX_SIZE ? TODO_SELECT_TO_NUM : result.size());
    }

    @Transactional
    public ResVo patchTodo(PatchTodoDto dto) {

        /*
        로직 -> start_date, end_date, start_time, end_time, repeat_end_date 중 하나라도 null 이 아니면,
        select 문을 통해 해당 itodo 로 부터 start_date, end_date, start_time, end_time, repeat_end_date 를 가져와서,
        LocalDate startDate;
        LocalDate endDate;
        LocalTime startTime;
        LocalTime endTime;
        LocalDate repeatEndDate;
        의 5가지 객체를 일단 각각 만들어 둔다.
        (repeatEndDate 가 db에서도 null 이고, 프론트에서 넘어온 값도 null 이면 repeatEndDate 는 신경쓰지 않아도 된다.)
        그후,
        프론트로부터 제공받은 start_date + start_time || end_date + end_time 으로 날짜를 만드는데,
        만약 그들중 null 인 부분이 있다면 db에서 가져온 값을 대입한다.
        그리고 그 두 날짜를 비교한다.
        만약 start_date + start_time 이 end_date + end_time 보다 1분이라도 이후라면 (isAfter) 정상로직을 수행 한다.
        아니라면 라면 예외처리한다. (제공된 날짜 정보가 잘못되었다)
        +
        repeat_end_date 도 검증해야한다.
        만약 프론트에서 넘어온 값이 있다면 end_date + end_time 과 비교, repeat_end_date + 23:59:59 가 더 이후인지 체크,
        만약 없다면 db에 저장된 repeat_end_date + 23:59:59 가 end_date + end_time 보다 이후인지 체크해야 한다.
        만약, 프론트에서도 repeat_end_date 가 넘어오지 않았고(null 이고), db에서 가져온 repeat_end_date 도 null 이라면
        (병합본이 null 이라면) t_todo 만 update 하면 된다. - 데이터 검증은 필요.
         */


        /*
        일단 다 가져와서 검증하는 모델 생성 (병합)
        검증
        문제없으면 PatchTodoDto 를 <if> 이용하여 update
         */

        TodoSelectTmpResult selectResult = repository.findTodoAndRepeatBy(
                new TodoSelectDtoForUpdate(dto.getIuser(), dto.getItodo())).get(0);
        // DB에 해당 _TODO 가 있는지 여부 체크 (수정이므로 있음이 보장되어야 함)
        if (selectResult == null) {
            throw new NoSuchDataException(NO_SUCH_DATA_EX_MESSAGE);
        }

        // 두 데이터 병합 (넘어온 수정 데이터에서 null 인 부분은 DB에서 가져온 데이터로 채움)
        CheckTodoAndRepeatDto checkResultData = checkTodoData(dto, selectResult);


        // startDate & endDate 오류 검증
        // startTime & endTime 오류 검증
        checkIsBefore(LocalDateTime.of(checkResultData.getEndDate(), checkResultData.getEndTime()),
                LocalDateTime.of(checkResultData.getStartDate(), checkResultData.getStartTime()));


        // 데이터에는 오류가 없음이 보장됨.

        /*
        수정에서는 진짜 수정만 하자.
        repeat 데이터의 삭제는 delete 로 따로 만들자.
        수정페이지에서 삭제버튼을 누르면 해당 repeat 의 delete 로 요청하면 되는 부분이다.
        이유 : 수정 데이터에서 repeat 정보가 null 이 들어올 경우,
        이게 삭제를 위한 null 인지, 아무수정도 하지 않아서 null 인지 알 수가 없다.
        다만, db에 반복 데이터가 없는데 수정 데이터에 반복 데이터가 있다면 insert 할 수는 있다.
         */

        // DB 에 repeat 정보가 저장되어 있는지 여부 체크.
        boolean hasRepeatInfoInDB = selectResult.getRepeatEndDate() != null;
        try {

            // db에 있든, dto 에 있든 repeatEndDate 가 있다면 검증.

            final LocalDate repeatEndDate = checkResultData.getRepeatEndDate();
            LocalDate startDateWalker = LocalDate.of(
                    checkResultData.getStartDate().getYear(),
                    checkResultData.getStartDate().getMonth(),
                    checkResultData.getStartDate().getDayOfMonth());
            // db, dto 모두 repeat 정보가 없는 경우 NPE
            // 병합된 데이터 기준, 따라서 repeat 정보가 db 또는 dto 중 한곳이라도 존재 한다면,
            // 무조건 repeatEndDate, repeatType, repeatNum 모두 null 이 아님.
            if (checkResultData.getRepeatType().equalsIgnoreCase(WEEK)) {
                startDateWalker = startDateWalker.plusWeeks(1);
            } else {
                startDateWalker = startDateWalker.plusMonths(1);
            }

            // 검증
            checkIsBefore(
                    LocalDateTime.of(repeatEndDate, LocalTime.of(23, 59, 59)),
                    LocalDateTime.of(startDateWalker, checkResultData.getStartTime()));

            if (!hasRepeatInfoInDB) {
                // dto에는 repeat 정보가 있고, db에는 없는 경우 (todo_repeat insert)
                repository.saveRepeat(new RepeatInsertDto(dto.getItodo(),
                        checkResultData.getRepeatEndDate(),
                        checkResultData.getRepeatType(),
                        checkResultData.getRepeatNum()));
                // 새로 insert 했으니까 update 될 필요 없기 때문에 null 로 세팅.
                checkResultData.setRepeatEndDate(null);
                checkResultData.setRepeatType(null);
                checkResultData.setRepeatNum(null);
            }


        } catch (NullPointerException e) {
            // dto 와 db 모두 repeat 정보가 없는 경우.
            // NPE 가 터져서 catch 로 온 상황에 repeatType 과 repeatNum 둘다 null 이 아니라면 충분한 정보가 제공되지 않은것.
            commonUtils.checkObjectIsNotNullThrow(BadInformationException.class, NOT_ENOUGH_INFO_EX_MESSAGE,
                    checkResultData.getRepeatEndDate(),
                    checkResultData.getRepeatType(),
                    checkResultData.getRepeatNum());

        }

        // t_todo update !  (공통 사항) - repeat 도 <if> 로 다 작성.
        return new ResVo(repository.updateTodoAndRepeatIfExists(dto));
    }

    @Transactional
    public ResVo deleteTodo(TodoDeleteDto dto, Integer delOnlyRepeat) {
        // repeat 유무 관계 없이 delete query 실행.
        Integer delRepeatResult = repository.deleteRepeat(dto.getIuser(), dto.getItodo());
        if (delOnlyRepeat != null && delOnlyRepeat == 1) {
            // 반복정보만 지울경우
            return new ResVo(delRepeatResult);
        }
        int result = repository.deleteTodo(dto);
        if (result == 0) {
            // 요청받은 iuser, itodo 로 삭제되는 일정이 없을경우 NoSuchDataException 발생.
            throw new NoSuchDataException(NO_SUCH_DATA_EX_MESSAGE);
        }
        return new ResVo(result);
    }

    /*
     * ------- Extracted Methods -------
     */

    private void checkRepeatTypeAndRepeatNum(String repeatType, Integer repeatNum) {
        if (!repeatType.equalsIgnoreCase(WEEK) &&
                !repeatType.equalsIgnoreCase(MONTH)) {
            throw new BadDateInformationException(BAD_DATE_INFO_EX_MESSAGE);
        }
        if (repeatNum == null) {
            throw new BadInformationException(BAD_REQUEST_EX_MESSAGE);
        }
    }

    private void checkRepeatNumInCatch(Integer repeatNum) {
        if (repeatNum != null) {
            throw new BadInformationException(BAD_REQUEST_EX_MESSAGE);
        }
    }

    private void checkIsBefore(LocalDateTime endDateTime, LocalDateTime startDateTime) {
        if (endDateTime.isBefore(startDateTime)) {
            throw new BadDateInformationException(BAD_TIME_INFO_EX_MESSAGE);
        }
    }


    private CheckTodoAndRepeatDto checkTodoData(PatchTodoDto dto, TodoSelectTmpResult selectResult) {
        return CheckTodoAndRepeatDto.builder()
                .todoContent(dto.getTodoContent() == null ? selectResult.getTodoContent() : dto.getTodoContent())
                .startDate(dto.getStartDate() == null ? selectResult.getStartDate() : dto.getStartDate())
                .endDate(dto.getEndDate() == null ? selectResult.getEndDate() : dto.getEndDate())
                .startTime(dto.getStartTime() == null ? selectResult.getStartTime() : dto.getStartTime())
                .endTime(dto.getEndTime() == null ? selectResult.getEndTime() : dto.getEndTime())
                .repeatEndDate(dto.getRepeatEndDate() == null ? selectResult.getRepeatEndDate() : dto.getRepeatEndDate())
                .repeatType(dto.getRepeatType() == null ? selectResult.getRepeatType() : dto.getRepeatType())
                .repeatNum(dto.getRepeatNum() == null ? selectResult.getRepeatNum() : dto.getRepeatNum())
                .build();
    }
}