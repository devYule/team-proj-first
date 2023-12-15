package team6.project.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "유저 수정", title = "유저 수정")
public class UserUpDto {


    @Schema(title = "유저 PK", minimum = "1", type = "Integer", description = "필수값", defaultValue = "유저pk")
    private int iuser;


    @Schema(title = "유저 닉네임", minimum = "1", maximum = "10" ,type = "String", description = "필수값", defaultValue = "닉네임")
    private String userNickName;


    @Schema(title = "유저 성별", minimum = "0", maximum = "2" ,type = "Integer", defaultValue = "성별")
    private Integer userGender;


    @Schema(title = "유저 나이", minimum = "0", maximum = "150" ,type = "Integer", defaultValue = "나이")
    private Integer userAge;
}
