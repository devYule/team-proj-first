package team6.project.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserUpDto {

    private int iuser;
    @JsonProperty("user_nick_name")
    private String userNickName;
    @JsonProperty("user_gender")
    private Integer userGender;
    @JsonProperty("user_age")
    private Integer userAge;
}
