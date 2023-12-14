package team6.project.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserSelVo {

    private String userNickName;
    private int userGender;
    private int userAge;
    private String createdAt;
}
