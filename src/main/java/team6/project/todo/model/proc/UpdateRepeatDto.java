package team6.project.todo.model.proc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateRepeatDto {
    private Integer itodo;
    private LocalDate repeatEndDate;
    private String repeatType;
    private Integer repeatNum;


}
