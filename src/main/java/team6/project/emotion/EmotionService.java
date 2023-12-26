package team6.project.emotion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team6.project.common.ResVo;
import team6.project.common.exception.BadDateInformationException;
import team6.project.common.exception.BadInformationException;
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
import static team6.project.common.Const.BAD_INFO_EX_MESSAGE;
import static team6.project.common.Const.RUNTIME_EX_MESSAGE;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmotionService {

    private final EmotionRepositoryRef emotionRepository;


    //(일 별)이모션 단계,이모션태그 insert//
    public ResVo postEmo(EmotionInsDto dto) {
        Integer checkIuser = emotionRepository.checkIuser(dto.getIuser());
        if (checkIuser == null) {
            throw new NoSuchDataException(BAD_INFO_EX_MESSAGE);
        }
        //일단위 중복 체크.
        LocalDate datetoday = LocalDate.now();
        EmotionDuplicationDto duplication = new EmotionDuplicationDto();
        duplication.setDateToday(String.valueOf(datetoday));
        duplication.setIuser(dto.getIuser());

        EmotionDuplicationVo checkDuplication = emotionRepository.checkDuplicationEmo(duplication);

        if (checkDuplication != null) {
            throw new BadDateInformationException(BAD_INFO_EX_MESSAGE);
        }
        Integer emoTagInt = emotionRepository.tagConvertInteger(dto.getEmoTag());
        if (emoTagInt == null) {
            throw new BadInformationException(BAD_INFO_EX_MESSAGE);
        }
        dto.setEmoTagInt(emoTagInt);
        int result = emotionRepository.postEmo(dto);
        if (result == 0) {
            throw new RuntimeException(RUNTIME_EX_MESSAGE);
        }
        return new ResVo(result);
    }

    //연,달,userPk를 받아와 해당한 달의 모든 일정(반복 포함), 등록한 emotionGrade를 select.
    //해당한 날에 일정이 있으면 _todo=1, 없으면 _todo=0.
    public List<EmotionSelVo> getEmo(EmotionSelDto dto) {
        // 반복X 일정.
        List<EmotionSelVo> todo = emotionRepository.getTodoMonth(dto);
        // 반복O 일정 추가.
        todo.addAll(emotionRepository.getRepeatTodoMonth(dto));
        Integer checkIuser = emotionRepository.checkIuser(dto.getIuser());
        if (checkIuser == null) {
            throw new NoSuchDataException(BAD_INFO_EX_MESSAGE);
        }
        // 중복제거.
        List<EmotionSelVo> asMonth = todo.stream().distinct()
                .collect(Collectors.toList());

        // EmotionGrade,EmotionTag를 등록한 날의 EmotionGrade, 날짜 select.
        List<EmotionSelVo> emotionMonth = emotionRepository.getEmotionMonth(dto);

        for (EmotionSelVo todosel : asMonth) {
            for (EmotionSelVo emotionsel : emotionMonth) {
                //_Todo가 적혀진 날들의 리스트와 이모션이 적혀진 날들의 리스트를 탐색.

                if (emotionsel.getDay().equals(todosel.getDay())) {
                    //emotionsel 의 날짜와 _todosel의 날짜가 같으면

                    emotionsel.setHasTodo(1);
                    //이모션 태그 날짜에 _toDo 1을 설정해줌.

                    //asMonth 에 겹치는 날의 emotion 등록.
                    todosel.setEmotionTag(emotionsel.getEmotionTag());
                    todosel.setEmotionGrade(emotionsel.getEmotionGrade());
                    break;
                }
            }
        }
        int i = 0;
        for (EmotionSelVo emotionSelVo : emotionMonth) {

            i = emotionSelVo.getHasTodo();
            if (i == 0) {
                asMonth.add(emotionSelVo);
                //hasTodo가 0인 emotionMonth을 asMonth에 추가.
            }
        }
        Comparator<EmotionSelVo> byDay = Comparator.comparing(EmotionSelVo::getDay);
        Collections.sort(asMonth, byDay);

        return asMonth;
    }

    // 해당 날의 이모션들 삭제 //
    public ResVo delEmo(EmotionDelDto dto) {
        Integer checkIuser = emotionRepository.checkIuser(dto.getIuser());
        if (checkIuser == null) {
            throw new NoSuchDataException(BAD_INFO_EX_MESSAGE);
        }
        int result = emotionRepository.delEmo(dto);
        return new ResVo(result);
    }

    // iuser 값을 받아 해당 주의 월요일부터 오늘까지 모든 기록된 감정과
    //감정 태그 의 주별 결산을 select//
    public EmotionSelAsChartVo getEmoChart(int iuser) {
        Integer checkIuser = emotionRepository.checkIuser(iuser);
        if (checkIuser == null) {
            throw new NoSuchDataException(BAD_INFO_EX_MESSAGE);
        }

        //오늘
        LocalDate today = LocalDate.now();

        //오늘이 일주일의 몇번째 인지 월요일=1,화요일=2,수요일=3
        //목요일=4,금요일=5,토요일=6,일요일=7.
        int day = today.get(DAY_OF_WEEK) - 1;

        //"오늘 날짜"에서 "오늘이 일주일의 몇번째"를 빼면 요번주의 시작일이 나옴.
        //ex) "2023.12.21 목요일" 이라면 "목요일=3" [12.21 - 3일 = 12월18일 월요일]
        LocalDate start = today.minusDays(day);
        //시작날짜
        LocalDate end = start.plusDays(6);
        //끝나는 날짜
        EmotionSelAsChartDto emoDto = new EmotionSelAsChartDto();
        //Dto 값넣기
        emoDto.setIuser(iuser);
        emoDto.setStartWeek(String.valueOf(start));
        emoDto.setEndWeek(String.valueOf(end));

        //emotion이 등록된 날과,emotionGrade를 가져옴.(주 단위)
        List<EmotionSel> emotionSelList = emotionRepository.getEmoChart(emoDto);

        List<Integer> weeks = new ArrayList<>();
        for (EmotionSel emotionSel : emotionSelList) {
            weeks.add(emotionSel.getDayOfTheWeek());
        }
        //weeks 에 emotionSel의 요일을 넣어줌.
        EmotionSelAsChartVo selAsChartVo = new EmotionSelAsChartVo();
        for (int i = 1; i < 8; i++) {
            if (weeks.contains(i)) {
                continue;
            }
            EmotionSel emotionSel = new EmotionSel();
            emotionSel.setEmotionGrade(3);
            emotionSel.setDayOfTheWeek(i);
            emotionSelList.add(emotionSel);
        }

        selAsChartVo.setEmoChart(emotionSelList);
        for (EmotionSel emo : emotionSelList) {
            emo.setDayOfTheWeek(CommonUtils.fromJavaTo(emo.getDayOfTheWeek()));
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