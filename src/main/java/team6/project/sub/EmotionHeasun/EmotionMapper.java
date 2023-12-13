package team6.project.sub.EmotionHeasun;

import org.apache.ibatis.annotations.Mapper;
import team6.project.sub.EmotionHeasun.model.EmotionInsDto;

@Mapper
public interface EmotionMapper {

    int insEmotion(EmotionInsDto dto);
}
