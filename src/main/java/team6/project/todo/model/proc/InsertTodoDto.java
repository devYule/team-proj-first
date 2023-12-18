package team6.project.todo.model.proc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team6.project.todo.model.TodoRegDto;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class InsertTodoDto {
    private Integer itodo;
    private Integer iuser;
    private String todoContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public InsertTodoDto(TodoRegDto dto) {
        this.iuser = dto.getIuser();
        this.todoContent = dto.getTodoContent();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
    }


}
