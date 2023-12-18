package team6.project.todo.model.ref;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class TodoSelectVoRef {
    // 같은 Mapper 의 기능을 사용하는데,
    // itodo 가 필요한 경우도 있고, 필요하지 않은 경우도 있다.
    // 그리고 두 경우 각각 필요한 데이터들이 다르다.
    private Integer itodo;
    private Integer iuser;
    private LocalDate selectedDate;

    public TodoSelectVoRef() {
    }
    public TodoSelectVoRef(Integer iuser, Integer y, Integer m, Integer d) {
        this.iuser = iuser;
        this.selectedDate = LocalDate.of(y, m, d);
    }
    public TodoSelectVoRef(Integer itodo, Integer iuser) {
        this.itodo = itodo;
        this.iuser = iuser;
    }
}
