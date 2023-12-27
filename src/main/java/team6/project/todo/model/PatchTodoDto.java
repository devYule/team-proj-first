package team6.project.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalTime;

import static team6.project.common.Const.BAD_INFO_EX_MESSAGE;

@Getter
@Setter
@Builder
@ToString
@Schema(name = "투두 수정", title = "투두 수정")
public class PatchTodoDto {

    @NotNull(message = BAD_INFO_EX_MESSAGE)
    @Range(min=1L, message = BAD_INFO_EX_MESSAGE)
    @Schema(title = "유저 PK", minimum = "1", type = "Integer", description = "필수값", defaultValue = "1")
    private Integer iuser;

    @NotNull(message = BAD_INFO_EX_MESSAGE)
    @Range(min=1L, message = BAD_INFO_EX_MESSAGE)
    @Schema(title = "투두 PK", minimum = "1", type = "Integer", description = "필수값", defaultValue = "1")
    private Integer itodo;

    @Schema(title = "투두 제목", maximum = "100", type = "String", defaultValue = "todoContent")
    private String todoContent;

    @Schema(title = "일정 시작일", type = "String", defaultValue = "2023-12-12", hidden = false)
    private LocalDate startDate;

    @Schema(title = "일정 종료일", type = "String", defaultValue = "2023-12-13", hidden = false)
    private LocalDate endDate;

    @Schema(title = "일정 시작 시간", type = "String", description = "nano 는 무시 가능", defaultValue = "00:00:00", hidden = false)
    private LocalTime startTime;

    @Schema(title = "일정 종료 시간", type = "String", description = "nano 는 무시 가능", defaultValue = "23:59:59", hidden = false)
    private LocalTime endTime;

    @Schema(title = "반복 종료일", type = "String", defaultValue = "2024-12-12", hidden = false)
    private LocalDate repeatEndDate;

    @Schema(title = "반복 종류 식별", type = "String", description = "주반복: week, 월반복: month", defaultValue = "week", hidden = false)
    private String repeatType;

    @Range(min = 0, max = 31, message = "올바른 반복 일 이 아님")
    @Schema(title = "반복일 식별 숫자", type = "Integer", description = "주반복일 경우: 0~6 (0금 ~ 6목), 월반복일 경우: 1~31", defaultValue = "3", hidden = false)
    private Integer repeatNum;

}
