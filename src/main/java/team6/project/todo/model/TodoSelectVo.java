package team6.project.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoSelectVo {

    private Integer itodo;
    @JsonProperty("todo_content")
    private String todoContent;

}
