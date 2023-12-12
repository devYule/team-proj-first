package team6.project.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team6.project.common.exception.BadDateInformationException;
import team6.project.common.exception.BadInformationException;
import team6.project.common.exception.NoSuchDataException;
import team6.project.common.exception.TodoIsFullException;
import team6.project.common.model.ExceptionResultVo;

import static team6.project.common.Const.*;

@RestControllerAdvice
public class ExceptionResolver {


    @ExceptionHandler
    public ExceptionResultVo noSuchDataException(NoSuchDataException e) {
        return new ExceptionResultVo(NO_SUCH_DATA_EX_MESSAGE);
    }

    @ExceptionHandler
    public ExceptionResultVo badDateInformationException(BadDateInformationException e) {
        return new ExceptionResultVo(BAD_DATE_INFO);
    }

    @ExceptionHandler
    public ExceptionResultVo badInformationException(BadInformationException e) {
        return new ExceptionResultVo(BAD_REQUEST);
    }

    @ExceptionHandler
    public ExceptionResultVo todoIsFullException(TodoIsFullException e) {
        return new ExceptionResultVo(TODO_IS_FULL_EX_MESSAGE);
    }

}
