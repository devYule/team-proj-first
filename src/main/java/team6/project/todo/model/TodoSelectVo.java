package team6.project.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "투두 조회 결과", title = "투두 조회 결과")
public class TodoSelectVo {
    @Schema(title = "조회된 투두 PK", minimum = "1", type = "Integer", description = "조회된 투두의 PK")
    private Integer itodo;
    @Schema(title = "조회된 투두 제목", maximum = "100", type = "String", description = "조회된 투두의 제목")
    private String todoContent;
    @Schema(title = "일정 시작일", type = "날짜", description = "일정의 시작 날짜 (시간 제외)", format = "yyyy-MM-dd")
    private LocalDate startDate;
    @Schema(title = "일정 종료일", type = "날짜", description = "일정의 종료 날짜 (시간 제외)", format = "yyyy-MM-dd")
    private LocalDate endDate;
    @Schema(title = "일정 시작 시간", type = "시간", description = "일정의 시작 시간 (날짜 제외)")
    private LocalTime startTime;
    @Schema(title = "일정 종료 시간", type = "시간", description = "일정의 종료 시간 (날짜 제외)")
    private LocalTime endTime;

}
