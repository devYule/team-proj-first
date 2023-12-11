package team6.project.todo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TodoSelectDto {
    private Integer iuser;
    private LocalDate selectedDate;
}
