package team6.project.sub.emotionhyunmin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team6.project.sub.emotionhyunmin.model.SubEmotionSelVo;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/emo/hyunmin")
@RequiredArgsConstructor
public class HyunEmotionController {

    private final HyunEmotionService service;

    @GetMapping("/{iuser}")
    public List<SubEmotionSelVo> getEmo(@PathVariable int iuser,
                                        @RequestParam("y") int year,
                                        @RequestParam("m") int month) {


        return service.getEmo(iuser, year, month);

    }

}
