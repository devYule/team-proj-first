package team6.project.todo.model.proc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class CheckTodoAndRepeatDto {
    // _Todo Info
    private String todoContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    // Repeat Info
    private LocalDate repeatEndDate;
    private String repeatType;
    private Integer repeatNum;
}
