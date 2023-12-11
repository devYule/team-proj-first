package team6.project.emotion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmotionInsDto {
    private int iuser;
    @JsonProperty("iemotion_grade")
    private int emoGrade;
    @JsonProperty("iemotion_tag")
    private int emoTag;
}
