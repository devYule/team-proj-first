package team6.project.todo;

import team6.project.todo.model.PatchTodoDto;
import team6.project.todo.model.TodoDeleteDto;
import team6.project.todo.model.TodoSelectTransVo;
import team6.project.todo.model.proc.EmotionSelectTmpResult;
import team6.project.todo.model.proc.InsertTodoDto;
import team6.project.todo.model.proc.RepeatInsertDto;
import team6.project.todo.model.proc.TodoSelectTmpResult;
import team6.project.todo.model.ref.TodoSelectVoRef;

import java.util.List;

public interface TodoRepositoryRef {

    Integer getListCountById(Integer iuser);

    Integer saveTodo(InsertTodoDto dto);

    Integer saveRepeat(RepeatInsertDto dto);

    List<TodoSelectTmpResult> findTodoAndRepeatBy(TodoSelectVoRef dto);

    EmotionSelectTmpResult findEmotionAndEmotionTagBy(TodoSelectTransVo dto);

    boolean checkIsRepeat(Integer iuser, Integer itodo);

    Integer deleteRepeat(Integer iuser, Integer itodo);

    Integer deleteTodo(TodoDeleteDto dto);

    Integer updateTodoAndRepeatIfExists(PatchTodoDto dto);
}

