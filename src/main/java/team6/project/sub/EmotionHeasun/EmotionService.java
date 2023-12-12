package team6.project.sub.EmotionHeasun;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team6.project.common.ResVo;
import team6.project.sub.EmotionHeasun.EmotionMapper;
import team6.project.sub.EmotionHeasun.model.EmotionInsDto;

@Service
@RequiredArgsConstructor
public class EmotionService {
    private final EmotionMapper mapper;


    public ResVo postEmotion(EmotionInsDto dto){
        int result = mapper.insEmotion(dto);
        return new ResVo(result);
    }
}
