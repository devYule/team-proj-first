package team6.project.emotion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team6.project.common.ResVo;
import team6.project.common.exception.BadInformationException;
import team6.project.common.exception.MyMethodArgumentNotValidException;
import team6.project.common.exception.NoSuchDataException;
import team6.project.common.utils.CommonUtils;
import team6.project.emotion.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmotionService {

    public final EmotionMapper emotionMapper;

    private final CommonUtils utils;

    //(일 별)이모션 단계,이모션태그 insert//
    public ResVo postEmo(EmotionInsDto dto) {
        Integer checkIuser=emotionMapper.checkIuser(dto.getIuser());
        if(checkIuser==null){
            throw new NoSuchDataException("올바른 iuser 값을 보내주세요");
        }
        Integer emoTagInt=emotionMapper.tagConvertInteger(dto.getEmoTag());
        if(emoTagInt==null){
            throw new BadInformationException("태그를 올바르게 작성 해 주세요");
        }
        dto.setEmoTagInt(emoTagInt);
        int result = emotionMapper.postEmo(dto);
        if(result==0){
            throw new RuntimeException("DB 오류");
        }
        return new ResVo(result);//1이면 잘 들어감.
    }

    //현재까지 작성한 이모션단계,이모션태그를 월별로 출력
    //이모션을 작성한 날에 Todo가 있으면 hasTodo 1, 없으면 0.

    public List<EmotionSelVo> getEmo(EmotionSelDto dto) {
        // 반복안되는 날에 할 일이 있으면 그 날 가져오고,hasTodo도 가져옴.
        List<EmotionSelVo> todo=emotionMapper.getTodoMonth(dto);
        // 반복되는 날에 할 일이 있으면 그 날 가져오고, hasTodo도 가져옴.
        todo.addAll(emotionMapper.getRepeatTodoMonth(dto));
        Integer checkIuser= emotionMapper.checkIuser(dto.getIuser());
        if(checkIuser==null){
            throw new NoSuchDataException("올바른 iuser 값을 보내주세요");
        }
        // 중복제거.
        List<EmotionSelVo> asMonth=todo.stream().distinct()
                .collect(Collectors.toList());

        // 감정 등록한 날들의 날짜와, emotionTag, emotionGrade 가져옴.
        List<EmotionSelVo> emotionMonth=emotionMapper.getEmotionMonth(dto);

        for (EmotionSelVo todosel:asMonth) {
            for (EmotionSelVo emotionsel : emotionMonth) {
                //_Todo가 적혀진 날들의 리스트와 이모션이 적혀진 날들의 리스트를 탐색.
                //날짜가 같으면 리스트에다 이모션이 적혀진 날,이모션태그,이모션단계,_Todo 1을 집어넣음.
                //만약에 같지 않으면?? => 어느 한쪽은 날짜,이모션태그,이모션단계가 있고 _Todo가 0,
                // 다른 쪽은 날짜,Todo가 있는데 이모션태그,이모션단계가 없음.

                if(emotionsel.getDay().equals(todosel.getDay())){
                    //emotionsel 의 날짜와 _todosel의 날짜가 같으면
                    emotionsel.setHasTodo(1);//이모션 태그 날짜에 _toDo 1을 설정해줌.
                    //emotionsel 에는 이모션이 등록된 날짜와, 그에 겹치는 _todo가 있는 날짜가 나옴.

                    //_todo 에 이모션을 설정해줌.
                    todosel.setEmotionTag(emotionsel.getEmotionTag());
                    todosel.setEmotionGrade(emotionsel.getEmotionGrade());
                    //_todoMonth 에는 _todo의 날짜와 그에 겹치는 날짜의 emotion 이 들어있음.
                    break;
                }
            }
        }
        int i=0;
        List<EmotionSelVo> anotherList=new ArrayList<>();

        //다른 리스트 생성.
        for (EmotionSelVo emotionSelVo:emotionMonth) {
            //이모션이 등록되 날짜만큼 반복.
            i=emotionSelVo.getHasTodo();
            //만약 emotionMonth 에 있는 날짜의 hasTodo 가 0이면 => emotion는 있는데 _todo 가 없음.
            //즉 _todo가 없는 emotion만 등록된 날짜만 가져옴.
            if(i==0){
                //다른리스트에 그 날을 넣어줌.
                anotherList.add(emotionSelVo);
                //anotherList에는 emotion과 emotion이 등록된 날짜와 곂치는 날이 들어있음(_hasTodo 1)
            }
        }
        asMonth.addAll(anotherList); // _todoMonth 에 anotherList 를 넣어줌.

        Comparator<EmotionSelVo> byDay = Comparator.comparing(EmotionSelVo::getDay);
        Collections.sort(asMonth , byDay);
        return asMonth;
    }

    // 해당 날의 이모션들 삭제 //
    public ResVo delEmo(EmotionDelDto dto) {
        Integer checkIuser= emotionMapper.checkIuser(dto.getIuser());
        if(checkIuser==null){
            throw new NoSuchDataException("올바른 iuser 값을 보내주세요");
        }
        int result = emotionMapper.delEmo(dto);
        return new ResVo(result);
    }

    // iuser 값을 받아 해당 주의 월요일부터 오늘까지 모든 기록된 감정과
    //감정 태그 의 주별 결산을 가져옴  //
    public EmotionSelAsChartVo getEmoChart(int iuser) {
        Integer checkIuser= emotionMapper.checkIuser(iuser);
        if(checkIuser==null){
            throw new NoSuchDataException("올바른 iuser 값을 보내주세요");
        }

        //오늘
        LocalDate today = LocalDate.now();
        //오늘이 일주일의 몇번째 인지 월요일=1,화요일=2,수요일=3
        //목요일=4,금요일=5,토요일=6,일요일=7.
        int day = today.get(DAY_OF_WEEK)-1;
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
        List<Integer> weeks = new ArrayList<>();
        for (EmotionSel emotionSel : emotionSelList) {
            weeks.add(emotionSel.getDayOfTheWeek());
        }
        /* _TODO: 2023-12-12
            문자 -> 숫자 (Monday: 1, ... Sunday = 7)
            --by Hyunmin for 승준 */
        //iuser, startWeek,endWeek
        EmotionSelAsChartVo selAsChartVo = new EmotionSelAsChartVo();
        for (int i = 1; i < 8; i++) {
            if(weeks.contains(i)){
                continue;
            }
            EmotionSel emotionSel = new EmotionSel();
            emotionSel.setEmotionGrade(3);
            emotionSel.setDayOfTheWeek(i);
            emotionSelList.add(emotionSel);

        }


        selAsChartVo.setEmoChart(emotionSelList);
        for (EmotionSel emo : emotionSelList) {
            emo.setDayOfTheWeek(utils.fromJavaTo(emo.getDayOfTheWeek()));
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
        emotionSelList.sort(Comparator.comparing((EmotionSel::getDayOfTheWeek)));

        return selAsChartVo;
    }

}