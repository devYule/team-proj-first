package team6.project.emotion.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import team6.project.common.Const;

import static team6.project.common.Const.*;

@Data
@Schema(name = "이모션 등록",title = "일 단위의 이모션 등록")
public class EmotionInsDto {
    @Schema(title = "유저 PK", minimum = "1", type = "int", description = "필수값이며,등록한 유저마다<br>다른iuser값을 가지고 있음.", defaultValue = "유저PK")
    private int iuser;
    @NotNull(message = NOT_ENOUGH_INFO_EX_MESSAGE)
    @Range(min = 1,max = 5,message = BAD_INFO_EX_MESSAGE)
    @Schema(title = "이모션 단계", minimum = "1", maximum = "5",type = "int",description = "이모션 단계를 입력.<br>최소 1~ 최대5 까지.")
    private Integer emoGrade;
    @Schema(title = "이모션 태그",type = "String",description = "이모션 태그를 입력.<br> ex)괴로운,귀찮은,기쁜,즐거운 ... 총 28가지.")
    @NotBlank(message = BAD_INFO_EX_MESSAGE)
    private String emoTag;
    @JsonIgnore
    private int emoTagInt;
    // 이모션 태그 String -> int(PK)
}
