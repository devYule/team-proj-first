package team6.project.common.utils;

import org.springframework.stereotype.Component;
import team6.project.common.exception.BadDateInformationException;
import team6.project.common.exception.BadInformationException;
import team6.project.common.exception.NotEnoughInformationException;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static team6.project.common.Const.*;

@Component
public class CommonUtils {
    /**
     * JAVA 요일 표기법 -> JS 요일 표기법
     *
     * @param target
     * @return JS 요일 표기법
     */
    public Integer fromJavaTo(int target) {
        return target + 2 >= 7 ? target + 2 - 7 : target + 2;
    }

    /**
     * JS 요일 표기법 -> JAVA 요일 표기법
     *
     * @param target
     * @return JAVA 요일 표기법
     */
    public Integer toJavaFrom(int target) {
        return target + 5 > 7 ? target + 5 - 7 : target + 5;
    }

    /**
     * 제공된 모든 객체가 모두 null 이면 ex에 제공한 예외에 message 를 담아서 throw
     *
     * @param ex      - throw 할 예외 타입
     * @param message - 예외 생성시 담을 메시지
     * @param objs    - 검증할 객체들
     */
    public void checkObjectAllNullThrow(Class<? extends RuntimeException> ex, String message, Object... objs) {
        for (Object obj : objs) {
            if (obj != null) {
                return;
            }
        }
        try {
            throw ex.getDeclaredConstructor(String.class).newInstance(message);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 제공된 모든 객체가 하나라도 null 이 아니면 ex에 제공한 예외에 message 를 담아서 throw
     *
     * @param ex      - throw 할 예외 타입
     * @param message - 예외 생성시 담을 메시지
     * @param objs    - 검증할 객체들
     */
    public void checkObjectAnyNotNullThrow(Class<? extends RuntimeException> ex, String message, Object... objs) {
        for (Object obj : objs) {
            if (obj != null) {
                try {
                    throw ex.getDeclaredConstructor(String.class).newInstance(message);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 제공된 모든 객체가 하나라도 null 이면 ex에 제공한 예외에 message 를 담아서 throw
     *
     * @param ex      - throw 할 예외 타입
     * @param message - 예외 생성시 담을 메시지
     * @param objs    - 검증할 객체들
     */
    public void checkObjectAnyNullThrow(Class<? extends RuntimeException> ex, String message, Object... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                try {
                    throw ex.getDeclaredConstructor(String.class).newInstance(message);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * repeatEndDate 가 null 이 아닌경우, repeatType 와 repeatNum 모두 null 이 아니여야 한다.
     * 위반시 NotEnoughInformationException 을 throw
     *
     * @param repeatEndDate - null 이 아닐경우
     * @param repeatType    - null 일 수 없다.
     * @param repeatNum     - null 일 수 없다.
     */
    public void checkRepeatInfo(LocalDate repeatEndDate, String repeatType, Integer repeatNum) {
        if (repeatEndDate != null) {
            if (repeatType == null || repeatNum == null) {
                throw new NotEnoughInformationException(NOT_ENOUGH_INFO_EX_MESSAGE);
            }
        }
    }

    /**
     * repeatType 이 week 일 경우 repeatNum 은 1 ~ 7 사이 (JAVA 요일 표기법 기준),
     * repeatType 이 month 일 경우 repeatNum 은 1 ~ 31 사이
     * 인지 검증.
     *
     * @param repeatType - week OR month
     * @param repeatNum  - 1 ~ 7 OR 1 ~ 31
     */
    public void checkRepeatNumWithRepeatType(String repeatType, Integer repeatNum) {
        if (repeatType.equalsIgnoreCase(WEEK)) {
            if (repeatNum < 1 || repeatNum > 7) {
                throw new BadInformationException(BAD_INFO_EX_MESSAGE);
            }
        } else if (repeatType.equalsIgnoreCase(MONTH)) {
            if (repeatNum < 1 || repeatNum > 31) {
                throw new BadInformationException(BAD_INFO_EX_MESSAGE);
            }
        } else {
            throw new NotEnoughInformationException(NOT_ENOUGH_INFO_EX_MESSAGE);
        }
    }

    /**
     * 제공된 모든 객체가 모두 null 이면 false, 하나라도 null 이 아니면 true 를 리턴한다.
     *
     * @param objs - 검증할 객체들
     * @return 모두 null - false , 하나라도 not null - true
     */
    public boolean checkObjectAnyNotNull(Object... objs) {
        for (Object obj : objs) {
            if (obj != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * endDateTime 에 제공된 날짜가 startDateTime 에 제공된 날짜보다 이전이면 예외 throw
     *
     * @param endDateTime   - 이후
     * @param startDateTime - 이전
     */
    public void checkIsBefore(LocalDateTime endDateTime, LocalDateTime startDateTime) {
        if (endDateTime.isBefore(startDateTime)) {
            throw new BadDateInformationException(BAD_DATE_INFO_EX_MESSAGE);
        }
    }

    /**
     * 반복이 있을 경우 검증 로직
     * 주 반복일경우 startDateTime 이 주 반복 숫자와 동일한 숫자인지 체크,
     * 월 반복일경우 startDateTime 이 월 반복 숫자(일) 와 동일한 숫자인지 체크.
     *
     * @param endDateTime   - 일정 종료 날짜 + 시간
     * @param startDateTime - 일정 시작 날짜 + 시간
     * @param repeatType    - 반복 종류
     * @param repeatNum     - 반복 숫자
     */
    public void checkIsBefore(LocalDateTime endDateTime, LocalDateTime startDateTime,
                              String repeatType, Integer repeatNum) {
        if (endDateTime.isEqual(startDateTime) || endDateTime.isAfter(startDateTime)) {
            if (repeatType.equalsIgnoreCase(WEEK)) {
                if (startDateTime.getDayOfWeek().getValue() == repeatNum) {
                    return;
                }
            }
            if (repeatType.equalsIgnoreCase(MONTH)) {
                if (startDateTime.getDayOfMonth() == repeatNum) {
                    return;
                }
            }
        }
        throw new BadInformationException(BAD_INFO_EX_MESSAGE);
    }
}
