package team6.project.sub.EmotionHeasun.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class EmotionInsDto {
    @JsonIgnore
    private int iuser;
    private int emoGrade;
    private String emoTag;
}
