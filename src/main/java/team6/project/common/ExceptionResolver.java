package team6.project.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team6.project.common.exception.*;
import team6.project.common.model.ExceptionResultVo;

import java.sql.SQLSyntaxErrorException;

import static team6.project.common.Const.*;

@Slf4j
@RestControllerAdvice
public class ExceptionResolver {


    @ExceptionHandler
    public ExceptionResultVo noSuchDataException(NoSuchDataException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(NO_SUCH_DATA_EX_MESSAGE);
    }

    @ExceptionHandler
    public ExceptionResultVo badDateInformationException(BadDateInformationException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(BAD_DATE_INFO);
    }

    @ExceptionHandler
    public ExceptionResultVo badInformationException(BadInformationException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(BAD_REQUEST);
    }

    @ExceptionHandler
    public ExceptionResultVo todoIsFullException(TodoIsFullException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(TODO_IS_FULL_EX_MESSAGE);
    }

    @ExceptionHandler
    public ExceptionResultVo methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("message = {}", e.getMessage(), e);
        StringBuilder sb = new StringBuilder();
        String separator = ", ";
        try {
//            return new ExceptionResultVo(e.getFieldError().getDefaultMessage());
            e.getFieldErrors().forEach(ex -> {
                sb.append(ex.getDefaultMessage());
                sb.append(separator);
            });
            String result = sb.toString();
            return new ExceptionResultVo(result.substring(0, result.length() - separator.length()));

        } catch (NullPointerException npe) {
            log.info("검증코드에 message 속성 추가 필수");
            return new ExceptionResultVo(BAD_REQUEST);
        }
    }

    @ExceptionHandler
    public ExceptionResultVo myMethodArgumentNotValidException(MyMethodArgumentNotValidException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    public ExceptionResultVo httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info("message = {}", e.getMessage());
        return new ExceptionResultVo("잘못된 요청 타입");
    }

    /**
     *
     * 만약 SQLSyntaxErrorException 발생시 해당 메소드 또는 어노테이션 주석 후 쿼리문 확인.
     *
     */
    @ExceptionHandler
    public ExceptionResultVo sqlSyntaxErrorException(SQLSyntaxErrorException e) {
        e.printStackTrace();
        return new ExceptionResultVo("알 수 없는 오류로 실행 불가능");
    }
}
