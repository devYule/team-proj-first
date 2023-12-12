package team6.project.emotion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team6.project.common.ResVo;
import team6.project.emotion.model.*;

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
    @GetMapping("/{iuser}")
    public List<EmotionSelVo> getEmo(@PathVariable int iuser,
                                     @RequestParam("y") int year,
                                     @RequestParam("m") int month){
        EmotionSelDto dto = new EmotionSelDto(iuser, year, month);
        log.info("EmotionSelDto : {}",dto);
        return emotionService.getEmo(dto);
    }
    @DeleteMapping

    public ResVo deleteEmo(EmotionDelDto dto){
        log.info("EmotionDelDto : {}",dto);
        return emotionService.delEmo(dto);
    }
    @GetMapping("/chart/{iuser}")
    public EmotionSelAsChartVo getEmoChart(@PathVariable int iuser){
        log.info("iuser : {}",iuser);
        return emotionService.getEmoChart(iuser);
    }
}
