package team6.project.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class PatchTodoDto {

    @NotNull
    private Integer iuser;
    @NotNull
    private Integer itodo;
    @JsonProperty("todo_content")
    private String todoContent;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("end_date")
    private LocalDate endDate;
    @JsonProperty("start_time")
    private LocalTime startTime;
    @JsonProperty("end_time")
    private LocalTime endTime;
    @JsonProperty("repeat_end_date")
    private LocalTime repeatEndDate;
    @JsonProperty("repeat_type")
    private String repeatType;
    @JsonProperty("repeat_num")
    private Integer repeatNum;
}
