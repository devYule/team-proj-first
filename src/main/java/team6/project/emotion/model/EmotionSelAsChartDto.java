package team6.project.emotion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Name;
import lombok.Data;

@Data
@Schema(name = "주간 이모션 조회",title = "주간 이모션 조회")
public class EmotionSelAsChartDto {
    @Schema(title = "유저 PK", minimum = "1", type = "int", description = "필수값", defaultValue = "유저PK")
    private int iuser;
    @Schema(title = "해당하는 주",description = "주단위로 이모션을 조회하기위한 주")
    private String startWeek;
    @Schema(title = "오늘 날짜",description = "주단위로 이모션을 조회하기위해 필요한 오늘의 날짜")
    private String endWeek;
}
