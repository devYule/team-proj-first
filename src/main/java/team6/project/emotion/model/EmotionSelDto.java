package team6.project.emotion.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "월 단위 이모션,todo 조회", title = "월별 Todo, Emotion 조회")
public class EmotionSelDto {
    @Schema(title = "유저 PK", minimum = "1", type = "int", description = "필수값", defaultValue = "유저PK")
    private int iuser;
    @Schema(title = "연도", minimum = "1", maximum = "9999",type = "int",description = "해당하는 년도")
    private int year;
    @Schema(title = "달", minimum = "1", maximum = "12",type = "int",description = "해당하는 달")
    private int month;
}
