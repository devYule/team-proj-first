package team6.project.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Schema(name = "유저 수정", title = "유저 수정")
public class UserUpDto {

    @NotNull(message = "유저 PK 는 필수값")
    @Range(min=1L, message = "유저 PK 는 1 이상")
    @Schema(title = "유저 PK", minimum = "1", type = "Integer", description = "필수값")
    private int iuser;

    @NotNull(message = "유저 NickName은 필수값")
    @Range(min = 1,max = 10, message = "1자이상 10자미만")
    @Schema(title = "유저 NickName", minimum = "1", maximum = "10" ,type = "String", description = "필수값")
    private String userNickName;

    @Range(min=0, max=2 ,message = "유저 gender는 0이상 2이하")
    @Schema(title = "유저 Gender", minimum = "0", maximum = "2" ,type = "Integer", description = "선택값")
    private Integer userGender;

    @Range(min=0, max=150 ,message = "유저 age는 0이상 150이하")
    @Schema(title = "유저 Age", minimum = "0", maximum = "150" ,type = "Integer", description = "선택값")
    private Integer userAge;
}
