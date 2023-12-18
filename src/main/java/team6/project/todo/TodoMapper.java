package team6.project.todo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team6.project.todo.model.PatchTodoDto;
import team6.project.todo.model.TodoDeleteDto;
import team6.project.todo.model.proc.InsertTodoDto;
import team6.project.todo.model.proc.TodoSelectTmpResult;
import team6.project.todo.model.proc.RepeatInsertDto;
import team6.project.todo.model.ref.TodoSelectVoRef;

import java.util.List;

@Mapper
public interface TodoMapper {

    Integer insTodo(InsertTodoDto dto);

    Integer insRepeat(RepeatInsertDto dto);

    Integer isRepeat(@Param("iuser") Integer iuser, @Param("itodo") Integer itodo);

    int getTodoListCount(Integer iuser);
    
    List<TodoSelectTmpResult> selectTodo(TodoSelectVoRef dto);

    int patchTodoAndRepeatIfExists(PatchTodoDto dto);

    int deleteTodo(TodoDeleteDto dto);

    Integer deleteTodoRepeat(@Param("iuser") Integer iuser, @Param("itodo") Integer itodo);


}
