package team6.project.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team6.project.common.ResVo;
import team6.project.user.model.UserInsProcDto;
import team6.project.user.model.UserInsDto;
import team6.project.user.model.UserSelVo;
import team6.project.user.model.UserUpDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;

    public ResVo postUser(UserInsDto dto){
        UserInsProcDto dto1= new UserInsProcDto(dto);
        mapper.insUser(dto1);
        ResVo vo = new ResVo(dto1.getIuser());

        return vo;
    }
    public UserSelVo getUser(int iuser){
        return mapper.selUser(iuser);
    }

    public ResVo upUser(UserUpDto dto){
        return new ResVo(mapper.upUser(dto));
    }
    public ResVo deleteUser(int iuser){
        return new ResVo(mapper.delUser(iuser));
    }
}
