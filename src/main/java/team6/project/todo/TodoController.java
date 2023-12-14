package team6.project.todo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.project.common.Const;
import team6.project.common.ResVo;
import team6.project.common.exception.NotEnoughInformationException;
import team6.project.common.utils.CommonUtils;
import team6.project.todo.model.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")

public class TodoController {

    private final TodoServiceInter service;
    private final CommonUtils commonUtils;

    @PostMapping
    public ResVo postTodo(@Validated @RequestBody TodoRegDto dto) {
        /* TODO: 2023-12-14
            repeatType 이 "week" 또는 "month" 인지 여부 체크,
            "week" 라면 repeatNum 이 7이하, "month" 라면 31 이하 인지 체크.
            --by Hyunmin */
        log.debug("postTodo's dto = {}", dto);

        return service.regTodo(dto);
    }

    @GetMapping("/{iuser}")
    public List<TodoSelectVo> getTodo(@Validated TodoSelectDto dto) {
        log.info("getTodo's dto = {}", dto);

        dto.setDate();

        return service.getTodo(dto);

    }

    @PatchMapping
    public ResVo patchTodo(@Validated @RequestBody PatchTodoDto dto) {
            /* TODO: 2023-12-14
            repeatType 이 "week" 또는 "month" 인지 여부 체크,
            "week" 라면 repeatNum 이 7이하, "month" 라면 31 이하 인지 체크.
            --by Hyunmin */
        log.info("patchTodo's dto = {}", dto);
        commonUtils.checkObjectIsNullThrow(NotEnoughInformationException.class, Const.NOT_ENOUGH_INFO_EX_MESSAGE, dto.getTodoContent(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getStartTime(), dto.getEndTime());
        return service.patchTodo(dto);

    }

    @DeleteMapping("/{iuser}/{itodo}")
    public ResVo deleteTodo(TodoDeleteDto dto) {
        log.info("deleteTodo's dto = {}", dto);
        return service.deleteTodo(dto);

    }

    /* TODO: 2023-12-14
        _todo 에서 repeat 정보만 삭제하는 기능 구현
        --by Hyunmin */


}
