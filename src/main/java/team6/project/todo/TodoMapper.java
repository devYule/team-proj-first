package team6.project.todo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team6.project.todo.model.PatchTodoDto;
import team6.project.todo.model.TodoDeleteDto;
import team6.project.todo.model.TodoSelectDto;
import team6.project.todo.model.TodoSelectVo;
import team6.project.todo.model.proc.*;

import java.util.List;

@Mapper
public interface TodoMapper {

    void insTodo(InsertTodoDto dto);

    void insRepeat(InsRepeatInfoDto dto);

    Integer isRepeat(@Param("iuser") Integer iuser, @Param("itodo") Integer itodo);

    int getTodoListCount(Integer iuser);
    List<TodoSelectTmpResult> selectTodo(TodoSelectDto dto);

//    int patchTodoAndRepeat(PatchTodoDto patchTodoDto);

    int patchTodo(UpdateTodoDto dto);

    int patchRepeat(UpdateRepeatDto dto);

    int deleteTodo(TodoDeleteDto dto);
    void deleteTodoRepeat(@Param("iuser") Integer iuser, @Param("itodo") Integer itodo);


}
