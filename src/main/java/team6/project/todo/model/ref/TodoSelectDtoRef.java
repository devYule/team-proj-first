package team6.project.todo.model.ref;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TodoSelectDtoRef {
    private Integer itodo;
    private Integer iuser;
    private LocalDate selectedDate;
}
