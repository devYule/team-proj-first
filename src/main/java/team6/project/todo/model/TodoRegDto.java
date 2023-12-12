package team6.project.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TodoRegDto {

    private Integer iuser;

    @JsonProperty("todo_content")
    @NotBlank
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
    private Integer repeatNum;

    public TodoRegDto() {
        this.startTime = LocalTime.of(0, 0, 0);
        this.endTime = LocalTime.of(23, 59, 59);
    }
}
