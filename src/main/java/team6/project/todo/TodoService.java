package team6.project.todo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team6.project.common.ResVo;
import team6.project.common.exception.BadDateInformationException;
import team6.project.common.exception.BadInformationException;
import team6.project.common.exception.NoSuchDataException;
import team6.project.common.exception.TodoIsFullException;
import team6.project.common.utils.WeekFormatResolver;
import team6.project.todo.model.*;
import team6.project.todo.model.proc.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static team6.project.common.Const.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoMapper mapper;

    private final WeekFormatResolver weekFormatResolver;

    @Transactional
    public ResVo regTodo(TodoRegDto dto) {
        // 투두 는 10개까지만 저장.
        if (mapper.getTodoListCount(dto.getIuser()) > 9) {
            throw new TodoIsFullException(TODO_IS_FULL_EX_MESSAGE);
        }

        // startDate & endDate 그리고 startTime & endTime 오류 검증
        checkIsBefore(dto.getEndDate(), dto.getStartDate(), dto.getEndTime(), dto.getStartTime());

        try {
            checkRepeatTypeAndRepeatNum(dto.getRepeatType(), dto.getRepeatNum());
            // 반복 있을때 로직
            log.debug("todo service in try");
            InsertTodoDto insertTodoDto = new InsertTodoDto(dto);
            mapper.insTodo(insertTodoDto);

            InsRepeatInfoDto insRepeatInfoDto = new InsRepeatInfoDto(dto, insertTodoDto.getItodo(),
                    dto.getRepeatType().equalsIgnoreCase(WEEK) ?
                            // resolve week format from JS to JAVA
                            weekFormatResolver.toJavaFrom(dto.getRepeatNum()) :
                            dto.getRepeatNum());
            mapper.insRepeat(insRepeatInfoDto);
            return new ResVo(insRepeatInfoDto.getItodo());

        } catch (NullPointerException e) {
            // 반복 없을때 로직
            log.debug("todo service in catch");
            checkRepeatNumInCatch(dto.getRepeatNum());
            InsertTodoDto insertTodoDto = new InsertTodoDto(dto);
            mapper.insTodo(insertTodoDto);
            return new ResVo(insertTodoDto.getItodo());
        }
    }

    public List<TodoSelectVo> getTodo(TodoSelectDto dto) {
        /* TODO: 12/12/23
            테스트 정상인지 체크하기
            (forEach() 내부 while 문에 break; 대신 return 으로 변경 했음.)
            --by Hyunmin */
        // 정제 전
        List<TodoSelectTmpResult> allTodos = mapper.selectTodo(dto);

        // 정제
        List<TodoSelectVo> result = new ArrayList<>();
        allTodos.forEach(todo -> {
            try {
                // 주반복
                if (todo.getRepeatType().equalsIgnoreCase(WEEK)) {
                    // 1: 월 ~ 7: 일 ('JAVA format') 로 DB 에 저장되어 있는것 사용.
                    // 현재 update 는 front 로 요일정보를 넘기지 않기 때문에 WeekFormatResolver 사용 하지 않음.
//                    LocalDate weekWalk = dto.getSelectedDate().withDayOfMonth(FIRST_DAY);
                    LocalDate weekWalk = LocalDate.of(dto.getSelectedDate().getYear(), dto.getSelectedDate().getMonth(), FIRST_DAY);
                    while (weekWalk.getDayOfWeek().getValue() != todo.getRepeatNum()) {
                        // 첫번째 요일
                        weekWalk = weekWalk.plusDays(1);
                    }
                    // 첫번째 요일 (자바기준 week) 획득 - weekWalk
                    while (true) {
                        // 1주씩 추가
                        weekWalk = weekWalk.plusWeeks(1);
                        log.debug("weekWalk = {}", weekWalk);
                        // 요일 체크 날짜가 해당 월의 마지막날과 같거나, 마지막날 보다 크면 break;
                        if (weekWalk.isEqual(dto.getSelectedDate().withDayOfMonth(dto.getSelectedDate().lengthOfMonth()))
                                || weekWalk.isAfter(dto.getSelectedDate())) {
                            return;
                        }
                        // 요일 체크 날짜가 요청 온 날짜와 같다면 result 에 추가, break;
                        if (weekWalk.isEqual(dto.getSelectedDate())) {
                            result.add(new TodoSelectVo(todo.getItodo(), todo.getTodoContent()));
                            return;
                        }
                    }
                }
                if (todo.getRepeatType().equalsIgnoreCase(MONTH)) {
                    if (todo.getRepeatNum() == dto.getSelectedDate().getDayOfMonth()) {
                        result.add(new TodoSelectVo(todo.getItodo(), todo.getTodoContent()));
                    }
                }
            } catch (NullPointerException e) {
                /*
                    반복이 없는경우 로직
                    (endDate 는 무조건 해당 날짜보다 같거나 이후이고, startDate 는 무조건 해당날짜와 같거나 이전임이 보장된 상황.)
                 */

                result.add(new TodoSelectVo(todo.getItodo(), todo.getTodoContent()));
            }
        });

        // 10개로 줄이기
        return result.subList(TODO_SELECT_FROM_NUM, result.size() > 11 ? TODO_SELECT_TO_NUM : result.size());
    }

    @Transactional
    public ResVo patchTodo(PatchTodoDto dto) {
        // startDate & endDate 오류 검증
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            checkIsBefore(dto.getEndDate(), dto.getStartDate());
        }
        // startTime & endTime 오류 검증
        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            checkIsBefore(dto.getEndTime(), dto.getStartTime());
        }

        if (mapper.isRepeat(dto.getIuser(), dto.getItodo()) == 0) {
            // 기존 일정이 repeat 이 아닌 일정일 경우
            try {
                checkRepeatTypeAndRepeatNum(dto.getRepeatType(), dto.getRepeatNum());
                // repeat insert

                mapper.insRepeat(new InsRepeatInfoDto(dto,
                        dto.getRepeatType().equalsIgnoreCase(WEEK) ?
                                // resolve week format from JS to JAVA
                                weekFormatResolver.toJavaFrom(dto.getRepeatNum()) :
                                dto.getRepeatNum()));
                // t_todo update (at last)
            } catch (NullPointerException e) {
                checkRepeatNumInCatch(dto.getRepeatNum());
                // t_todo update (at last)
            }
        } else {
            // 기존 일정이 repeat 인 일정일 경우
            try {
                checkRepeatTypeAndRepeatNum(dto.getRepeatType(), dto.getRepeatNum());
                // repeat update

                // resolve week format from JS to JAVA
                mapper.patchRepeat(new UpdateRepeatDto(dto.getItodo(), dto.getRepeatEndDate(), dto.getRepeatType(),
                        dto.getRepeatType().equalsIgnoreCase(WEEK) ?
                                weekFormatResolver.toJavaFrom(dto.getRepeatNum()) :
                                dto.getRepeatNum()));
                // t_todo update (at last)
            } catch (NullPointerException e) {
                // 수정사항에 repeat 정보가 없는경우 == 기존 repeat 을 제거하거나 애초에 repeat 일정이 아닌경우
                // 두가지 경우에 관계 없이 delete query 실행.
                checkRepeatNumInCatch(dto.getRepeatNum());
                // repeat delete
                mapper.deleteTodoRepeat(dto.getIuser(), dto.getItodo());
                // t_todo update (at last)
            }
        }
        // t_todo update !  (공통 사항)
        return new ResVo(mapper.patchTodo(new UpdateTodoDto(dto)));
    }

    public ResVo deleteTodo(TodoDeleteDto dto) {
        // repeat 유무 관계 없이 delete query 실행.
        mapper.deleteTodoRepeat(dto.getIuser(), dto.getItodo());
        int result = mapper.deleteTodo(dto);
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
            throw new BadDateInformationException(BAD_DATE_INFO);
        }
        if (repeatNum == null) {
            throw new BadInformationException(BAD_REQUEST);
        }
    }

    private void checkRepeatNumInCatch(Integer repeatNum) {
        if (repeatNum != null) {
            throw new BadInformationException(BAD_REQUEST);
        }
    }

    private void checkIsBefore(LocalDate endDate, LocalDate startDate, LocalTime endTime,
                               LocalTime startTime) {
        checkIsBefore(endDate, startDate);
        checkIsBefore(endTime, startTime);
    }

    private void checkIsBefore(LocalDate endDate, LocalDate startDate) {
        if (endDate.isBefore(startDate)) {
            throw new BadDateInformationException(BAD_DATE_INFO);
        }
    }

    private void checkIsBefore(LocalTime endTime, LocalTime startTime) {
        if (endTime.isBefore(startTime)) {
            throw new BadDateInformationException(BAD_TIME_INFO);
        }
    }
}
