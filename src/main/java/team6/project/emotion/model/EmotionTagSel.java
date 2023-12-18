package team6.project.emotion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "이모션 태그들 전부 조회 ",title = "이모션 태그들 전부 조회")
public class EmotionTagSel {
    @Schema(title = "이모션 태그 Pk",minimum = "1",maximum = "28",type = "int")
    private int emotionPk;
}
