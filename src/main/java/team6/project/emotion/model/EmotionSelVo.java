package team6.project.emotion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmotionSelVo {
    @JsonProperty("emotion_grade")
    private int emotionGrade;
    @JsonProperty("emotion_tag")
    private int emotionTag;
    @JsonProperty("day")
    private String emotionCreatedAt;
    @JsonProperty("has_todo")
    private int hasTodo;
}
