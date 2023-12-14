//package team6.project.todo;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.transaction.annotation.Transactional;
//import team6.project.common.ResVo;
//import team6.project.common.exception.*;
//import team6.project.common.utils.CommonUtils;
//import team6.project.todo.model.*;
//import team6.project.todo.model.proc.*;
//import team6.project.todo.model.RepeatInsertDto;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static team6.project.common.Const.*;
//
//@Slf4j
////@Service
//@RequiredArgsConstructor
//public class TodoServiceV1 implements TodoServiceInter {
//
//    private final TodoRepository repository;
//    private final CommonUtils commonUtils;
//
//
//    @Transactional
//    public ResVo regTodo(TodoRegDto dto) {
//        // 투두 는 10개까지만 저장.
//        if (repository.getListCountById(dto.getIuser()) >= TODO_MAX_SIZE) {
//            throw new TodoIsFullException(TODO_IS_FULL_EX_MESSAGE);
//        }
//
//
//        // startDate & endDate 그리고 startTime & endTime 오류 검증
//        checkIsBefore(dto.getEndDate(), dto.getStartDate(), dto.getEndTime(), dto.getStartTime());
//
//        try {
//            checkRepeatTypeAndRepeatNum(dto.getRepeatType(), dto.getRepeatNum());
//            // 반복 있을때 로직
//            log.debug("todo service in try");
//            InsertTodoDto insertTodoDto = new InsertTodoDto(dto);
//            if (repository.saveTodo(insertTodoDto) == 0) {
//                throw new TodoSaveException(TODO_SAVE_FAIL_EX_MESSAGE);
//            }
//
//            RepeatInsertDto insRepeatInfoDto = new RepeatInsertDto(dto, insertTodoDto.getItodo(),
//                    dto.getRepeatType().equalsIgnoreCase(WEEK) ?
//                            // resolve week format from JS to JAVA
//                            commonUtils.toJavaFrom(dto.getRepeatNum()) :
//                            dto.getRepeatNum());
//            if (repository.saveRepeat(insRepeatInfoDto) == 0) {
//                throw new RepeatSaveException(REPEAT_SAVE_FAIL_EX_MESSAGE);
//            }
//
//            return new ResVo(insRepeatInfoDto.getItodo());
//
//        } catch (NullPointerException e) {
//            // 반복 없을때 로직
//            log.debug("todo service in catch");
//            checkRepeatNumInCatch(dto.getRepeatNum());
//            InsertTodoDto insertTodoDto = new InsertTodoDto(dto);
//            if (repository.saveTodo(insertTodoDto) == 0) {
//                throw new TodoSaveException(TODO_SAVE_FAIL_EX_MESSAGE);
//            }
//            return new ResVo(insertTodoDto.getItodo());
//        }
//    }
//
//    public List<TodoSelectVo> getTodo(TodoSelectDto dto) {
//
//        // 정제 전
//        List<TodoSelectTmpResult> allTodos = repository.findTodoAndRepeatBy(dto);
//
//        // 정제
//        List<TodoSelectVo> result = new ArrayList<>();
//        allTodos.forEach(todo -> {
//            try {
//                // 주반복
//                if (todo.getRepeatType().equalsIgnoreCase(WEEK)) {
//                    // 1: 월 ~ 7: 일 ('JAVA format') 로 DB 에 저장되어 있는것 사용.
//                    // 현재 update 는 front 로 요일정보를 넘기지 않기 때문에 WeekFormatResolver 사용 하지 않음.
//                    LocalDate weekWalk = LocalDate.of(dto.getSelectedDate().getYear(), dto.getSelectedDate().getMonth(), FIRST_DAY);
//                    while (weekWalk.getDayOfWeek().getValue() != todo.getRepeatNum()) {
//                        // 첫번째 요일
//                        weekWalk = weekWalk.plusDays(PLUS_ONE_MONTH_OR_WEEK_OR_DAY);
//                    }
//                    // 첫번째 요일 (자바기준 week) 획득 - weekWalk
//                    while (true) {
//                        // 1주씩 추가
//                        weekWalk = weekWalk.plusWeeks(PLUS_ONE_MONTH_OR_WEEK_OR_DAY);
//                        // 요일 체크 날짜가 해당 월의 마지막날과 같거나, 마지막날 보다 크면 break;
//                        if (weekWalk.isEqual(dto.getSelectedDate().withDayOfMonth(dto.getSelectedDate().lengthOfMonth()))
//                                || weekWalk.isAfter(dto.getSelectedDate())) {
//                            return;
//                        }
//                        // 요일 체크 날짜가 요청 온 날짜와 같다면 result 에 추가, break;
//                        if (weekWalk.isEqual(dto.getSelectedDate())) {
//                            result.add(new TodoSelectVo(todo.getItodo(), todo.getTodoContent(),
//                                    todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime()));
//                            return;
//                        }
//                    }
//                }
//                if (todo.getRepeatType().equalsIgnoreCase(MONTH)) {
//                    if (todo.getRepeatNum() == dto.getSelectedDate().getDayOfMonth()) {
//                        result.add(new TodoSelectVo(todo.getItodo(), todo.getTodoContent(),
//                                todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime()));
//                    }
//                }
//            } catch (NullPointerException e) {
//                /*
//                    반복이 없는경우 로직
//                    (endDate 는 무조건 해당 날짜보다 같거나 이후이고, startDate 는 무조건 해당날짜와 같거나 이전임이 보장된 상황.)
//                 */
//
//                result.add(new TodoSelectVo(todo.getItodo(), todo.getTodoContent(),
//                        todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime()));
//            }
//        });
//
//        // 10개로 줄이기
//        return result.subList(TODO_SELECT_FROM_NUM, result.size() > TODO_MAX_SIZE ? TODO_SELECT_TO_NUM : result.size());
//    }
//
//    @Transactional
//    public ResVo patchTodo(PatchTodoDto dto) {
//
//        /*
//        로직 -> start_date, end_date, start_time, end_time, repeat_end_date 중 하나라도 null 이 아니면,
//        select 문을 통해 해당 itodo 로 부터 start_date, end_date, start_time, end_time, repeat_end_date 를 가져와서,
//        LocalDate startDate;
//        LocalDate endDate;
//        LocalTime startTime;
//        LocalTime endTime;
//        LocalDate repeatEndDate;
//        의 5가지 객체를 일단 각각 만들어 둔다.
//        (repeatEndDate 가 db에서도 null 이고, 프론트에서 넘어온 값도 null 이면 repeatEndDate 는 신경쓰지 않아도 된다.)
//        그후,
//        프론트로부터 제공받은 start_date + start_time || end_date + end_time 으로 날짜를 만드는데,
//        만약 그들중 null 인 부분이 있다면 db에서 가져온 값을 대입한다.
//        그리고 그 두 날짜를 비교한다.
//        만약 start_date + start_time 이 end_date + end_time 보다 1분이라도 이후라면 (isAfter) 정상로직으로,
//        데이터를 update 한다.
//        else 라면 예외처리한다. (제공된 날짜 정보가 잘못되었다)
//        +
//        repeat_end_date 도 검증해야한다.
//        만약 프론트에서 넘어온 값이 있다면 end_date + end_time 과 비교, repeat_end_date 가 더 이후인지 체크,
//        없다면 db에 저장된 repeat_end_date 가 end_date + end_time 보다 이후인지 체크해야 한다.
//        만약, 프론트에서도 repeat_end_date 가 넘어오지 않았고(null 이고), db에서 가져온 repeat_end_date 도 null 이라면
//        그냥 set을 하지 않으면 된다.
//         */
//
//        /*
//        일단 다 가져와서 검증하는 모델 생성
//        검증
//        문제없으면 PatchTodoDto 를 <if> 이용하여 update
//         */
//
//        TodoSelectTmpResult selectResult = repository.findTodoAndRepeatBy(
//                new TodoSelectDtoForUpdate(dto.getIuser(), dto.getItodo())).get(0);
//        // DB에 해당 _TODO 가 있는지 여부 체크 (수정이므로 있음이 보장되어야 함)
//        if (selectResult == null) {
//            throw new NoSuchDataException(NO_SUCH_DATA_EX_MESSAGE);
//        }
//
//        // 두 데이터 병합 (넘어온 수정 데이터에서 null 인 부분은 DB에서 가져온 데이터로 채움)
//        CheckTodoAndRepeatDto checkResultData = checkTodoData(dto, selectResult);
//
//
//        // startDate & endDate 오류 검증
//        checkIsBefore(checkResultData.getEndDate(), checkResultData.getStartDate());
//        // startTime & endTime 오류 검증
//        checkIsBefore(checkResultData.getEndTime(), checkResultData.getStartTime());
//
//
//        // 데이터에는 오류가 없음이 보장됨.
//
//        /*
//        수정에서는 진짜 수정만 하자.
//        repeat 데이터의 삭제는 delete 로 따로 만들자.
//        수정페이지에서 삭제버튼을 누르면 해당 repeat 의 delete 로 요청하면 되는 부분이다.
//        이유 : 수정 데이터에서 repeat 정보가 null 이 들어올 경우,
//        이게 삭제를 위한 null 인지, 아무수정도 하지 않아서 null 인지 알 수가 없다.
//        다만, db에 반복 데이터가 없는데 수정 데이터에 반복 데이터가 있다면 insert 할 수는 있다.
//
//        수정 분기
//        1. _todo 만 update 하는 경우 - 단순 수정
//        ㄴ> if 분기 (repeatEndDate != null && repeatNum != null)
//        2. db 에 repeat 정보가 있고, dto 에도 repeat 정보가 있는 경우 - 단순 수정
//        ㄴ> 1의 분기 중 else
//        3. db 에 repeat 정보가 없는데, dto 에는 repeat 정보가 있는 경우 - insert
//        ㄴ> 1의 분기 중 else 에서 try - catch
//        (CheckTodoAndRepeatDto 의 repeatEndDate, repeatType, repeatNum 은 null 이 아니어야 한다.
//        만약 null 이라면 DB 에 데이터가 원래 없었다는 뜻이므로, dto 에서 넘어온 데이터를 insert 해야한다.
//        그 후, _todo 도 update 해야 한다.)
//         */
//
//
//        if (repository.checkIsRepeat(dto.getIuser(), dto.getItodo())) {
//            // 기존 일정이 repeat 이 아닌 일정일 경우
//            try {
//                checkRepeatTypeAndRepeatNum(dto.getRepeatType(), dto.getRepeatNum());
//                // repeat insert
//
//                if (repository.saveRepeat(new RepeatInsertDto(dto,
//                        dto.getRepeatType().equalsIgnoreCase(WEEK) ?
//                                // resolve week format from JS to JAVA
//                                commonUtils.toJavaFrom(dto.getRepeatNum()) :
//                                dto.getRepeatNum())) == 0) {
//                    throw new RepeatSaveException(REPEAT_SAVE_FAIL_EX_MESSAGE);
//                }
//                // t_todo update (at last)
//            } catch (NullPointerException e) {
//                checkRepeatNumInCatch(dto.getRepeatNum());
//                // t_todo update (at last)
//            }
//        } else {
//            // 기존 일정이 repeat 인 일정일 경우
//            try {
//                checkRepeatTypeAndRepeatNum(dto.getRepeatType(), dto.getRepeatNum());
//                // repeat update
//
//                // resolve week format from JS to JAVA
////                repository.updateRepeat(new RepeatInsertDto(dto.getItodo(), dto.getRepeatEndDate(), dto.getRepeatType(),
////                        dto.getRepeatType().equalsIgnoreCase(WEEK) ?
////                                commonUtils.toJavaFrom(dto.getRepeatNum()) :
////                                dto.getRepeatNum()));
//                // t_todo update (at last)
//            } catch (NullPointerException e) {
//                // 수정사항에 repeat 정보가 없는경우 == 기존 repeat 을 제거하거나 애초에 repeat 일정이 아닌경우
//                // 두가지 경우에 관계 없이 delete query 실행.
//                checkRepeatNumInCatch(dto.getRepeatNum());
//                // repeat delete
//                repository.deleteRepeat(dto.getIuser(), dto.getItodo());
//                // t_todo update (at last)
//            }
//        }
//        // t_todo update !  (공통 사항)
//        return new ResVo(repository.updateTodoAndRepeatIfExists(dto));
//    }
//
//    public ResVo deleteTodo(TodoDeleteDto dto) {
//        // repeat 유무 관계 없이 delete query 실행.
//        repository.deleteRepeat(dto.getIuser(), dto.getItodo());
//        int result = repository.deleteTodo(dto);
//        if (result == 0) {
//            // 요청받은 iuser, itodo 로 삭제되는 일정이 없을경우 NoSuchDataException 발생.
//            throw new NoSuchDataException(NO_SUCH_DATA_EX_MESSAGE);
//        }
//        return new ResVo(result);
//    }
//
//    /*
//     * ------- Extracted Methods -------
//     */
//
//    private void checkRepeatTypeAndRepeatNum(String repeatType, Integer repeatNum) {
//        if (!repeatType.equalsIgnoreCase(WEEK) &&
//                !repeatType.equalsIgnoreCase(MONTH)) {
//            throw new BadDateInformationException(BAD_DATE_INFO_EX_MESSAGE);
//        }
//        if (repeatNum == null) {
//            throw new BadInformationException(BAD_REQUEST_EX_MESSAGE);
//        }
//    }
//
//    private void checkRepeatNumInCatch(Integer repeatNum) {
//        if (repeatNum != null) {
//            throw new BadInformationException(BAD_REQUEST_EX_MESSAGE);
//        }
//    }
//
//    private void checkIsBefore(LocalDate endDate, LocalDate startDate, LocalTime endTime,
//                               LocalTime startTime) {
//        checkIsBefore(endDate, startDate);
//        checkIsBefore(endTime, startTime);
//    }
//
//    private void checkIsBefore(LocalDate endDate, LocalDate startDate) {
//        if (endDate.isBefore(startDate)) {
//            throw new BadDateInformationException(BAD_DATE_INFO_EX_MESSAGE);
//        }
//    }
//
//    private void checkIsBefore(LocalTime endTime, LocalTime startTime) {
//        if (endTime.isBefore(startTime)) {
//            throw new BadDateInformationException(BAD_TIME_INFO_EX_MESSAGE);
//        }
//    }
//
//    private CheckTodoAndRepeatDto checkTodoData(PatchTodoDto dto, TodoSelectTmpResult selectResult) {
//        return CheckTodoAndRepeatDto.builder()
//                .todoContent(dto.getTodoContent() == null ? selectResult.getTodoContent() : dto.getTodoContent())
//                .startDate(dto.getStartDate() == null ? selectResult.getStartDate() : dto.getStartDate())
//                .endDate(dto.getEndDate() == null ? selectResult.getEndDate() : dto.getEndDate())
//                .startTime(dto.getStartTime() == null ? selectResult.getStartTime() : dto.getStartTime())
//                .endTime(dto.getEndTime() == null ? selectResult.getEndTime() : dto.getEndTime())
//                .repeatEndDate(dto.getRepeatEndDate() == null ? selectResult.getRepeatEndDate() : dto.getRepeatEndDate())
//                .repeatType(dto.getRepeatType() == null ? selectResult.getRepeatType() : dto.getRepeatType())
//                .repeatNum(dto.getRepeatNum() == null ? selectResult.getRepeatNum() : dto.getRepeatNum())
//                .build();
//    }
//}
