package team6.project.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(name = "투두 등록", title = "투두 등록")
public class TodoRegDto {

    @Schema(title = "유저 PK", minimum = "1", type = "Integer", description = "필수값")
    @NotNull(message = "유저 PK 는 필수값")
    @Range(min=1L, message = "유저 PK 는 1 이상")
    private Integer iuser;

    @Schema(title = "투두 제목", maximum = "100", type = "String", description = "필수값, 공백일 수 없음")
    @NotBlank(message = "투두 내용은 공백일 수 없음")
    @Length(max=100, message = "투두 내용은 100자 이내")
    private String todoContent;

    @Schema(title = "일정 시작일", type = "날짜", description = "필수값", format = "yyyy-MM-dd")
    @NotNull(message = "일정 시작일은 null 일 수 없음")
    private LocalDate startDate;


    @Schema(title = "일정 종료일", type = "날짜", description = "필수값")
    @NotNull(message = "일정 종료일은 null 일 수 없음")
    private LocalDate endDate;

    @Schema(title = "일정 시작 시간", type = "시간", description = "nano 는 무시 가능")
    private LocalTime startTime;

    @Schema(title = "일정 종료 시간", type = "시간", description = "nano 는 무시 가능")
    private LocalTime endTime;

    @Schema(title = "반복 종료 시간", type = "날짜")
    private LocalDate repeatEndDate;

    @Schema(title = "반복 종류 식별", type = "String", description = "주반복: week, 월반복: month")
    private String repeatType;

    @Range(min = 0, max = 31, message = "올바른 반복 일 이 아님")
    @Schema(title = "반복일 식별 숫자", type = "Integer", description = "주반복일 경우: 0~6 (0금 ~ 6목), 월반복일 경우: 1~31")
    private Integer repeatNum;

    public TodoRegDto() {
        this.startTime = LocalTime.of(0, 0, 0);
        this.endTime = LocalTime.of(23, 59, 59);
    }
}
