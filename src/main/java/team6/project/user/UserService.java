package team6.project.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team6.project.common.ResVo;
import team6.project.user.model.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;

    public ResVo postUser(UserInsDto dto) {
        UserInsProcDto dto1 = new UserInsProcDto(dto);
        if (mapper.checkUser(dto) != null) {
            return new ResVo(0);
        }
        mapper.insUser(dto1);
        ResVo vo = new ResVo(dto1.getIuser());

        return vo;
    }

    public UserSelVo getUser(int iuser) {
        return mapper.selUser(iuser);
    }

    public ResVo upUser(UserUpDto dto) {
        return new ResVo(mapper.upUser(dto));
    }

    public ResVo deleteUser(int iuser) {

        List<Integer> itodos = mapper.selItodo(iuser);

        if(itodos!=null && !itodos.isEmpty()){
            mapper.delToDoRepeat(itodos);
            mapper.delToDo(iuser);

        }
        mapper.delToEmo(iuser);

        return new ResVo(mapper.delUser(iuser));
    }
}
