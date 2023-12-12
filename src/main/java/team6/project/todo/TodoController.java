package team6.project.todo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.project.common.ResVo;
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
        log.info("dto = {}", dto);

        dto.setDate();

        return null;

    }

    @PatchMapping
    public ResVo patchTodo(@RequestBody PatchTodoDto dto) {
        log.info("patchTodo's dto = {}", dto);
        return service.patchTodo(dto);
    }

    @DeleteMapping("/{iuser}/{itodo}")
    public ResVo deleteTodo(TodoDeleteDto dto) {
        log.info("dto = {}", dto);
        return service.deleteTodo(dto);

    }

}
