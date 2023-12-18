package team6.project.emotion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team6.project.common.ResVo;
import team6.project.common.exception.MyMethodArgumentNotValidException;
import team6.project.emotion.model.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/emo")
@Tag(name = "이모션, 태그", description = "이모션, 태그 등록, 조회, 수정, 삭제")
public class EmotionController {
    public final EmotionService emotionService;
    private void checkIuser(int iuser) {
        if (iuser<1) {
            throw new MyMethodArgumentNotValidException("올바른 iuser값 입력");
        }
    }
    @PostMapping
    @Operation(summary = "이모션 등록",description = "이모션 단계,이모션 태그 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200" ,description = "성공"),
            @ApiResponse(responseCode = "400",description = "요청 오류"),
            @ApiResponse(responseCode = "500",description = "서버 오류")
    })
    public ResVo postEmo(@RequestBody EmotionInsDto dto){
        int getIuser=dto.getIuser();
        
        log.info("EmotionInsDto : {}",dto);
        return emotionService.postEmo(dto);
    }
    @GetMapping("/{iuser}")
    @Operation(summary = "이모션&투두 월별 조회",description = "이모션이 등록된 날짜와 투두가 등록된 날짜를 월별로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public List<EmotionSelVo> getEmo(@PathVariable int iuser,
                                     @RequestParam("y") int year,
                                     @RequestParam("m") int month){
        checkIuser(iuser);

        EmotionSelDto dto = new EmotionSelDto(iuser, year, month);
        log.info("EmotionSelDto : {}",dto);

        return emotionService.getEmo(dto);
    }
    @DeleteMapping("/{iuser}/{iemo}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Operation(summary = "이모션 삭제",description = "유저PK,이모션PK를 받아와 해당하는 날에 등록된 이모션 삭제")
    public ResVo deleteEmo(EmotionDelDto dto){

        log.info("EmotionDelDto : {}",dto);
        return emotionService.delEmo(dto);
    }
    @GetMapping("/chart/{iuser}")
    @Operation(summary = "주간 차트",description = "이모션이 등록된 날짜를 주 단위로 조회하여 통계출력")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public EmotionSelAsChartVo getEmoChart(@PathVariable int iuser){
        log.info("iuser : {}",iuser);
        return emotionService.getEmoChart(iuser);
    }
}
