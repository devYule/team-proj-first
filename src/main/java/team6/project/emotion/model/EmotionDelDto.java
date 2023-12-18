package team6.project.emotion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "이모션 삭제",title = "선택한 이모션 삭제")
public class EmotionDelDto {
    @Schema(title = "유저 PK", minimum = "1", type = "int", description = "필수값", defaultValue = "유저PK")
    private int iuser;
    @Schema(title = "유저 PK", minimum = "1", type = "int", description = "필수값", defaultValue = "이모션PK")
    private int iemotion;
}
