package team6.project.emotion;

import org.apache.ibatis.annotations.Mapper;
import team6.project.emotion.model.*;

import java.util.List;

@Mapper
public interface EmotionMapper {
    int postEmo(EmotionInsDto dto);
    List<EmotionSelVo> getEmotions(EmotionSelDto dto);
    int delEmo(EmotionDelDto dto);
    List<EmotionSel> getEmoChart(EmotionSelAsChartDto dto);
}
