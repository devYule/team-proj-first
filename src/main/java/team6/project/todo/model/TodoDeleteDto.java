package team6.project.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Schema(name = "투두 삭제", title = "투두 삭제")
public class TodoDeleteDto {
    @Schema(title = "유저 PK", minimum = "1", type = "Integer", description = "필수값")
    private Integer iuser;
    @Schema(title = "투두 PK", minimum = "1", type = "Integer", description = "필수값")
    private Integer itodo;

}
