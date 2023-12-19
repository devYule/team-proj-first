package team6.project.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team6.project.common.Const;

import static team6.project.common.Const.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResultVo {
    @Schema(title = "요청 결과(에러 발생시)", type = "String", description = "에러문구 리턴", defaultValue = BAD_REQUEST_EX_MESSAGE)
    private String message;
    @Schema(title = "요청 결과(에러 발생시)", type = "Integer", description = "에러코드 리턴", defaultValue = "1")
    private Integer messageCode;

    public ExceptionResultVo(String message) {
        this.message = message;
        if (this.message.equals(BAD_REQUEST_EX_MESSAGE)) {
            this.messageCode = 1001;
        }
        if (this.message.equals(BAD_INFO_EX_MESSAGE)) {
            this.messageCode = 1002;
        }
        if (this.message.equals(BAD_DATE_INFO_EX_MESSAGE)) {
            this.messageCode = 1003;
        }
        if (this.message.equals(NO_SUCH_DATA_EX_MESSAGE)) {
            this.messageCode = 2001;
        }
        if (this.message.equals(TODO_IS_FULL_EX_MESSAGE)) {
            this.messageCode = 3001;
        }
        if (this.message.equals(NOT_ENOUGH_INFO_EX_MESSAGE)) {
            this.messageCode = 1004;
        }
        if (this.message.equals(BAD_REQUEST_TYPE_EX_MESSAGE)) {
            this.messageCode = 4001;
        }
        if (this.message.equals(RUNTIME_EX_MESSAGE)) {
            this.messageCode = 5001;
        }
    }
}
