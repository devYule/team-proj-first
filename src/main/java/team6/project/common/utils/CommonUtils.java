package team6.project.common.utils;

import org.springframework.stereotype.Component;
import team6.project.common.exception.BadInformationException;
import team6.project.common.exception.NotEnoughInformationException;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import static team6.project.common.Const.*;
import static team6.project.common.Const.NOT_ENOUGH_INFO_EX_MESSAGE;

@Component
public class CommonUtils {
    public Integer fromJavaTo(int target) {
        return target + 2 >= 7 ? target + 2 - 7 : target + 2;
    }

    public Integer toJavaFrom(int target) {
        return target + 5 > 7 ? target + 5 - 7 : target + 5;
    }

    public void checkObjectIsNotNullThrow(Class<? extends RuntimeException> ex, String message, Object... objs) {
        // 제공된 모든 객체가 모두 null 이면 ex에 제공한 예외에 message 를 담아서 throw
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
    public void checkObjectIsNullThrow(Class<? extends RuntimeException> ex, String message, Object... objs) {
        // 제공된 모든 객체가 모두 null 이면 ex에 제공한 예외에 message 를 담아서 throw
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
    public void checkRepeatNumWithRepeatType(LocalDate repeatEndDate, String repeatType, Integer repeatNum) {
        if (checkObjectIsNotNull(repeatEndDate, repeatType, repeatNum)) {
            checkObjectIsNotNullThrow(NotEnoughInformationException.class,
                    NOT_ENOUGH_INFO_EX_MESSAGE,
                    repeatType, repeatNum);
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
    }

    public boolean checkObjectIsNotNull(Object... objs) {
        for (Object obj : objs) {
            if (obj != null) {
                return true;
            }
        }
        return false;
    }
}
