package team6.project.user;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import team6.project.common.Const;
import team6.project.common.ResVo;
import team6.project.common.exception.BadUserNickNameException;
import team6.project.common.exception.MyMethodArgumentNotValidException;
import team6.project.user.model.UserInsDto;
import team6.project.user.model.UserSelVo;
import team6.project.user.model.UserUpDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;

    @PostMapping
    public ResVo postUser(@RequestBody UserInsDto dto) {
        // TODO 예외처리
        if (dto.getUserNickName() == null || dto.getUserNickName().equals("") || dto.getUserNickName().equals(" ")) {
            throw new MyMethodArgumentNotValidException("수정할 정보가 제공되지 않음");
        }
        return service.postUser(dto);
    }

    @GetMapping("/{iuser}")
    public UserSelVo getUser(@PathVariable int iuser) {
        return service.getUser(iuser);
    }

    @PatchMapping
    public ResVo patchUser(@RequestBody UserUpDto dto) {
        checkIuser(dto.getIuser());

        if (dto.getUserAge() == null && dto.getUserNickName() == null && dto.getUserGender() == null) {
            //todo 예외처리
        }
        if (dto.getUserNickName() != null) {
            if (dto.getUserNickName().isEmpty() || dto.getUserNickName().length() > 10) {
                //todo 예외처리
                // todo isEmpty 가 null 까지 포함되는지 여부 체크
            }
        }
        if (!(dto.getUserGender() >= 0 && dto.getUserGender() <= 3)) {
            //todo 예외처리
        }
        if (!(dto.getUserAge() >= 0 && dto.getUserAge() <= 150)) {
            //todo 예외처리
        }

        return service.upUser(dto);
    }

    @DeleteMapping("/{iuser}")
    public ResVo deleteUser(@PathVariable int iuser) {
        checkIuser(iuser);

        return service.deleteUser(iuser);
    }
    //요청 = body , get,delete =nobody


    private void checkIuser(int iuser) {
        if (iuser == 0) {
            //todo 예외처리
        }
    }

}
