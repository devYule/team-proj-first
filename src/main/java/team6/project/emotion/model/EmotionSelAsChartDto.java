package team6.project.emotion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class EmotionSelAsChartDto {
    private int iuser;
    private String startWeek;
    private String today;
}
