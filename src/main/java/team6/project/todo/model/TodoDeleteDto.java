package team6.project.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "투두 삭제", title = "투두 삭제")
public class TodoDeleteDto {
    @Schema(title = "유저 PK", minimum = "1", type = "Integer", description = "필수값", defaultValue = "1")
    private Integer iuser;
    @Schema(title = "투두 PK", minimum = "1", type = "Integer", description = "필수값", defaultValue = "1")
    private Integer itodo;

}
