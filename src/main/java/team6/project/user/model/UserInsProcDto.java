package team6.project.user.model;


import lombok.Data;

@Data
public class UserInsProcDto {
    private int iuser;
    private String userNickName;
    private int userGender;
    private int userAge;

    public UserInsProcDto(UserInsDto dto){
        this.userNickName=dto.getUserNickName();
        this.userAge=dto.getUserAge();
        this.userGender=dto.getUserGender();
    }

}
