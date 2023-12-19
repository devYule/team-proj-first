package team6.project.emotion.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "날짜별 이모션,Todo 조회", title = "날짜,이모션단계,이모션태그,일정이 있는지 확인")
public class EmotionSelVo {
    @Schema(title = "날짜", minimum = "1", maximum = "31",type = "String")
    private String day;
    @Schema(title = "이모션 단계", minimum = "1", maximum = "5",type = "int")
    private int emotionGrade;
    @Schema(title = "이모션 태그",type = "String")
    private String emotionTag;
    @Schema(title = "일정", minimum = "0", maximum = "1",type = "int",description = "1이면 일정O,0이면 일정X")
    private int hasTodo;
}
