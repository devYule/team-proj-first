package team6.project.todo.model.ref;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoInfosRef {
    // _Todo Info
    private String todoContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    // Repeat Info
    private LocalDate repeatEndDate;
    private String repeatType;
    private Integer repeatNum;
}
