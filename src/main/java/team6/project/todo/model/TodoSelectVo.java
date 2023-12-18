package team6.project.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(name = "투두 조회 결과", title = "투두 조회 결과")
public class TodoSelectVo {
    @Schema(title = "조회된 투두 PK", minimum = "1", type = "Integer", description = "조회된 투두의 PK", defaultValue = "1")
    private Integer itodo;
    @Schema(title = "조회된 투두 제목", maximum = "100", type = "String", description = "조회된 투두의 제목", defaultValue = "todoContent")
    private String todoContent;
    @Schema(title = "일정 시작일", type = "String", description = "일정의 시작 날짜 (시간 제외)", format = "yyyy-MM-dd", defaultValue = "2023-12-12")
    private LocalDate startDate;
    @Schema(title = "일정 종료일", type = "String", description = "일정의 종료 날짜 (시간 제외)", format = "yyyy-MM-dd", defaultValue = "2023-12-13")
    private LocalDate endDate;
    @Schema(title = "일정 시작 시간", type = "String", description = "일정의 시작 시간 (날짜 제외)", defaultValue = "00:00:00", hidden = true)
    private LocalTime startTime;
    @Schema(title = "일정 종료 시간", type = "String", description = "일정의 종료 시간 (날짜 제외)", defaultValue = "23:59:59", hidden = true)
    private LocalTime endTime;
    @Schema(title = "일정 반복 종료 시간", type = "String", description = "일정의 종료 날짜 (시간 제외 - 항상 23:59:59 로 설정됨)", defaultValue = "2024-12" +
            "-12", hidden = true)
    private LocalDate repeatEndDate;
    @Schema(title = "반복 종류 식별", type = "String", description = "주반복: week, 월반복: month", defaultValue = "week", hidden = true)
    private String repeatType;
    @Schema(title = "반복일 식별 숫자", type = "Integer", description = "주반복일 경우: 0~6 (0금 ~ 6목), 월반복일 경우: 1~31", defaultValue = "1", hidden = true)
    private Integer repeatNum;

    public TodoSelectVo() {
    }

    public TodoSelectVo(Integer itodo, String todoContent, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        this.itodo = itodo;
        this.todoContent = todoContent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TodoSelectVo(Integer itodo, String todoContent, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, LocalDate repeatEndDate, String repeatType, Integer repeatNum) {
        this.itodo = itodo;
        this.todoContent = todoContent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatEndDate = repeatEndDate;
        this.repeatType = repeatType;
        this.repeatNum = repeatNum;
    }
}
