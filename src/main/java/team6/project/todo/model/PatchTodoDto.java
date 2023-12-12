package team6.project.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class PatchTodoDto {


    private Integer iuser;

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
    @Schema(description = "기존 일정에 반복정보가 있고, 수정시 반복정보를 수정하지 않더라도 해당 수정보의 값들을 모두 담아서 보내야함.")
    private LocalDate repeatEndDate;
    @JsonProperty("repeat_type")
    @Schema(description = "기존 일정에 반복정보가 있고, 수정시 반복정보를 수정하지 않더라도 해당 수정보의 값들을 모두 담아서 보내야함.")
    private String repeatType;
    @JsonProperty("repeat_num")
    @Schema(description = "기존 일정에 반복정보가 있고, 수정시 반복정보를 수정하지 않더라도 해당 수정보의 값들을 모두 담아서 보내야함.")
    private Integer repeatNum;
}
