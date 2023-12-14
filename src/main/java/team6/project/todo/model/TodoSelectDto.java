package team6.project.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import team6.project.todo.model.ref.TodoSelectDtoRef;

import java.time.LocalDate;


@ToString
public class TodoSelectDto extends TodoSelectDtoRef {


    @Getter
    @Setter
    private Integer iuser;

    @Getter
    @JsonIgnore
    private LocalDate selectedDate;


    @NotNull(message = "년 정보는 필수값")
    @Range(min = 1900, max = 9999, message = "년은 1900 이상 9999 이하 허용")
    @Setter
    private Integer y;
    @NotNull(message = "월 정보는 필수값")
    @Range(min = 1, max = 12, message = "월은 1 이상 12 이하 허용")
    @Setter
    private Integer m;
    @NotNull(message = "일 정보는 필수값")
    @Range(min = 1, max = 31, message = "일은 1 이상 31 이하 허용")
    @Setter
    private Integer d;

    public void setDate() {
        this.selectedDate = LocalDate.of(y, m, d);
    }


}
