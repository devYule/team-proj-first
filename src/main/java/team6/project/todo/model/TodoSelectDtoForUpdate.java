package team6.project.todo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team6.project.todo.model.ref.TodoSelectDtoRef;

@Getter
@Setter
@AllArgsConstructor
public class TodoSelectDtoForUpdate extends TodoSelectDtoRef {
    private Integer iuser;
    private Integer itodo;
}
