package team6.project.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team6.project.common.exception.*;
import team6.project.common.model.ExceptionResultVo;

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
        try {
            return new ExceptionResultVo(e.getFieldError().getDefaultMessage());
        }catch (NullPointerException npe){
            log.info("검증코드에 message 속성 추가 필수");
            return new ExceptionResultVo(BAD_REQUEST);
        }
    }

    @ExceptionHandler
    public ExceptionResultVo myMethodArgumentNotValidException(MyMethodArgumentNotValidException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }
}
