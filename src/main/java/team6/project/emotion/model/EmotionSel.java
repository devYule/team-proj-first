package team6.project.emotion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "이모션 단계,요일",title = "일 단위의 이모션 단계, 해당 요일 리턴, 일주일 모두 출력")
public class EmotionSel {
    @Schema(title = "이모션 단계",minimum = "1",defaultValue = "3",maximum = "5",description = "일주일 단위로 출력하는<br>" +
            "이모션의 단계 리턴. 기본값 3 <br>1:매우좋음,2:좋음,3:보통,4:나쁨,5:매우나쁨")
    private int emotionGrade=3;
    @Schema(title = "해당 요일",minimum = "0",maximum = "6",description =
    "월요일 : 0 , 화요일 : 1 , 수요일 : 2 ... 일요일 : 6")
    private int dayOfTheWeek;

}
