package team6.project.emotion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team6.project.common.ResVo;
import team6.project.emotion.model.EmotionInsDto;
import team6.project.emotion.model.EmotionSelDto;
import team6.project.emotion.model.EmotionSelVo;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmotionService {
    public final EmotionMapper emotionMapper;
    //(일 별)이모션 단계,이모션태그 insert//
    public ResVo postEmo(EmotionInsDto dto){
        int result=emotionMapper.postEmo(dto);
        return new ResVo(result);//1이면 잘 들어감.
    }
    //현재까지 작성한 이모션단계,이모션태그를 월별로 출력
    //이모션을 작성한 날에 Todo가 있으면 hasTodo 1, 없으면 0.
    public List<EmotionSelVo> getEmo(EmotionSelDto dto){
        List<EmotionSelVo> emotionSelVos=emotionMapper.getEmotions(dto);
        return emotionSelVos;
    }

}
