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


    @NotBlank(message = "투두 내용은 공백일 수 없음")
    @Length(max=100, message = "투두 내용은 100자 이내")
    private String todoContent;


    @NotNull(message = "일정 시작일은 null 일 수 없음")
    private LocalDate startDate;


    @NotNull(message = "일정 마감일은 null 일 수 없음")
    private LocalDate endDate;


    private LocalTime startTime;


    private LocalTime endTime;


    private LocalDate repeatEndDate;


    private String repeatType;


    @Range(min = 1, max = 31, message = "올바른 반복 일 이 아님")
    private Integer repeatNum;

    public TodoRegDto() {
        this.startTime = LocalTime.of(0, 0, 0);
        this.endTime = LocalTime.of(23, 59, 59);
    }
}
