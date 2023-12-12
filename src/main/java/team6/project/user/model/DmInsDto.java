package team6.project.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DmInsDto {
    @JsonIgnore
    private int idm; //auto increment가져오기위해
    private int loginedIuser;
    private int otherPersonIuser;
}
