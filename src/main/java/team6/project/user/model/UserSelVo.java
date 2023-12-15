package team6.project.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "유저 조회",title = "유저 조회")
public class UserSelVo {

    @Schema(title = "유저 닉네임", minimum = "1", maximum = "10", type = "String", description = "조회된 유저의 닉네임")
    private String userNickName;

    @Schema(title="유저 성별", minimum = "0", maximum = "2", type = "Integer", description = "조회된 유저의 성별 (미선택했던 유저일경우 0)")
    private int userGender;

    @Schema(title="유저 나이", minimum = "0", maximum = "150", type = "Integer", description = "조회된 유저의 나이 (미선택했던 유저일경우 0)")
    private int userAge;

    @Schema(title = "유저 등록 시간", type = "시간", description = "유저 등록 시간")
    private String createdAt;
}
