package team6.project.emotion;

import org.apache.ibatis.annotations.Mapper;
import team6.project.emotion.model.*;

import java.util.List;

@Mapper
public interface EmotionMapper {
    //이모션 등록
    int postEmo(EmotionInsDto dto);
    //이번달 반복하지 않는 투두 날짜, hasTodo 가져옴.
    List<EmotionSelVo> getTodoMonth(EmotionSelDto dto);
    //이번달에 등록한 이모션 가져옴.
    List<EmotionSelVo> getEmotionMonth(EmotionSelDto dto);
    //이번달 반복하는 투두 날짜, hasTodo 가져옴.
    List<EmotionSelVo> getRepeatTodoMonth(EmotionSelDto dto);
    int delEmo(EmotionDelDto dto);
    List<EmotionSel> getEmoChart(EmotionSelAsChartDto dto);
}
