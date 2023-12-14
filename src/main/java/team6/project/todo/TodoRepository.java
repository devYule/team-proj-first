package team6.project.todo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import team6.project.todo.model.PatchTodoDto;
import team6.project.todo.model.RepeatInsertDto;
import team6.project.todo.model.TodoDeleteDto;
import team6.project.todo.model.proc.*;
import team6.project.todo.model.ref.TodoSelectDtoRef;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TodoRepository {

    private final TodoMapper mapper;


    public Integer getListCountById(Integer iuser) {
        return mapper.getTodoListCount(iuser);
    }

    public Integer saveTodo(InsertTodoDto dto) {
        return mapper.insTodo(dto);
    }


    public Integer saveRepeat(RepeatInsertDto dto) {
        return mapper.insRepeat(dto);
    }

    public List<TodoSelectTmpResult> findTodoAndRepeatBy(TodoSelectDtoRef dto) {
        return mapper.selectTodo(dto);
    }



    public boolean checkIsRepeat(Integer iuser, Integer itodo) {
        boolean b = mapper.isRepeat(iuser, itodo) == 0;
        log.debug("repository's b = {}", b);
        return b;
    }



    public Integer deleteRepeat(Integer iuser, Integer itodo) {
        return mapper.deleteTodoRepeat(iuser, itodo);
    }

    public Integer deleteTodo(TodoDeleteDto dto) {
        return mapper.deleteTodo(dto);
    }

    public Integer updateTodoAndRepeatIfExists(PatchTodoDto dto) {

        return mapper.patchTodoAndRepeatIfExists(dto) == 0 ? 0 : 1;

    }



}
