package team6.project.sub.emotionhyunmin.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SubEmotionSelVo {
    private int day;
    private Integer emotionGrade;
    private Integer emotionTag;
    private int hasTodo;

}
