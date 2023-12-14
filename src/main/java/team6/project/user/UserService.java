package team6.project.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team6.project.common.ResVo;
import team6.project.common.exception.BadInformationException;
import team6.project.common.exception.MyMethodArgumentNotValidException;
import team6.project.common.exception.NoSuchException;
import team6.project.user.model.UserInsDto;
import team6.project.user.model.UserSelVo;
import team6.project.user.model.UserUpDto;

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

    public ResVo patchUser(UserUpDto dto) {
        int result = mapper.upUser(dto);
        if (result == 0) {
            throw new BadInformationException("존재 하지 않는 iuserPk");
        }

        return new ResVo(result);

    }

    public ResVo deleteUser(int iuser) {

            mapper.delRepeat(iuser);
            mapper.delTodoEmo(iuser);
            if(mapper.delUser(iuser)==0){
                throw new NoSuchException("존재하지 않는 pk");
            }
            mapper.delUser(iuser);//pk 없을시
            return new ResVo(iuser);
        }


//        List<Integer> itodos = mapper.selItodo(iuser);
//
//
//        if (itodos != null && !itodos.isEmpty()) {
//            mapper.delToDoRepeat(itodos);
//            mapper.delToDo(iuser);
//
//        }
//        mapper.delToEmo(iuser);
//
//        int effectedRow = mapper.delUser(iuser);
//        if (effectedRow == 0) {
//            throw new MyMethodArgumentNotValidException("해당 유저가 존재하지 않거나 삭제되지 않음");
//        }
//
//
//        return new ResVo(effectedRow);
//    }
//


}
