package team6.project.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    public ExceptionResultVo badDateInformationException(BadDateInformationException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    public ExceptionResultVo badInformationException(BadInformationException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    public ExceptionResultVo todoIsFullException(TodoIsFullException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    public ExceptionResultVo methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("message = {}", e.getMessage(), e);
        StringBuilder sb = new StringBuilder();
        String separator = ", ";

        e.getFieldErrors().forEach(ex -> {
            sb.append(ex.getDefaultMessage());
            sb.append(separator);
        });
        String result = sb.toString();
        return new ExceptionResultVo(result.substring(0, result.length() - separator.length()));

    }

    @ExceptionHandler
    public ExceptionResultVo myMethodArgumentNotValidException(MyMethodArgumentNotValidException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    public ExceptionResultVo httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    public ExceptionResultVo notEnoughInformationException(NotEnoughInformationException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }


    //
    @ExceptionHandler
    public ExceptionResultVo runtimeException(RuntimeException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }
}
