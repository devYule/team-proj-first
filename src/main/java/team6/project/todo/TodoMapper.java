package team6.project.todo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team6.project.todo.model.PatchTodoDto;
import team6.project.todo.model.TodoSelectDto;
import team6.project.todo.model.TodoSelectVo;
import team6.project.todo.model.proc.InsRepeatInfoDto;
import team6.project.todo.model.proc.InsertTodoDto;
import team6.project.todo.model.proc.TodoSelectTmpResult;
import team6.project.todo.model.proc.UpdateTodoDto;

import java.util.List;

@Mapper
public interface TodoMapper {

    void insTodo(InsertTodoDto dto);

    void insRepeat(InsRepeatInfoDto dto);

    Integer isRepeat(@Param("iuser") Integer iuser, @Param("itodo") Integer itodo);

    List<TodoSelectTmpResult> selectTodo(TodoSelectDto dto);

    int patchTodoAndRepeat(PatchTodoDto patchTodoDto);

    int patchTodo(UpdateTodoDto dto);


    int deleteTodo(@Param("iuser") Integer iuser, @Param("itodo") Integer itodo);
    void deleteTodoRepeat(@Param("iuser") Integer iuser, @Param("itodo") Integer itodo);


}
