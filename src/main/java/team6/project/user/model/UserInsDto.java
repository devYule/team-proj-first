package team6.project.user.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "유저 등록", title = "유저 등록")
public class UserInsDto {
    @JsonIgnore
    private int iuser;

    @Schema(title = "유저 닉네임", minimum = "1", maximum = "10", type = "String", description = "필수값")
    private String userNickName;


    @Schema(title = "유저 Gender", minimum = "0", maximum = "2" ,type = "Integer")
    private int userGender;


    @Schema(title = "유저 나이", minimum = "0", maximum = "150" ,type = "Integer")
    private int userAge;

}

