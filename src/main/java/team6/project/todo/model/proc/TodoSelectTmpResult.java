package team6.project.todo.model.proc;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TodoSelectTmpResult {
    private Integer itodo;
    private String todoContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate repeatEndDate;
    private String repeatType;
    private Integer repeatNum;
}
