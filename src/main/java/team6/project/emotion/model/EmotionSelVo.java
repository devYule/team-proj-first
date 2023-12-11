package team6.project.emotion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmotionSelVo {
    private int emotionGrade;
    private int emotionTag;
    private String emotionCreatedAt;
    private int hasTodo;
}
