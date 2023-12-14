package team6.project.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;


import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TodoRegDto {

    @NotNull(message = "유저 PK 는 필수값")
    @Range(min=1L, message = "유저 PK 는 1 이상")
    private Integer iuser;

    @JsonProperty("todo_content")
    @NotBlank(message = "투두 내용은 공백일 수 없음")
    @Length(max=100, message = "투두 내용은 100자 이내")
    private String todoContent;

    @JsonProperty("start_date")
    @NotNull(message = "일정 시작일은 null 일 수 없음")
    private LocalDate startDate;

    @JsonProperty("end_date")
    @NotNull(message = "일정 마감일은 null 일 수 없음")
    private LocalDate endDate;

    @JsonProperty("start_time")
    private LocalTime startTime;

    @JsonProperty("end_time")
    private LocalTime endTime;

    @JsonProperty("repeat_end_date")
    private LocalDate repeatEndDate;

    @JsonProperty("repeat_type")
    private String repeatType;

    @JsonProperty("repeat_num")
    @Range(min = 1, max = 31, message = "올바른 반복 일 이 아님")
    private Integer repeatNum;

    public TodoRegDto() {
        this.startTime = LocalTime.of(0, 0, 0);
        this.endTime = LocalTime.of(23, 59, 59);
    }
}
