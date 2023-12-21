package team6.project.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import static team6.project.common.Const.BAD_DATE_INFO_EX_MESSAGE;

@Data
public class TodoSelectDto {

    @JsonIgnore
    private Integer iuser;

    @NotNull(message = "년 정보는 필수값")
    @Range(min = 1900, max = 9999, message = BAD_DATE_INFO_EX_MESSAGE)
    @Schema(title = "선택된 날짜의 년 정보", type = "Integer", format = "yyyy", description = "2023", defaultValue = "2023")
    private Integer y;
    @NotNull(message = "월 정보는 필수값")
    @Range(min = 1, max = 12, message = BAD_DATE_INFO_EX_MESSAGE)
    @Schema(title = "선택된 날짜의 월 정보", type = "Integer", format = "MM", description = "12", defaultValue = "12")
    private Integer m;
    @NotNull(message = "일 정보는 필수값")
    @Range(min = 1, max = 31, message = BAD_DATE_INFO_EX_MESSAGE)
    @Schema(title = "선택된 날짜의 일 정보", type = "Integer", format = "dd", description = "12", defaultValue = "12")
    private Integer d;

}
