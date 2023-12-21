package team6.project.emotion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.PathVariable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "이모션 삭제",title = "선택한 이모션 삭제")
public class EmotionDelDto {
    @Schema(title = "유저 PK", minimum = "1", type = "int", description = "필수값", defaultValue = "유저PK")
    private int iuser;

    @Schema(title = "유저 PK", minimum = "1", type = "int", description = "필수값", defaultValue = "이모션PK")
    private Integer iemotion;
}
