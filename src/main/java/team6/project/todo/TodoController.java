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
    @Operation(summary = "투두 등록", description = "투두 정보 등록<br><br>반복 설정시 startDate, endDate 는 동일해야함 & startTime 은 endTime 이전이어야 " +
            "함.")
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
    @Operation(summary = "투두 조회", description = "투두 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public TodoSelectVo getTodo(@Validated TodoSelectDto dto) {
        log.info("getTodo's dto = {}", dto);
        TodoSelectTransVo transDto = new TodoSelectTransVo(dto.getIuser(), dto.getY(), dto.getM(), dto.getD());
        return service.getTodo(transDto);

    }

    @PatchMapping
    @Operation(summary = "투두 정보 수정", description = "투두 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResVo patchTodo(@Validated @RequestBody PatchTodoDto dto) {
        log.info("patchTodo's dto = {}", dto);

        return service.patchTodo(dto);

    }

    @DeleteMapping("/{iuser}/{itodo}") // query string - rp(delOnlyRepeat)
    @Operation(summary = "투두 삭제", description = "투두 정보 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResVo deleteTodo(TodoDeleteDto dto,
                            @RequestParam(required = false, value = "rp") @Schema(title = "반복 여부",
                                    description = "반복일시 쿼리스트링으로 rp=1 요청, 반복 아닐경우 쿼리파라미터 자체를 기재 x", hidden = true) Integer delOnlyRepeat) {
        if (delOnlyRepeat != null && delOnlyRepeat != 1) {
            throw new BadInformationException(BAD_INFO_EX_MESSAGE);
        }
        log.info("deleteTodo's dto = {}", dto);

        return service.deleteTodo(dto, delOnlyRepeat);
    }

}
