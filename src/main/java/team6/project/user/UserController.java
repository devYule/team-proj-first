package team6.project.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import team6.project.common.Const;
import team6.project.common.ResVo;
import team6.project.common.exception.BadInformationException;
import team6.project.common.exception.MyMethodArgumentNotValidException;
import team6.project.common.exception.NotEnoughInformationException;
import team6.project.user.model.ResVoWithNickName;
import team6.project.user.model.UserInsDto;
import team6.project.user.model.UserSelVo;
import team6.project.user.model.UserUpDto;

import static team6.project.common.Const.NOT_ENOUGH_INFO_EX_MESSAGE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "유저", description = "유저 등록, 조회, 수정, 삭제")
public class UserController {
    private final UserService service;

    @PostMapping
    @Operation(summary = "유저", description = "<strong>유저 정보 등록</strong><br><br>" +
            "userNickName: 유저 닉네임 (필수값)<br>" +
            "userGender: 유저 성별 (선택값)<br>" +
            "userAge: 유저 나이 (선택값)" +
            "<br><br><br>" +
            "<strong>response</strong><br><br>" +
            "성공시<br>" +
            "iuser: 새로 등록된 해당 유저의 PK값 반환<br>"+
            "result: 1<br> " +
            "userNickName: 설정한 닉네임<br>" +
            "hasEmotion: 오늘기준 이미 등록한 이모션이 있는지 여부 (0: 없음, 1: 있음)<br><br>" +
            "실패시<br>" +
            "errorCode 리턴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResVoWithNickName postUser(@RequestBody UserInsDto dto) {

        if(dto.getUserNickName() ==null){
            throw new BadInformationException(Const.NICK_NAME_RANGE_EX_MESSAGE);
        }
        checkAllRange(dto.getUserNickName(), dto.getUserGender(), dto.getUserAge());

        return service.postUser(dto);

    }

    @GetMapping("/{iuser}")
    @Operation(summary = "유저 조회", description = "유저 정보 조회", hidden = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public UserSelVo getUser(@PathVariable int iuser) {
        return service.getUser(iuser);
    }

    @PatchMapping
    @Operation(summary = "유저 정보 수정", description = "유저 정보 수정", hidden = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResVo patchUser(@RequestBody UserUpDto dto) {
        checkIuser(dto.getIuser());

        if (dto.getUserAge() == null && dto.getUserNickName() == null && dto.getUserGender() == null) {
            throw new NotEnoughInformationException(NOT_ENOUGH_INFO_EX_MESSAGE);
        }

        checkAllRange(dto.getUserNickName(), dto.getUserGender(), dto.getUserAge());




        return service.patchUser(dto);
    }

    @DeleteMapping("/{iuser}")
    @Operation(summary = "유저 삭제", description = "유저 정보 삭제", hidden = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
        if ( name.isBlank() || name.length() > 10) {
            throw new BadInformationException(Const.NICK_NAME_RANGE_EX_MESSAGE);
        }
        if (!(gender >= 0 && gender <= 3)) {
            throw new BadInformationException(Const.GENDER_RANGE_EX_MESSAGE);
        }
        if (!(age >= 0 && age <= 150)) {
            throw new BadInformationException(Const.AGE_RANGE_EX_MESSAGE);
        }


    }

}
