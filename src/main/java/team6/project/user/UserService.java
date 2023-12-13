package team6.project.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team6.project.common.ResVo;
import team6.project.common.exception.MyMethodArgumentNotValidException;
import team6.project.user.model.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;

    public ResVo postUser(UserInsDto dto) {

        if (mapper.checkUser(dto) != null) {
            throw new MyMethodArgumentNotValidException("이미 생성된 닉네임");
        }
        mapper.insUser(dto);
        ResVo vo = new ResVo(dto.getIuser());

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
