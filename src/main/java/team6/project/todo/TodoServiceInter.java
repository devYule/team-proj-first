package team6.project.todo;


import team6.project.common.ResVo;

import team6.project.todo.model.*;
import team6.project.todo.model.TodoSelectTransDto;

import java.util.List;


public interface TodoServiceInter {
    ResVo regTodo(TodoRegDto dto);//
    List<TodoSelectVo> getTodo(TodoSelectTransDto dto);
    ResVo patchTodo(PatchTodoDto dto);
    ResVo deleteTodo(TodoDeleteDto dto, Integer delOnlyRepeat);

}
