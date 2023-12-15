package team6.project.todo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.project.common.ResVo;
import team6.project.common.exception.BadInformationException;
import team6.project.common.utils.CommonUtils;
import team6.project.todo.model.*;

import java.util.List;

import static team6.project.common.Const.BAD_INFO_EX_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")
@Tag(name = "투두", description = "투두 등록, 조회, 수정, 삭제")
public class TodoController {

    /* TODO: 2023-12-15
        model 들 전부 정리
        --by Hyunmin */
    private final TodoServiceInter service;
    private final CommonUtils commonUtils;

    @PostMapping
    @Operation(summary = "투두 등록", description = "투두 정보 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResVo postTodo(@Validated @RequestBody TodoRegDto dto) {

        // repeatType, repeatNum 둘중 하나만 값 있는 경우 체크
        // repeatEndDate != null 인데, repeatType 이나 repeatNum 이 null 인 경우 체크
        // repeatType == week && repeatNum >= 1 && repeatNum <= 7 과,
        // repeatType == month && repeatNum >= 1 && repeatNum <= 31 여부 체크
        // 3개 모두 null 일경우 아무것도 하지않음 보장.
        commonUtils.checkRepeatNumWithRepeatType(dto.getRepeatEndDate(), dto.getRepeatType(), dto.getRepeatNum());

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
    public List<TodoSelectVo> getTodo(@Validated TodoSelectDto dto) {
        log.info("getTodo's dto = {}", dto);

        dto.setDate();

        return service.getTodo(dto);

    }

    @PatchMapping
    @Operation(summary = "투두 정보 수정", description = "투두 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResVo patchTodo(@Validated @RequestBody PatchTodoDto dto) {

        // repeatType, repeatNum 둘중 하나만 값 있는 경우 체크
        // repeatEndDate != null 인데, repeatType 이나 repeatNum 이 null 인 경우 체크
        // repeatType == week && repeatNum >= 1 && repeatNum <= 7 과,
        // repeatType == month && repeatNum >= 1 && repeatNum <= 31 여부 체크
        // 3개 모두 null 일경우 아무것도 하지않음 보장.
        commonUtils.checkRepeatNumWithRepeatType(dto.getRepeatEndDate(), dto.getRepeatType(), dto.getRepeatNum());

        log.info("patchTodo's dto = {}", dto);
//        commonUtils.checkObjectAnyNullThrow(NotEnoughInformationException.class, NOT_ENOUGH_INFO_EX_MESSAGE,
//                dto.getTodoContent(),
//                dto.getStartDate(),
//                dto.getEndDate(),
//                dto.getStartTime(), dto.getEndTime());

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
                            @RequestParam(required = false, value = "rp") Integer delOnlyRepeat) {
        if (delOnlyRepeat != null && delOnlyRepeat != 1) {
            throw new BadInformationException(BAD_INFO_EX_MESSAGE);
        }
        log.info("deleteTodo's dto = {}", dto);

        return service.deleteTodo(dto, delOnlyRepeat);
    }


}
