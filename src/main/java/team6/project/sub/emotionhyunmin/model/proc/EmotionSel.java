package team6.project.sub.emotionhyunmin.model.proc;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmotionSel {
    private LocalDate createdAt;
    private Integer emotionGrade;
    private Integer emotionTag;
}
