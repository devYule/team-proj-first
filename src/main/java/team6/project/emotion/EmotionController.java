package team6.project.emotion;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.*;
import team6.project.common.ResVo;
import team6.project.common.exception.BadInformationException;
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
        if (iuser < 1) {
            throw new BadInformationException("올바른 iuser값 입력");
        }
    }

    @PostMapping
    @Operation(summary = "이모션 등록", description = "iuser(int)  ,emoGrade(int)  ,emoTag(String)<br>" +
            "값을 받아서 일일 이모션 단계와 태그를 등록한다. iuser=개인마다 다른 값.<br> emoGrade=이모션 단계. 1(매우좋음)~5(매우나쁨)까지 있음." +
            "emoTag=여러가지 이모션태그 중에서 하나만 입력받는 값.ex)괴로운,귀찮은,기쁜,즐거운 ... 총 28가지.<br>" +
            "성공시<br>" +
            "result: 1 리턴<br>" +
            "실패시<br>" +
            "errorMessage 와 errorCode 리턴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResVo postEmo(@RequestBody EmotionInsDto dto) {
        int getIuser = dto.getIuser();
        checkIuser(getIuser);
        log.debug("EmotionInsDto : {}", dto);
        return emotionService.postEmo(dto);
    }

    @GetMapping("/{iuser}")

    @Operation(summary = "이모션&투두 월별 조회", description = "이모션이나,투두가 등록되 날짜를 모두 출력하며,<br>투두가 있는 날에" +
            "이모션이 등록되어 있지 않으면 ,emotionGrade : 0  ,emotionTag : null  ,hasTodo : 1  <br>" +
            "투두가 없는 날에 이모션이 등록되어 있으면 ,emotionGrade : 1~5  ,emotionTag : 이모션태그(String)  ,hasTodo : 0  <br>" +
            "투두가 있고 이모션도 등록되어 있으면 ,emotionGrade : 1~5  ,emotionTag : 이모션태그(String)  ,hasTodo : 1  <br>" +
            "성공시<br>" +
            "여기 작성<br>" +
            "실패시<br>" +
            "errorMessage 와 errorCode 리턴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public List<EmotionSelVo> getEmo(@PathVariable int iuser, @RequestParam @Range(min = 1900, max = 9999) int y,
                                     @RequestParam @Range(min = 1, max = 12, message = "올바르지 않은 달 입력") int m) {
        EmotionSelDto dto = new EmotionSelDto(iuser, y, m);
        checkIuser(dto.getIuser());
        log.debug("EmotionSelDto : {}", dto);
        return emotionService.getEmo(dto);
    }

    @DeleteMapping("/{iuser}/{iemo}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Operation(summary = "이모션 삭제", description = "유저PK,이모션PK를 받아와 해당하는 날에 등록된 이모션 삭제", hidden = true)
    public ResVo deleteEmo(@PathVariable int iuser,
                           @PathVariable @NotNull @Range(min = 1, max = 5) Integer iemo) {
        EmotionDelDto dto = new EmotionDelDto(iuser, iemo);
        checkIuser(dto.getIuser());
        log.debug("EmotionDelDto : {}", dto);
        return emotionService.delEmo(dto);
    }
    @GetMapping("/chart/{iuser}")
    @Operation(summary = "주간 차트", description = "이모션이 등록된 날짜를 주 단위로 조회하여 통계출력<br>" +
            "여기 설명 작성<br>" +
            "성공시<br>" +
            "여기 작성<br>" +
            "실패시<br>" +
            "errorMessage 와 errorCode 리턴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public EmotionSelAsChartVo getEmoChart(@PathVariable int iuser) {
        log.debug("iuser : {}", iuser);
        checkIuser(iuser);
        return emotionService.getEmoChart(iuser);
    }
}
