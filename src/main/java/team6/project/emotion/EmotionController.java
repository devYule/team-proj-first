package team6.project.emotion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team6.project.common.ResVo;
import team6.project.emotion.model.EmotionInsDto;
import team6.project.emotion.model.EmotionSelDto;
import team6.project.emotion.model.EmotionSelVo;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/emo")
public class EmotionController {
    public final EmotionService emotionService;
    @PostMapping
    public ResVo postEmo(@RequestBody EmotionInsDto dto){
        log.info("EmotionInsDto : {}",dto);
        return emotionService.postEmo(dto);
    }
    @GetMapping
    public List<EmotionSelVo> getEmo(EmotionSelDto dto){
        log.info("EmotionSelDto : {}",dto);
        return emotionService.getEmo(dto);
    }

    @DeleteMapping
    public ResVo deleteEmo(){
        return null;
    }
    @DeleteMapping
    public ResVo deleteTag(){
        return null;
    }
}
