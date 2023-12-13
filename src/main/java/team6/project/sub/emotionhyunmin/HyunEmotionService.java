package team6.project.sub.emotionhyunmin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team6.project.common.Const;
import team6.project.sub.emotionhyunmin.model.SubEmotionSelVo;
import team6.project.sub.emotionhyunmin.model.proc.EmotionSel;
import team6.project.sub.emotionhyunmin.model.proc.EmotionSelProcDto;
import team6.project.sub.emotionhyunmin.model.proc.TodoSelProcDto;
import team6.project.sub.emotionhyunmin.model.proc.TodoSel;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HyunEmotionService {

    private final HyunEmotionMapper mapper;

    public List<SubEmotionSelVo> getEmo(int iuser,
                                        int year,
                                        int month) {

        // map 으로 {day, Object {day, emotionGrade, emotionTag}, {day, hasTodo}} 형태 구현

        /*
        list 로 day + emotionGrade + emotionTag || day + hasTodo 를 가져옴
        각각 day 를 key 로, 객체 자체를 value 로 map 생성. (A, B)
        C list 를 만들어서 A의 key 로 1 일부터 해당 월의 마지막일까지 ++ 하면서 A 와 B 에서 객체 가져온 후,
        EmotionSelVo 에 값을 담음 (A 또는 B 가 없으면 해당 필드에는 null 대입, 둘다 없으면 list 에 add 하지 않음)
         */

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = LocalDate.of(year, month, firstDayOfMonth.lengthOfMonth());

        // 정제 전
        List<TodoSel> todoResult = mapper.getHasTodoInfo(new TodoSelProcDto(iuser, firstDayOfMonth, lastDayOfMonth));
        Map<Integer, SubEmotionSelVo> todoMap = new HashMap<>();
        // 정제
        todoResult.forEach(todo -> {

            try {
                if (todo.getRepeatType().equalsIgnoreCase(Const.WEEK)) {
                    // week 반복일때 로직
                    LocalDate dayWalk = LocalDate.of(firstDayOfMonth.getYear(), firstDayOfMonth.getMonth(),
                            firstDayOfMonth.getDayOfMonth());
                    while (dayWalk.getDayOfWeek().getValue() != todo.getRepeatNum()) {
                        dayWalk = dayWalk.plusDays(1);
                    }
                    LocalDate refDate = firstDayOfMonth.plusMonths(1);
                    // 당월의 첫번째 반복 주 = checkWeekRepeat
                    while (dayWalk.isBefore(refDate)) {
                        // 이미 map 에 해당 일 의 정보가 있을 경우
                        if (dayWalk.isAfter(todo.getRepeatEndDate())) {
                            break;
                        }
                        if (todo.getStartDate().isEqual(dayWalk) || todo.getStartDate().isBefore(dayWalk)) {
                            putInTodoMap(todoMap, dayWalk);
                        }
                        dayWalk = dayWalk.plusWeeks(1);
                    }


                } else {
                    // month 반복일때 로직
                    if(todo.getRepeatEndDate().getYear() == year && todo.getRepeatEndDate().getMonthValue() == month &&
                    todo.getRepeatEndDate().getDayOfMonth() < todo.getRepeatNum()) {
                        return;
                    }
                    if (todoMap.get(todo.getRepeatNum()) != null) {
                        if (todoMap.get(todo.getRepeatNum()).getHasTodo() < 1) {
                            todoMap.get(todo.getRepeatNum()).setHasTodo(1);
                        }
                    } else {
                        todoMap.put(todo.getRepeatNum(), SubEmotionSelVo.builder().hasTodo(1).day(todo.getRepeatNum()).build());
                    }
                }


            } catch (NullPointerException e) {
//                if (firstDayOfMonth.isBefore(todo_.getStartDate())) {
                if (todo.getStartDate().isBefore(firstDayOfMonth)) {
                    // 당월의 1일 (firstDayOfMonth) 부터 endDate 까지 모든 일을 map 에 추가
                    LocalDate dayWalk = LocalDate.of(firstDayOfMonth.getYear(), firstDayOfMonth.getMonth(),
                            firstDayOfMonth.getDayOfMonth());
                    while (true) {
                        putInTodoMap(todoMap, dayWalk);

                        if (dayWalk.isEqual(todo.getEndDate())) {
                            break;
                        }
                        dayWalk = dayWalk.plusDays(1);
                    }

                } else {
                    // startDate 부터 endDate 까지 모든 일을 map 에 추가.
                    LocalDate dayWalk = LocalDate.of(todo.getStartDate().getYear(), todo.getStartDate().getMonth(),
                            todo.getStartDate().getDayOfMonth());
                    while (true) {
                        putInTodoMap(todoMap, dayWalk);
                        log.info("dayTest = {}{}", dayWalk, todo.getEndDate());
                        if (dayWalk.isEqual(todo.getEndDate())) {
                            break;
                        }
                        dayWalk = dayWalk.plusDays(1);
                    }

                }

            }
        });
        EmotionSelProcDto emotionSelProcDto = new EmotionSelProcDto(iuser, String.format("%d-%d-%%", year,
                month));
        log.info("emotionSelProcDto = {}", emotionSelProcDto);
        // emotion 바인딩
        List<EmotionSel> emotionResult = mapper.getEmotionInfo(emotionSelProcDto);
        // map 에 해당 이모션 생성일의 '일' 이 key 로 존재하면 거기에 삽입,
        // 없으면 새로운 객체 만들어서 세팅.

        emotionResult.forEach(emo -> {
            if (todoMap.get(emo.getCreatedAt().getDayOfMonth()) != null) {
                SubEmotionSelVo subEmoInMap = todoMap.get(emo.getCreatedAt().getDayOfMonth());

                subEmoInMap.setEmotionGrade(emo.getEmotionGrade());
                subEmoInMap.setEmotionTag(emo.getEmotionTag());
                if (subEmoInMap.getDay() == 0) {
                    subEmoInMap.setDay(emo.getCreatedAt().getDayOfMonth());
                }

            } else {
                todoMap.put(emo.getCreatedAt().getDayOfMonth(),
                        SubEmotionSelVo.builder().emotionGrade(emo.getEmotionGrade()).emotionTag(emo.getEmotionTag()).day(emo.getCreatedAt().getDayOfMonth()).build());
            }
        });

        return todoMap.values().stream().toList();
    }

    private void putInTodoMap(Map<Integer, SubEmotionSelVo> todoMap, LocalDate dayWalk) {
        if (todoMap.get(dayWalk.getDayOfMonth()) != null) {
            SubEmotionSelVo subEmoInTodo = todoMap.get(dayWalk.getDayOfMonth());
            if (subEmoInTodo.getHasTodo() < 1) {
                subEmoInTodo.setHasTodo(1);
            }
            if (subEmoInTodo.getDay() == 0) {
                subEmoInTodo.setDay(dayWalk.getDayOfMonth());
            }

        } else {
            // map 에 해당 일 의 정보가 없을 경우
            todoMap.put(dayWalk.getDayOfMonth(),
                    SubEmotionSelVo.builder().hasTodo(1).day(dayWalk.getDayOfMonth()).build());
        }
    }

}
