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
@Schema(name = "투두 수정", title = "투두 수정")
public class PatchTodoDto {

    @NotNull(message = "유저 PK 는 필수값")
    @Range(min=1L, message = "유저 PK 는 1 이상")
    @Schema(title = "유저 PK", minimum = "1", type = "Integer", description = "필수값")
    private Integer iuser;

    @NotNull(message = "투두 PK 는 필수값")
    @Range(min=1L, message = "유저 PK 는 1 이상")
    @Schema(title = "투두 PK", minimum = "1", type = "Integer", description = "필수값")
    private Integer itodo;

    @Schema(title = "투두 제목", maximum = "100", type = "String")
    private String todoContent;

    @Schema(title = "일정 시작일", type = "날짜", format = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(title = "일정 종료일", type = "날짜", format = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(title = "일정 시작 시간", type = "시간", format = "HH:mm:ss")
    private LocalTime startTime;

    @Schema(title = "일정 종료 시간", type = "시간", format = "HH:mm:ss")
    private LocalTime endTime;

    @Schema(title = "반복 종료 시간", type = "날짜", format = "yyyy-MM-dd")
    private LocalDate repeatEndDate;

    @Schema(title = "반복 종류 시간", type = "String", description = "주반복: week, 월반복: month")
    private String repeatType;

    @Range(min = 1, max = 31, message = "올바른 반복 일 이 아님")
    @Schema(title = "반복 식별 숫자", type = "Integer", description = "주반복일 경우: 1~7, 월반복일 경우: 1~31")
    private Integer repeatNum;
}
