package team6.project.common.utils;

import org.springframework.stereotype.Component;

@Component
public class WeekFormatResolver {
    public Integer fromJavaTo(int target) {
        return target + 2 >= 7 ? target + 2 - 7 : target + 2;
    }

    public Integer toJavaFrom(int target) {
        return target + 5 > 7 ? target + 5 - 7 : target + 5;
    }
}
