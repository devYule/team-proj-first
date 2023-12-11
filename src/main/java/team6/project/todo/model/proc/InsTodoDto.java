package team6.project.todo.model.proc;

import lombok.Getter;
import lombok.Setter;
import team6.project.todo.model.TodoRegDto;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class InsTodoDto {
    private Integer itodo;
    private Integer iuser;
    private String todoContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public InsTodoDto(TodoRegDto dto) {
        this.iuser = dto.getIuser();
        this.todoContent = dto.getTodoContent();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        if(dto.getStartDate() != null) {
            this.startTime = dto.getStartTime();
        }

        this.endTime = dto.getEndTime();
    }
}
