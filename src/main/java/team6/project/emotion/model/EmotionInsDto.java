package team6.project.emotion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmotionInsDto {
    private int iuser;

    private int emoGrade;

    private int emoTag;
}
