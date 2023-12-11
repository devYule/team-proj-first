package team6.project.emotion;

import org.apache.ibatis.annotations.Mapper;
import team6.project.emotion.model.EmotionInsDto;
import team6.project.emotion.model.EmotionSelDto;
import team6.project.emotion.model.EmotionSelVo;

import java.util.List;

@Mapper
public interface EmotionMapper {
    int postEmo(EmotionInsDto dto);
    List<EmotionSelVo> getEmotions(EmotionSelDto dto);
}
