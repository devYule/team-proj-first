package team6.project.emotion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EmotionSelAsChartVo {

    private List<EmotionSel> emoChart;
    private int good=0;
    private int normal=0;
    private int bad=0;
}
