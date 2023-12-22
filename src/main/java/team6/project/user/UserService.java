package team6.project.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team6.project.common.Const;
import team6.project.common.ResVo;
import team6.project.common.exception.BadInformationException;
import team6.project.common.exception.MyMethodArgumentNotValidException;
import team6.project.common.exception.NoSuchException;
import team6.project.user.model.ResVoWithNickName;
import team6.project.user.model.UserInsDto;
import team6.project.user.model.UserSelVo;
import team6.project.user.model.UserUpDto;

import static team6.project.common.Const.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;

    public ResVoWithNickName postUser(UserInsDto dto) {
//        ResVoWithNickName name = new ResVoWithNickName();



//        if (mapper.checkUser(dto) != null) {
//            throw new MyMethodArgumentNotValidException(NICK_NAME_IS_EXISTS);
//        }
        ResVoWithNickName ifIsExists = mapper.checkUser(dto);

        if (ifIsExists != null && ifIsExists.getIuser() > 0 && ifIsExists.getUserNickName() != null) {
            return ifIsExists;
        }
        //쿼리에서 받아온 유저정보를 저장해서  iuser, result, userNickName 보내줘야한다.

        if(mapper.insUser(dto) == 1){
            return new ResVoWithNickName(dto.getIuser(), 1, dto.getUserNickName());
        }

        throw new RuntimeException(RUNTIME_EX_MESSAGE);

    }

    public UserSelVo getUser(int iuser) {
        UserSelVo userSelVo = mapper.selUser(iuser);
        if (userSelVo == null) {
            throw new MyMethodArgumentNotValidException(NO_SUCH_DATA_EX_MESSAGE);
        }
        return userSelVo;
    }

    public ResVo patchUser(UserUpDto dto) {
        int result = mapper.upUser(dto);
        if (result == 0) {
            throw new BadInformationException(BAD_INFO_EX_MESSAGE);
        }

        return new ResVo(result);

    }

    public ResVo deleteUser(int iuser) {

            mapper.delRepeat(iuser);
            mapper.delTodoEmo(iuser);
            if(mapper.delUser(iuser)==0){
                throw new NoSuchException(NO_SUCH_DATA_EX_MESSAGE);
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
