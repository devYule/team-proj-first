package team6.project.user.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Schema(name = "유저 등록", title = "유저 등록")
public class UserInsDto {
    @JsonIgnore
    @Schema(title = "유저 PK",description = "auto_increment")
    private int iuser;

    @Schema(title = "유저 NickName", minimum = "1", maximum = "10", type = "String", description = "필수값")
    @Range(min=1L, max =10 ,message = "유저 NickName은 1~10자")
    private String userNickName;

    @Range(min=0, max=2 ,message = "유저 gender는 0이상 2이하")
    @Schema(title = "유저 Gender", minimum = "0", maximum = "2" ,type = "Integer", description = "선택값")
    private int userGender;

    @Range(min=0, max=150 ,message = "유저 age는 0이상 150이하")
    @Schema(title = "유저 Age", minimum = "0", maximum = "150" ,type = "Integer", description = "선택값")
    private int userAge;

}

