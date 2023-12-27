package team6.project.todo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.project.common.ResVo;
import team6.project.common.exception.BadInformationException;
import team6.project.todo.model.*;
import team6.project.todo.model.TodoSelectDto;

import java.util.List;

import static team6.project.common.Const.BAD_INFO_EX_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")
@Tag(name = "투두", description = "투두 등록, 조회, 수정, 삭제")
public class TodoController {

    private final TodoServiceRef service;

    @PostMapping
    @Operation(summary = "투두 등록", description = "<strong>투두 정보 등록</strong><br>startDate, endDate 는 동일해야함<br><br>" +
            "iuser: 유저 PK 값 (필수값)<br>" +
            "todoContent: 유저가 작성한 투두의 제목(내용) (필수값)<br>" +
            "startDate: 일정 시작일 (endDate 와 동일한 값) (필수값)<br>" +
            "endDate: 일정 종료일 (startDate 와 동일한 값) (필수값)" +
            "<br><br><br>" +
            "<strong>response</strong><br><br>" +
            "성공시<br>" +
            "result: 등록된 투두의 itodo 값 리턴<br><br>" +
            "실패시<br>" +
            "errorMessage 와 errorCode 리턴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResVo postTodo(@Validated @RequestBody TodoRegDto dto) {
        log.debug("postTodo's dto = {}", dto);

        return service.regTodo(dto);

    }


    @GetMapping("/{iuser}")
    @Operation(summary = "투두 조회", description = "<strong>투두 정보 조회</strong><br><br>" +
            "iuser: 유저 PK 값 (필수값)<br>" +
            "y: 선택된 날짜의 년 정보, 1900 이상 9999 이하 허용(필수값)<br>" +
            "m: 선택된 날짜의 월 정보, 1 이상 12 이하 허용 (필수값)<br>" +
            "d: 선택된 날짜의 일 정보, 1 이상 31 이하 허용 (필수값)" +
            "<br><br><br>" +
            "<strong>response</strong><br><br>" +
            "성공시<br>" +
            "emotionGrade: 선택된 날짜의 감정 (숫자)<br>" +
            "emotionTag: 선택된 날짜의 이모션 태그<br>" +
            "todos: 조회된 투두의 PK 와 유저가 작성한 투두의 제목(내용) (배열로 리턴)<br><br>" +
            "실패시<br>" +
            "errorMessage 와 errorCode 리턴" +
            "<br><br><br>" +
            "<strong>참고</strong><br>" +
            "/api/todo/7?y=2023&m=12&d=21 으로 요청시 데이터가 존재함이 보장됨.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public TodoSelectVo getTodo(@PathVariable Integer iuser, @Validated TodoSelectDto dto) {
        checkPathVariable(iuser);
        log.debug("getTodo's dto = {}", dto);
        dto.setIuser(iuser);
        TodoSelectTransVo transDto = new TodoSelectTransVo(dto.getIuser(), dto.getY(), dto.getM(), dto.getD());
        return service.getTodo(transDto);

    }

    @PatchMapping
    @Operation(summary = "투두 정보 수정", description = "<strong>투두 정보 수정</strong><br><br>" +
            "iuser: 유저 PK 값 (필수값)<br>" +
            "itodo: 투두 PK 값 (필수값)<br>" +
            "todoContent: 유저가 수정 하려는 투두의 제목(내용)" +
            "<br><br><br>" +
            "<strong>response</strong><br><br>" +
            "성공시<br>" +
            "result: 1 리턴<br><br>" +
            "실패시<br>" +
            "errorMessage 와 errorCode 리턴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResVo patchTodo(@Validated @RequestBody PatchTodoDto dto) {
        log.debug("patchTodo's dto = {}", dto);

        return service.patchTodo(dto);

    }

    @DeleteMapping("/{iuser}/{itodo}") // query string - rp(delOnlyRepeat)
    @Operation(summary = "투두 삭제", description = "<strong>투두 정보 삭제</strong><br><br>" +
            "iuser: 유저 PK 값 (필수값)<br>" +
            "itodo: 투두 PK 값 (필수값)" +
            "<br><br><br>" +
            "<strong>response</strong><br><br>" +
            "성공시<br>" +
            "result: 1 리턴<br><br>" +
            "실패시<br>" +
            "errorMessage 와 errorCode 리턴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResVo deleteTodo(@PathVariable Integer iuser, @PathVariable Integer itodo,
                            @RequestParam(required = false, value = "rp") @Schema(title = "반복 여부",
                                    description = "반복일시 쿼리스트링으로 rp=1 요청, 반복 아닐경우 쿼리파라미터 자체를 기재 x", hidden = false) Integer delOnlyRepeat) {
        checkPathVariable(iuser, itodo);
        if (delOnlyRepeat != null && delOnlyRepeat != 1) {
            throw new BadInformationException(BAD_INFO_EX_MESSAGE);
        }
        TodoDeleteDto dto = new TodoDeleteDto(iuser, itodo);
        log.debug("deleteTodo's dto = {}", dto);

        return service.deleteTodo(dto, delOnlyRepeat);
    }
    private void checkPathVariable(Integer... pks) {
        for (Integer pk : pks) {
            if (pk == null || pk < 1) {
                throw new BadInformationException(BAD_INFO_EX_MESSAGE);
            }
        }
    }
}
