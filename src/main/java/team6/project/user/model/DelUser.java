package team6.project.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Schema(name = "유저 삭제", title = "유저 삭제")
public class DelUser {
    @Schema(title = "유저 PK", minimum = "1", type = "Integer", description = "필수값", defaultValue = "유저pk")
    private int iuser;

    @Schema(title = "유저 itodo", minimum = "1", type = "Integer", description = "필수값", defaultValue = "유저itodo")
    private int itodo;



}
