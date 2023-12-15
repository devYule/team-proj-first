package team6.project.todo.model.proc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team6.project.todo.model.PatchTodoDto;
import team6.project.todo.model.TodoRegDto;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepeatInsertDto {
    private Integer itodo;
    private LocalDate repeatEndDate;
    private String repeatType;
    private Integer repeatNum;

    public RepeatInsertDto(TodoRegDto dto, Integer itodo, Integer repeatNum) {
        this.itodo = itodo;
        this.repeatEndDate = dto.getRepeatEndDate();
        this.repeatType = dto.getRepeatType();
        this.repeatNum = repeatNum;
    }
    public RepeatInsertDto(PatchTodoDto dto, Integer repeatNum) {
        this.itodo = dto.getItodo();
        this.repeatEndDate = dto.getRepeatEndDate();
        this.repeatType = dto.getRepeatType();
        this.repeatNum = repeatNum;
    }
}
