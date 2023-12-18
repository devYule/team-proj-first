package team6.project.emotion.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "이번주 이모션 차트", title = "주간 차트",description = "해당하는 주의 일별" +
        "emotionGrade 조회, good,normal,bad 3가지 단계로 분기")
public class EmotionSelAsChartVo {
    @Schema(title = "일별 이모션 단계",type = "List<EmotionSel>",description = "List타입")
    private List<EmotionSel> emoChart;
    @Schema(title = "좋아요",defaultValue = "0",type = "int",description = "일단위로 Grade 분기하여 " +
            "Good,Normal,Bad 에 +1")
    private int good=0;
    @Schema(title = "보통",defaultValue = "0",type = "int",description = "일단위로 Grade 분기하여 " +
            "Good,Normal,Bad 에 +1")
    private int normal=0;
    @Schema(title = "나빠요",defaultValue = "0",type = "int",description = "일단위로 Grade 분기하여 " +
            "Good,Normal,Bad 에 +1")
    private int bad=0;
}
