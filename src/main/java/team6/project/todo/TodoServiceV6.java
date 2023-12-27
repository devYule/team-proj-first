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
@Service
@RequiredArgsConstructor
public class TodoServiceV6 implements TodoServiceRef {

    private final TodoRepositoryRef repository;

    public ResVo regTodo(TodoRegDto dto) {

        if (repository.getListCountById(dto.getIuser(), dto.getStartDate()) >= TODO_MAX_SIZE) {
            throw new TodoIsFullException(TODO_IS_FULL_EX_MESSAGE);
        }


        CommonUtils.checkDateInfo(
                LocalDateTime.of(dto.getEndDate(),
                        dto.getEndTime() == null ? LocalTime.of(23, 59, 59) : dto.getEndTime()),
                LocalDateTime.of(dto.getStartDate(),
                        dto.getStartTime() == null ? LocalTime.of(0, 0, 0) : dto.getStartTime())
        );
        try {
            // NPE & checkRepeatInfos
            checkRepeatInfo(dto.getEndDate(), dto.getStartDate(), dto.getRepeatEndDate(), dto.getEndTime(), dto.getStartTime(),
                    dto.getRepeatType(),
                    dto.getRepeatType().equalsIgnoreCase(WEEK) ? CommonUtils.toJavaFrom(dto.getRepeatNum()) : dto.getRepeatNum());


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


            InsertTodoDto insertTodoDto = new InsertTodoDto(dto);
            if (repository.saveTodo(insertTodoDto) == 0) {
                throw new RuntimeException(RUNTIME_EX_MESSAGE);
            }
            return new ResVo(insertTodoDto.getItodo());
        }
    }


    public TodoSelectVo getTodo(TodoSelectTransVo dto) {
        List<TodoSelectTmpResult> allTodos = repository.findTodoAndRepeatBy(dto);
        EmotionSelectTmpResult emotionSelectTmpResult = repository.findEmotionAndEmotionTagBy(dto);
        TodoSelectVo todoSelectVo = new TodoSelectVo();
        if (emotionSelectTmpResult != null) {
            todoSelectVo.setEmotionGrade(emotionSelectTmpResult.getEmotion());
            todoSelectVo.setEmotionTag(emotionSelectTmpResult.getEmotionTag());
        }
        List<TodoInfo> result = new ArrayList<>();

        allTodos.forEach(todo -> {
            try {
                if (todo.getRepeatType().equalsIgnoreCase(WEEK)) {
                    if (todo.getRepeatNum() == dto.getSelectedDate().getDayOfWeek().getValue()) {
                        result.add(new TodoInfo(todo.getItodo(), todo.getTodoContent(),
                                todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime(),
                                todo.getRepeatEndDate(), todo.getRepeatType(), todo.getRepeatNum()));
                        return;
                    }
                }
                if (todo.getRepeatType().equalsIgnoreCase(MONTH)) {
                    if (todo.getRepeatNum() <= dto.getSelectedDate().lengthOfMonth()) {
                        return;
                    }
                    if (todo.getRepeatNum() == dto.getSelectedDate().getDayOfMonth()) {
                        result.add(new TodoInfo(todo.getItodo(), todo.getTodoContent(),
                                todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime()
                                , todo.getRepeatEndDate(), todo.getRepeatType(), todo.getRepeatNum()
                        ));
                    }
                }
            } catch (NullPointerException e) {
                result.add(new TodoInfo(todo.getItodo(), todo.getTodoContent(),
                        todo.getStartDate(), todo.getEndDate(), todo.getStartTime(), todo.getEndTime()));
            }
        });

        todoSelectVo.setTodos(result);
        return todoSelectVo;
    }

    public ResVo patchTodo(PatchTodoDto dto) {

        CommonUtils.checkObjectAllNullThrow(NotEnoughInformationException.class, NOT_ENOUGH_INFO_EX_MESSAGE,
                dto.getTodoContent(), dto.getStartDate(), dto.getEndDate(), dto.getStartTime(),
                dto.getEndTime(), dto.getRepeatEndDate(), dto.getRepeatType(), dto.getRepeatNum());

        TodoSelectTmpResult selectResult;
        try {
            selectResult = repository.findTodoAndRepeatBy(new TodoSelectVoForUpdate(dto.getItodo(), dto.getIuser())).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchDataException(NO_SUCH_DATA_EX_MESSAGE);
        }
        MergedTodoAndRepeatDto mergedTodoAndRepeat = mkTodoObject(dto, selectResult);

        boolean hasRepeatInfoInDB = selectResult.getRepeatEndDate() != null;

        CommonUtils.checkDateInfo(
                LocalDateTime.of(mergedTodoAndRepeat.getEndDate(), mergedTodoAndRepeat.getEndTime()),
                LocalDateTime.of(mergedTodoAndRepeat.getStartDate(), mergedTodoAndRepeat.getStartTime())
        );


        try {
            LocalDate startDateWalker;
            // NPE
            if (mergedTodoAndRepeat.getRepeatType().equalsIgnoreCase(WEEK)) {
                startDateWalker = LocalDate.of(
                        mergedTodoAndRepeat.getStartDate().getYear(),
                        mergedTodoAndRepeat.getStartDate().getMonth(),
                        mergedTodoAndRepeat.getStartDate().getDayOfMonth()
                );
                startDateWalker = startDateWalker.plusWeeks(1);
            } else {
                startDateWalker = LocalDate.of(
                        mergedTodoAndRepeat.getStartDate().getYear(),
                        mergedTodoAndRepeat.getStartDate().getMonth(),
                        mergedTodoAndRepeat.getStartDate().getDayOfMonth()
                );
                startDateWalker = startDateWalker.plusMonths(1);
            }

            final LocalDate repeatEndDate = mergedTodoAndRepeat.getRepeatEndDate();

            CommonUtils.checkObjectAnyNullThrow(NotEnoughInformationException.class,
                    NOT_ENOUGH_INFO_EX_MESSAGE,
                    repeatEndDate, dto.getRepeatNum());

            // insert 와 같은 반복 검증 시작

            Integer checkingNum = dto.getRepeatNum() != null ?
                    mergedTodoAndRepeat.getRepeatType().equalsIgnoreCase(WEEK) ?
                            CommonUtils.toJavaFrom(mergedTodoAndRepeat.getRepeatNum()) : mergedTodoAndRepeat.getRepeatNum() :
                    mergedTodoAndRepeat.getRepeatNum();

            checkRepeatInfo(mergedTodoAndRepeat.getEndDate(), mergedTodoAndRepeat.getStartDate(),
                    mergedTodoAndRepeat.getRepeatEndDate(), mergedTodoAndRepeat.getEndTime(),
                    mergedTodoAndRepeat.getStartTime(), mergedTodoAndRepeat.getRepeatType(),
                    checkingNum);
            CommonUtils.checkDateInfo(
                    LocalDateTime.of(repeatEndDate, LocalTime.of(23, 59, 59)),
                    LocalDateTime.of(startDateWalker, mergedTodoAndRepeat.getStartTime())
            );

            if (!hasRepeatInfoInDB) {
                repository.saveRepeat(new RepeatInsertDto(dto, CommonUtils.toJavaFrom(mergedTodoAndRepeat.getRepeatNum())));
                mergedTodoAndRepeat.setRepeatEndDate(null);
                mergedTodoAndRepeat.setRepeatType(null);
                mergedTodoAndRepeat.setRepeatNum(null);

            }
            if (mergedTodoAndRepeat.getRepeatType().equalsIgnoreCase(WEEK) && dto.getRepeatNum() != null) {
                dto.setRepeatNum(CommonUtils.toJavaFrom(dto.getRepeatNum()));
            }

        } catch (NullPointerException e) {


            CommonUtils.checkObjectAnyNotNullThrow(
                    BadInformationException.class,
                    NOT_ENOUGH_INFO_EX_MESSAGE,
                    mergedTodoAndRepeat.getRepeatEndDate(),
                    mergedTodoAndRepeat.getRepeatType(),
                    mergedTodoAndRepeat.getRepeatNum()
            );
        }

        return new ResVo(repository.updateTodoAndRepeatIfExists(dto));
    }

    public ResVo deleteTodo(TodoDeleteDto dto, Integer delOnlyRepeat) {
        RepeatInsertDto backUpRepeat = repository.findRepeatBy(dto.getItodo());
        Integer delRepeatResult = repository.deleteRepeat(dto);
        if (delOnlyRepeat != null && delOnlyRepeat == 1) {
            checkResultIfNullOrZeroThrow(delRepeatResult);
            return new ResVo(delRepeatResult);
        }

        Integer result = repository.deleteTodo(dto);
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
        if (mergedTodoAndRepeatDto.getRepeatType() != null &&
                mergedTodoAndRepeatDto.getRepeatType().equalsIgnoreCase(WEEK) && dto.getRepeatNum() != null) {
            mergedTodoAndRepeatDto.setRepeatNum(CommonUtils.toJavaFrom(mergedTodoAndRepeatDto.getRepeatNum()));
        }
        return mergedTodoAndRepeatDto;
    }

    private static void checkRepeatInfo(LocalDate endDate, LocalDate startDate, LocalDate repeatEndDate,
                                        LocalTime endTime, LocalTime startTime,
                                        String repeatType, Integer repeatNum) {

        CommonUtils.checkRepeatNumWithRepeatType(repeatType, repeatNum);
        CommonUtils.checkStartDateWithRepeatInfo(startDate, repeatType, repeatNum);
        CommonUtils.checkDateWhenRepeat(endDate, startDate);
        CommonUtils.checkTimeWhenRepeat(endTime, startTime);
        CommonUtils.checkRepeatEndDate(repeatEndDate, startDate, repeatType);

    }
}
