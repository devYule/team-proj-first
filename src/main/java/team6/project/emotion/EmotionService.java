package team6.project.emotion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team6.project.common.ResVo;
import team6.project.emotion.model.*;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmotionService {
    public final EmotionMapper emotionMapper;

    //(일 별)이모션 단계,이모션태그 insert//
    public ResVo postEmo(EmotionInsDto dto) {
        int result = emotionMapper.postEmo(dto);
        return new ResVo(result);//1이면 잘 들어감.
    }

    //현재까지 작성한 이모션단계,이모션태그를 월별로 출력
    //이모션을 작성한 날에 Todo가 있으면 hasTodo 1, 없으면 0.
    public List<EmotionSelVo> getEmo(EmotionSelDto dto) {
        List<EmotionSelVo> emotionSelVos = emotionMapper.getEmotions(dto);
        return emotionSelVos;
    }

    // 해당 날의 이모션들 삭제 //
    public ResVo delEmo(EmotionDelDto dto) {
        int result = emotionMapper.delEmo(dto);
        return new ResVo(result);
    }

    // iuser 값을 받아 해당 주의 월요일부터 오늘까지 모든 기록된 감정과
    //감정 태그 의 주별 결산을 가져옴  //
    public EmotionSelAsChartVo getEmoChart(int iuser) {
        //오늘
        LocalDate today = LocalDate.now();
        //오늘이 일주일의 몇번째 인지 월요일=1,화요일=2,수요일=3
        //목요일=4,금요일=5,토요일=6,일요일=7.
        int day = today.get(DAY_OF_WEEK);
        if (day == 7) {
            day = 0;
        }
        //"오늘 날짜"에서 "오늘이 일주일의 몇번째"를 빼면 요번주의 시작일이 나온다.
        //예를 들어 "2022.9.8 목요일" 이라면 "목요일=4" [9.8 - 4일 = 9월4일 일요일]
        LocalDate start = today.minusDays(day);
        LocalDate todayDate = start.plusDays(6);
        EmotionSelAsChartDto emoDto = new EmotionSelAsChartDto();
        //Dto 값넣기
        emoDto.setIuser(iuser);
        emoDto.setStartWeek(String.valueOf(start));
        emoDto.setToday(String.valueOf(todayDate));
        //Dto값넣어줌.
        List<EmotionSel> emotionSelList = emotionMapper.getEmoChart(emoDto);
        EmotionSel emotionSel = emotionSelList.get(0);
        /* TODO: 2023-12-12
            문자 -> 숫자 (Monday: 1, ... Sunday = 7)
            --by Hyunmin for 승준 */
        //iuser, startWeek,endWeek
        EmotionSelAsChartVo selAsChartVo = new EmotionSelAsChartVo();
        selAsChartVo.setEmoChart(emotionSelList);
        for (EmotionSel emo : emotionSelList) {
            switch (emo.getEmotionGrade()) {
                case 1, 2:
                    selAsChartVo.setGood(selAsChartVo.getGood() + 1);
                    break;
                case 3:
                    selAsChartVo.setNormal(selAsChartVo.getNormal() + 1);
                    break;
                case 4, 5:
                    selAsChartVo.setBad(selAsChartVo.getBad() + 1);
                    break;
            }
        }
        return selAsChartVo;
    }

}
