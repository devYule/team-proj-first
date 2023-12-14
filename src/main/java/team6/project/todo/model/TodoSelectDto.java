package team6.project.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import team6.project.todo.model.ref.TodoSelectDtoRef;

import java.time.LocalDate;


@ToString
@Schema(name = "투두 조회",title = "투두 조회")
public class TodoSelectDto extends TodoSelectDtoRef {


    @Getter
    @Setter
    @Schema(title = "유저 PK", minimum = "1", type = "Integer", description = "필수값")
    private Integer iuser;

    @Getter
    @JsonIgnore

    private LocalDate selectedDate;


    @NotNull(message = "년 정보는 필수값")
    @Range(min = 1900, max = 9999, message = "년은 1900 이상 9999 이하 허용")
    @Setter
    @Schema(title = "선택된 날짜의 년 정보", type = "Integer", format = "yyyy")
    private Integer y;
    @NotNull(message = "월 정보는 필수값")
    @Range(min = 1, max = 12, message = "월은 1 이상 12 이하 허용")
    @Setter
    @Schema(title = "선택된 날짜의 월 정보", type = "Integer", format = "MM")
    private Integer m;
    @NotNull(message = "일 정보는 필수값")
    @Range(min = 1, max = 31, message = "일은 1 이상 31 이하 허용")
    @Setter
    @Schema(title = "선택된 날짜의 일 정보", type = "Integer", format = "dd")
    private Integer d;

    public void setDate() {
        this.selectedDate = LocalDate.of(y, m, d);
    }


}
