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
        UserSelVo userSelVo = mapper.selUser(iuser);
        if (userSelVo == null) {
            throw new MyMethodArgumentNotValidException("조회된 유저 정보가 없음");
        }
        return userSelVo;
    }

    public ResVo upUser(UserUpDto dto) {
        return new ResVo(mapper.upUser(dto));
    }

    public ResVo deleteUser(int iuser) {

        List<Integer> itodos = mapper.selItodo(iuser);


        if (itodos != null && !itodos.isEmpty()) {
            mapper.delToDoRepeat(itodos);
            mapper.delToDo(iuser);

        }
        mapper.delToEmo(iuser);

        int effectedRow = mapper.delUser(iuser);
        if (effectedRow == 0) {
            throw new MyMethodArgumentNotValidException("해당 유저가 존재하지 않거나 삭제되지 않음");
        }


        return new ResVo(effectedRow);
    }
}
