package team6.project.todo;


import team6.project.common.ResVo;

import team6.project.todo.model.*;
import team6.project.todo.model.TodoSelectTransVo;

import java.util.List;


public interface TodoServiceRef {
    ResVo regTodo(TodoRegDto dto);//
    TodoSelectVo getTodo(TodoSelectTransVo dto);
    ResVo patchTodo(PatchTodoDto dto);
    ResVo deleteTodo(TodoDeleteDto dto, Integer delOnlyRepeat);

}
