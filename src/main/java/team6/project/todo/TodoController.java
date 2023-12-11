package team6.project.todo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.project.common.ResVo;
import team6.project.todo.model.PatchTodoDto;
import team6.project.todo.model.TodoRegDto;
import team6.project.todo.model.TodoSelectDto;
import team6.project.todo.model.TodoSelectVo;

import java.time.LocalDate;
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
//        return null;
    }
    // TODO - @GetMapping - 구현

    @GetMapping("/{iuser}")
    public List<TodoSelectVo> getTodo(@PathVariable Integer iuser,
                                      @RequestParam("y") Integer year,
                                      @RequestParam("m") Integer month,
                                      @RequestParam("d") Integer day){

        TodoSelectDto dto = new TodoSelectDto(iuser, LocalDate.of(year, month, day));
        return service.getTodo(dto);

    }

    // TODO - 구현
    @PatchMapping
    public ResVo patchTodo(@RequestBody PatchTodoDto dto) {
        log.debug("patchTodo's dto = {}", dto);
        return service.patchTodo(dto);
    }

    @DeleteMapping("/{iuser}/{itodo}")
    public ResVo deleteTodo(@PathVariable Integer iuser, @PathVariable Integer itodo){
       return service.deleteTodo(iuser, itodo);
    }

}
