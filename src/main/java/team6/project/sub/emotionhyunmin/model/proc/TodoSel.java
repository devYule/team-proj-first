package team6.project.sub.emotionhyunmin.model.proc;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TodoSel {
    // 부가 자료
    // start - 해당 월의 마지막날 이전
    // end - 해당 월의 첫날 이후
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate repeatEndDate;
    private String repeatType;
    private Integer repeatNum;
}
