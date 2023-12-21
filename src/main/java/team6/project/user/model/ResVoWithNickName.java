package team6.project.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team6.project.common.ResVo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResVoWithNickName {
    @Schema(title = "유저 정보", description = "성공한 유저Pk")
    private int iuser;

    @Schema(title = "요청 결과", description = "요청 결과")
    private int result;

    @Schema(title = "유저 닉네임", defaultValue = "유저닉네임")
    private String userNickName;


}