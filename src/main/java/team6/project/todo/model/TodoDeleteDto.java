package team6.project.todo.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TodoDeleteDto {
    private Integer iuser;
    private Integer itodo;
}
