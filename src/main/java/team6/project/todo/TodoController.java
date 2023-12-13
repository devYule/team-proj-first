package team6.project.todo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.project.common.Const;
import team6.project.common.ResVo;
import team6.project.common.exception.NotEnoughInformationException;
import team6.project.todo.model.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")

public class TodoController {

    private final TodoService service;


    @PostMapping
    public ResVo postTodo(@Validated @RequestBody TodoRegDto dto) {
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
        log.info("patchTodo's dto = {}", dto);
        checkObjectIsNull(dto.getTodoContent(), dto.getStartDate(), dto.getEndDate(), dto.getStartTime(), dto.getEndTime());
        return service.patchTodo(dto);
    }

    @DeleteMapping("/{iuser}/{itodo}")
    public ResVo deleteTodo(TodoDeleteDto dto) {
        log.info("deleteTodo's dto = {}", dto);
        return service.deleteTodo(dto);

    }

    private void checkObjectIsNull(Object... objs) {
        for (Object obj : objs) {
            if (obj != null) {
                return;
            }
        }
        throw new NotEnoughInformationException(Const.NOT_ENOUGH_INFO_EXCEPTION_MESSAGE);
    }

}
