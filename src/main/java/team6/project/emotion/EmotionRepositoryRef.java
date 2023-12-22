package team6.project.emotion;

import team6.project.emotion.model.*;

import java.util.List;

public interface EmotionRepositoryRef {


    Integer tagConvertInteger(String emoTag);

    Integer checkIuser(int iuser);

    int postEmo(EmotionInsDto dto);

    List<EmotionSelVo> getTodoMonth(EmotionSelDto dto);

    List<EmotionSelVo> getEmotionMonth(EmotionSelDto dto);

    List<EmotionSelVo> getRepeatTodoMonth(EmotionSelDto dto);

    int delEmo(EmotionDelDto dto);

    List<EmotionSel> getEmoChart(EmotionSelAsChartDto dto);

    EmotionDuplicationVo checkDuplicationEmo(EmotionDuplicationDto dto);


}
