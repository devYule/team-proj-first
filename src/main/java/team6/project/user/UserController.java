package team6.project.user;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.*;
import team6.project.common.Const;
import team6.project.common.ResVo;
import team6.project.common.exception.BadUserNickNameException;
import team6.project.user.model.UserInsDto;
import team6.project.user.model.UserSelVo;
import team6.project.user.model.UserUpDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;

    @PostMapping
    public ResVo userIns(@RequestBody UserInsDto dto) {
        if (dto.getUserNickName()==null || dto.getUserNickName().equals("")||dto.getUserNickName().equals(" ")) {
            throw new BadUserNickNameException(Const.NICK_NAME_RANGE_EX_MESSAGE);
        }
        return service.postUser(dto);
    }

    @GetMapping("/{iuser}")
    public UserSelVo postUser(@PathVariable int iuser) {
        return service.getUser(iuser);
    }

    @PatchMapping
    public ResVo patchUser(@RequestBody UserUpDto dto) {
        return service.upUser(dto);
    }

    @DeleteMapping("/{iuser}")
    public ResVo deleteUser(@PathVariable int iuser) {

        return service.deleteUser(iuser);
    }
    //요청 = body , get,delete =nobody

}
