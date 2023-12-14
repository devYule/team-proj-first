package team6.project.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@ToString
@Schema(description = "기존데이터 필수로 제공, 변경된 사항은 변경된 데이터로 제공")
public class PatchTodoDto {

    @NotNull(message = "유저 PK 는 필수값")
    @Range(min=1L, message = "유저 PK 는 1 이상")
    private Integer iuser;
    @NotNull(message = "투두 PK 는 필수값")
    @Range(min=1L, message = "유저 PK 는 1 이상")
    private Integer itodo;

    private String todoContent;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    @Schema(description = "기존 일정에 반복정보가 있고, 수정시 반복정보를 수정하지 않더라도 해당 수정보의 값들을 모두 담아서 보내야함.")
    private LocalDate repeatEndDate;

    @Schema(description = "기존 일정에 반복정보가 있고, 수정시 반복정보를 수정하지 않더라도 해당 수정보의 값들을 모두 담아서 보내야함.")
    private String repeatType;

    @Range(min = 1, max = 31, message = "올바른 반복 일 이 아님")
    @Schema(description = "기존 일정에 반복정보가 있고, 수정시 반복정보를 수정하지 않더라도 해당 수정보의 값들을 모두 담아서 보내야함.")
    private Integer repeatNum;
}
