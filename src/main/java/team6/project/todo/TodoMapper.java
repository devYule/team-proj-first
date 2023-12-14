package team6.project.todo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team6.project.todo.model.*;
import team6.project.todo.model.proc.*;
import team6.project.todo.model.RepeatInsertDto;
import team6.project.todo.model.ref.TodoSelectDtoRef;

import java.util.List;

@Mapper
public interface TodoMapper {

    Integer insTodo(InsertTodoDto dto);

    Integer insRepeat(RepeatInsertDto dto);

    Integer isRepeat(@Param("iuser") Integer iuser, @Param("itodo") Integer itodo);

    int getTodoListCount(Integer iuser);
    List<TodoSelectTmpResult> selectTodo(TodoSelectDtoRef dto);



//    int patchTodoAndRepeat(PatchTodoDto patchTodoDto);

    int patchTodoAndRepeatIfExists(PatchTodoDto dto);

//    int patchRepeat(RepeatUpdateDto dto);

    int deleteTodo(TodoDeleteDto dto);
    Integer deleteTodoRepeat(@Param("iuser") Integer iuser, @Param("itodo") Integer itodo);


}
