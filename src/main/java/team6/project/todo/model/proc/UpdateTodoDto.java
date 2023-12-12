package team6.project.todo.model.proc;

import lombok.Getter;
import team6.project.todo.model.PatchTodoDto;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class UpdateTodoDto {
    private Integer itodo;
    private Integer iuser;
    private String todoContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public UpdateTodoDto(PatchTodoDto dto) {
        this.itodo = dto.getItodo();
        this.iuser = dto.getIuser();
        this.todoContent = dto.getTodoContent();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
    }
}
