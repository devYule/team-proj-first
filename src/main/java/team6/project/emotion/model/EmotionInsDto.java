package team6.project.emotion.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
@Schema(name = "이모션 등록",title = "일 단위의 이모션 등록")
public class EmotionInsDto {
    @Schema(title = "유저 PK", minimum = "1", type = "int", description = "필수값", defaultValue = "유저PK")
    private int iuser;
    @Schema(title = "이모션 단계", minimum = "1", maximum = "5",type = "int")
    private int emoGrade;
    @Schema(title = "이모션 태그",type = "String")
    private String emoTag;
    @JsonIgnore
    private int emoTagInt;
    // 이모션 태그 String -> int(PK)
}
