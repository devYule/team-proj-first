package team6.project.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserUpDto {

    private int iuser;
    private String userNickName;
    private Integer userGender;
    private Integer userAge;
}
