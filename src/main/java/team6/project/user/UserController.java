package team6.project.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import team6.project.common.ResVo;
import team6.project.common.exception.MyMethodArgumentNotValidException;
import team6.project.user.model.UserInsDto;
import team6.project.user.model.UserSelVo;
import team6.project.user.model.UserUpDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;

    @PostMapping
    public ResVo postUser(@RequestBody UserInsDto dto) {
        log.info("{}", dto);
        if (dto.getUserNickName() == null || dto.getUserNickName().isBlank()) {
            throw new MyMethodArgumentNotValidException("닉네임은 필수값");
        }
        checkAllRange(dto.getUserNickName(), dto.getUserGender(), dto.getUserAge());
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
            throw new MyMethodArgumentNotValidException("수정할 정보가 제공되지 않음");
        }

        checkAllRange(dto.getUserNickName(), dto.getUserGender(), dto.getUserAge());




        return service.patchUser(dto);
    }

    @DeleteMapping("/{iuser}")
    public ResVo deleteUser(@PathVariable int iuser) {
        checkIuser(iuser);

        return service.deleteUser(iuser);
    }
    //요청 = body , get,delete =nobody


    private void checkIuser(int iuser) {
        if (iuser<1) {
            throw new MyMethodArgumentNotValidException("iuser 필수");
        }

    }


    private void checkAllRange(String name, Integer gender, Integer age) {
        StringBuilder sb = new StringBuilder();
        if (name != null) {
            if (name.isEmpty() || name.length() > 10) {
                // 추가해야할 메시지:  "닉네임은 1자 이상 10자이하, "
                sb.append("닉네임은 1자 이상 10자 이하, ");
            }
        }
        if (name.equals(" ")) {
            sb.append("닉네임은 공백불가, ");
        }
        if (!(gender >= 0 && gender <= 3)) {
            // 추가해야할 메시지:  "성별은 0이상 3이하 선택, "
            sb.append("성별은 0이상 3이하 선택, ");
        }
        if (!(age >= 0 && age <= 150)) {
            // 추가해야할 메시지:  "나이는 0~150까지만 입력, "
            sb.append("나이는 0~150까지만 입력, ");
        }

        String errorMessage = sb.toString();
        if (StringUtils.isNotEmpty(errorMessage)) {
            throw new MyMethodArgumentNotValidException(errorMessage.substring(0, errorMessage.length() - 2));
        }
    }

}
