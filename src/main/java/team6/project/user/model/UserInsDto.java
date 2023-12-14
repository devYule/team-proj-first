package team6.project.user.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserInsDto {
    @JsonIgnore
    private int iuser;
    private String userNickName;
    private int userGender;
    private int userAge;

}

