package team6.project.sub.emotionhyunmin.model.proc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TodoSelProcDto {
    private Integer iuser;
    private LocalDate firstDayOfMonth;
    private LocalDate lastDayOfMonth;
}
