package team6.project.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team6.project.common.Const;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResultVo {
    /* TODO: 2023-12-15  
        에러코드 설정 후 private String message 는 삭제 (@Schema 도)
        --by Hyunmin */
    @Schema(title = "요청 결과(에러 발생시)", type = "String", description = "에러문구 리턴", defaultValue = Const.BAD_REQUEST_EX_MESSAGE)
    private String message;
    @Schema(title = "요청 결과(에러 발생시)", type = "Integer", description = "에러코드 리턴", defaultValue = "1")
    private Integer messageCode;

    public ExceptionResultVo(String message) {
        this.message = message;
    }
}
