package team6.project.emotion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "이모션 단계,요일",title = "일 단위의 이모션 단계, 해당 요일")
public class EmotionSel {
    @Schema(title = "이모션 단계",minimum = "1",defaultValue = "3",maximum = "5",description = "해당일의 이모션 단계")
    private int emotionGrade=3;
    @Schema(title = "해당 요일",minimum = "1",maximum = "7",description =
    "월요일 : 1 , 화요일 : 2 , 수요일 : 3 ... 일요일 : 7")
    private int dayOfTheWeek;

}
