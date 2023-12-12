package team6.project.emotion.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmotionSelDto {

    private int iuser;

    private int year;

    private int month;
}
