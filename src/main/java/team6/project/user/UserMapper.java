package team6.project.user;


import org.apache.ibatis.annotations.Mapper;

import team6.project.user.model.UserInsProcDto;
import team6.project.user.model.UserSelVo;
import team6.project.user.model.UserUpDto;

@Mapper
public interface UserMapper {
    int insUser(UserInsProcDto dto);
    UserSelVo selUser(int iuser);
    int upUser(UserUpDto dto);
    int delUser(int iuser);
}
