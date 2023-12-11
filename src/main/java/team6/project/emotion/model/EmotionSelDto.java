package team6.project.emotion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmotionSelDto {
    private int iuser;
    @JsonProperty("y")
    private int year;
    @JsonProperty("m")
    private int month;
}
