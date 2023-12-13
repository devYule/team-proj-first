package team6.project.common.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class CommonUtils {
    public Integer fromJavaTo(int target) {
        return target + 2 >= 7 ? target + 2 - 7 : target + 2;
    }

    public Integer toJavaFrom(int target) {
        return target + 5 > 7 ? target + 5 - 7 : target + 5;
    }

    public void checkObjectIsNull(Class<? extends RuntimeException> ex, String message, Object... objs) {
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

    public boolean checkObjectIsNotNull(Object... objs) {
        for (Object obj : objs) {
            if (obj != null) {
                return true;
            }
        }
        return false;
    }
}
