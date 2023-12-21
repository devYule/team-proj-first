package team6.project.emotion.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "날짜별 이모션,Todo 조회", title = "날짜,이모션단계,이모션태그,일정이 있는지 확인"
        ,description = "리스트타입으로 반환하며, 일정이 아예 없거나, 이모션이 등록되어있지 않은 날은 표시하지 않음.")
public class EmotionSelVo {
    @Schema(title = "날짜", minimum = "1", maximum = "31",type = "String",description = "월 단위로 출력할 때 <br>year-month-day 형식으로 날짜 반환." +
            "<br> ex) 2023-12-31")
    private String day;
    @Schema(title = "이모션 단계", minimum = "1", maximum = "5",type = "int",description = "월 단위로 출력 할 때 <br>" +
            "매일 선택한 이모션 단계를 반환.")
    private int emotionGrade;
    @Schema(title = "이모션 태그",type = "String",description = "월 단위로 출력할 때 <br>" +
            "매일 선택한 이모션 태그를 반환. ex) emotionTag: 즐거운, emotionTag: 후련한")
    private String emotionTag;
    @Schema(title = "일정", minimum = "0", maximum = "1",type = "int",description = "월 단위로 출력 할 때 <br>" +
            "그날 일정(Todo)이 있으면 1 , 0이면 일정(Todo) 없음")
    private int hasTodo;
}
