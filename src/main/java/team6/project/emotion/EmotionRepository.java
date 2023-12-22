package team6.project.emotion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import team6.project.emotion.model.*;


import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EmotionRepository {

    private final EmotionMapper emotionMapper;

    public Integer tagConvertInteger(String emoTag) {
        return emotionMapper.tagConvertInteger(emoTag);
    }
    public Integer checkIuser(int iuser){
        return emotionMapper.checkIuser(iuser);
    }
    public int postEmo(EmotionInsDto dto) {
        return emotionMapper.postEmo(dto);
    }
    public List<EmotionSelVo> getTodoMonth(EmotionSelDto dto) {
        return emotionMapper.getTodoMonth(dto);
    }

    public List<EmotionSelVo> getEmotionMonth(EmotionSelDto dto) {
        return emotionMapper.getEmotionMonth(dto);
    }

    public List<EmotionSelVo> getRepeatTodoMonth(EmotionSelDto dto) {
        return emotionMapper.getRepeatTodoMonth(dto);
    }
    public int delEmo(EmotionDelDto dto){
        return emotionMapper.delEmo(dto);
    }
    public List<EmotionSel> getEmoChart(EmotionSelAsChartDto dto){
        return emotionMapper.getEmoChart(dto);
    }
    public EmotionDuplicationVo checkDuplicationEmo(EmotionDuplicationDto dto){
        return emotionMapper.checkDupliEmo(dto);
    }

}
