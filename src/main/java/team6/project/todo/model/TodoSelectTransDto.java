package team6.project.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import team6.project.todo.model.ref.TodoSelectDtoRef;

import java.time.LocalDate;


public class TodoSelectTransDto extends TodoSelectDtoRef {

    @Schema(title = "유저 PK", minimum = "1", type = "Integer", description = "필수값")
    private Integer iuser;
    private LocalDate selectedDate;

}
