package team6.project.user.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserInsDto {
    @JsonProperty("user_nick_name")
    private String userNickName;
    @JsonProperty("user_gender")
    private int userGender;
    @JsonProperty("user_age")
    private int userAge;

}
