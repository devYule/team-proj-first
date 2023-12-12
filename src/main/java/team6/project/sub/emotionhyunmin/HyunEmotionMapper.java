package team6.project.sub.emotionhyunmin;

import org.apache.ibatis.annotations.Mapper;
import team6.project.sub.emotionhyunmin.model.proc.EmotionSel;
import team6.project.sub.emotionhyunmin.model.proc.EmotionSelProcDto;
import team6.project.sub.emotionhyunmin.model.proc.TodoSelProcDto;
import team6.project.sub.emotionhyunmin.model.proc.TodoSel;

import java.util.List;

@Mapper
public interface HyunEmotionMapper {


    List<TodoSel> getHasTodoInfo(TodoSelProcDto dto);

    List<EmotionSel> getEmotionInfo(EmotionSelProcDto dto);
}
