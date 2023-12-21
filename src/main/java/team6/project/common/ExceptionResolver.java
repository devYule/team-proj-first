package team6.project.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team6.project.common.exception.*;
import team6.project.common.model.ExceptionResultVo;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import static team6.project.common.Const.*;

@Slf4j
@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResultVo noSuchDataException(NoSuchDataException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResultVo badDateInformationException(BadDateInformationException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResultVo badInformationException(BadInformationException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResultVo todoIsFullException(TodoIsFullException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResultVo notEnoughInformationException(NotEnoughInformationException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResultVo myMethodArgumentNotValidException(MyMethodArgumentNotValidException e) {
        log.info("message = {}", e.getMessage(), e);
//        return new ExceptionResultVo(BAD_REQUEST_TYPE_EX_MESSAGE);
        return new ExceptionResultVo(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResultVo httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info("message = {}", e.getMessage(), e);
        if (e.getMessage().contains("java.time.LocalDate: (java.time.format.DateTimeParseException)")) {
            return new ExceptionResultVo(BAD_DATE_INFO_EX_MESSAGE);
        }
        if (e.getMessage().contains("java.time.LocalTime: (java.time.format.DateTimeParseException)")) {
            return new ExceptionResultVo(BAD_TIME_INFO_EX_MESSAGE);
        }

        return new ExceptionResultVo(BAD_REQUEST_TYPE_EX_MESSAGE);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResultVo dateTimeException(DateTimeException e) {
        return new ExceptionResultVo(BAD_DATE_INFO_EX_MESSAGE);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResultVo runtimeException(RuntimeException e) {
        log.info("message = {}", e.getMessage(), e);
        return new ExceptionResultVo(e.getMessage());
    }
}
