package team6.project.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "유저 조회",title = "유저 조회")
public class UserSelVo {

    @Schema(title = "유저 NickName", minimum = "1", maximum = "10", type = "String", description = "필수값")
    private String userNickName;

    @Schema(title="유저 Gender", minimum = "0", maximum = "2", type = "Integer", description = "선택값")
    private int userGender;

    @Schema(title="유저 Age", minimum = "0", maximum = "150", type = "Integer", description = "선택값")
    private int userAge;

    @Schema(title = "유저 업데이트 시간", type = "시간", description = "유저 업데이트 시간")
    private String createdAt;
}
