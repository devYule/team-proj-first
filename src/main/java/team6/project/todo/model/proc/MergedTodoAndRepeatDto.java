package team6.project.todo.model.proc;

import team6.project.todo.model.ref.TodoInfosRef;

import java.time.LocalDate;
import java.time.LocalTime;

public class MergedTodoAndRepeatDto extends TodoInfosRef {
    /**
     * AllArgsConstructor
     */
    public MergedTodoAndRepeatDto(String todoContent, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, LocalDate repeatEndDate, String repeatType, Integer repeatNum) {
        super(todoContent, startDate, endDate, startTime, endTime, repeatEndDate, repeatType, repeatNum);
    }

    public MergedTodoAndRepeatDto() {
    }
}
