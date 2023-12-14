package team6.project.todo.model.proc;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class UpdateTodoAndRepeatIfExistsDto {
    private Integer itodo;
    private Integer iuser;
    private String todoContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    // repeat
    private LocalDate repeatEndDate;
    private String repeatType;
    private Integer repeatNum;

}
