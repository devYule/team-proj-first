package team6.project.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "요청 결과")
public class ResVo {
    @Schema(title = "요청 결과", type = "Integer", description = "성공시 1 리턴", defaultValue = "1")
    private int result;
}
