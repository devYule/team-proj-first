package team6.project.user;


import org.apache.ibatis.annotations.Mapper;

import team6.project.user.model.*;

import java.util.List;

@Mapper
public interface UserMapper {
    int insUser(UserInsDto dto);
    String checkUser(UserInsDto dto);
    UserSelVo selUser(int iuser);
    int upUser(UserUpDto dto);

    int delUser(int iuser);
    int delRepeat(int iuser);
    int delTodoEmo(int iuser);




//    List<Integer> selItodo(int itodo);

//    int delToDoRepeat(List<Integer> itodos);
//    int delToDo(int iuser);
//    int delToEmo(int iuser);


    //int insUser(UserInsDto dto)
    //String checkUser(UserInsDto
}
