package team6.project.emotion.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "날짜별 이모션,Todo 조회", title = "날짜,이모션단계,이모션태그,일정이 있는지 확인")
public class EmotionSelVo {
    @Schema(title = "날짜", minimum = "1", maximum = "31",type = "String")
    private String day;
    @Schema(title = "이모션 단계", minimum = "1", maximum = "31",type = "Integer")
    private int emotionGrade;
    private int emotionTag;
    private int hasTodo;

}
