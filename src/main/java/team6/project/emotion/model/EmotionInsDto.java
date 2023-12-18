package team6.project.emotion.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Schema(name = "이모션 등록",title = "일 단위의 이모션 등록")
public class EmotionInsDto {
    @Schema(title = "유저 PK", minimum = "1", type = "int", description = "필수값", defaultValue = "유저PK")
    private int iuser;
    @NotNull
    @Range(min = 1,max = 5,message = "올바르지 않은 이모션 단계 입력")
    @Schema(title = "이모션 단계", minimum = "1", maximum = "5",type = "int")
    private Integer emoGrade;
    @Schema(title = "이모션 태그",type = "String")
    @NotBlank(message = "올바르지 않은 이모션 태그 값 입력")
    private String emoTag;
    @JsonIgnore
    private int emoTagInt;
    // 이모션 태그 String -> int(PK)
}
