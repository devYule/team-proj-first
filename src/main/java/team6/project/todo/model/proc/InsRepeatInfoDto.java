package team6.project.todo.model.proc;

import lombok.Getter;
import lombok.Setter;
import team6.project.todo.model.PatchTodoDto;
import team6.project.todo.model.TodoRegDto;

import java.time.LocalDate;

@Getter
@Setter
public class InsRepeatInfoDto {
    private Integer itodo; // generatedKey
    private LocalDate repeatEndDate; // nullable
    private String repeatType; // not null
    private Integer repeatNum; // not null

    public InsRepeatInfoDto(TodoRegDto dto, Integer itodo, Integer repeatNum) {
        this.itodo = itodo;
        this.repeatEndDate = dto.getRepeatEndDate();
        this.repeatType = dto.getRepeatType();
        this.repeatNum = repeatNum;
    }
    public InsRepeatInfoDto(PatchTodoDto dto, Integer repeatNum){
        this.itodo = dto.getItodo();
        this.repeatEndDate = dto.getRepeatEndDate();
        this.repeatType = dto.getRepeatType();
        this.repeatNum = repeatNum;
    }
}
