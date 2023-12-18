package team6.project.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoSelectVo {
    @Schema(title = "감정",  description = "선택된 날짜의 감정")
    private Integer emotion;
    @Schema(title = "이모션 태그",  description = "선택된 날짜의 이모션 태그")
    private String emotionTag;
    @Schema(title = "조회된 투두의 PK & 내용",  description = "조회된 투두의 PK 와 투두의 내용")
    private List<TodoInfo> todos;

}
