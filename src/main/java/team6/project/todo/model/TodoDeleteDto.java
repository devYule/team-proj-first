package team6.project.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class TodoDeleteDto {
    private Integer iuser;
    private Integer itodo;

}
