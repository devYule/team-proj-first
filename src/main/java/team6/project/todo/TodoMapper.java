package team6.project.todo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team6.project.todo.model.PatchTodoDto;
import team6.project.todo.model.TodoDeleteDto;
import team6.project.todo.model.TodoSelectTransVo;
import team6.project.todo.model.proc.EmotionSelectTmpResult;
import team6.project.todo.model.proc.InsertTodoDto;
import team6.project.todo.model.proc.TodoSelectTmpResult;
import team6.project.todo.model.proc.RepeatInsertDto;
import team6.project.todo.model.ref.TodoSelectVoRef;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TodoMapper {

    Integer insTodo(InsertTodoDto dto);

    Integer insRepeat(RepeatInsertDto dto);


    int getTodoListCount(@Param("iuser") Integer iuser, @Param("startDate") LocalDate startDate);
    
    List<TodoSelectTmpResult> selectTodo(TodoSelectVoRef dto);

    EmotionSelectTmpResult selectEmotion(TodoSelectTransVo dto);

    int patchTodoAndRepeatIfExists(PatchTodoDto dto);

    int deleteTodo(TodoDeleteDto dto);

    Integer deleteTodoRepeat(TodoDeleteDto dto);

    RepeatInsertDto selectOnlyRepeat(Integer itodo);
}
