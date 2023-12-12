package team6.project.sub.EmotionHeasun;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team6.project.common.ResVo;
import team6.project.sub.EmotionHeasun.model.EmotionInsDto;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EmotionController {
    private final EmotionService service;
    //emotion table에 고객에게서 받은 정보를 insert

    @PostMapping("/api/emo")
    public ResVo postEmotion(EmotionInsDto dto){
        return service.postEmotion(dto);
    }

}
