package team6.project.todo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team6.project.common.ResVo;
import team6.project.common.exception.BadDateInformationException;
import team6.project.todo.model.PatchTodoDto;
import team6.project.todo.model.TodoRegDto;
import team6.project.todo.model.proc.InsRepeatInfoDto;
import team6.project.todo.model.proc.InsTodoDto;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoMapper mapper;


    @Transactional
    public ResVo regTodo(TodoRegDto dto) {
        // TODO 아직 예외처리 X
        if (dto.getEndDate().isBefore(dto.getStartDate()) ||
                dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new BadDateInformationException("badDateInfo");
        }

        try {
            if (!dto.getRepeatType().equalsIgnoreCase("week") &&
                    !dto.getRepeatType().equalsIgnoreCase("month")) {
                throw new BadDateInformationException("badDateInfo");
            }
            // 반복 있을때 로직
            log.debug("todo service in try");
            InsTodoDto insTodoDto = new InsTodoDto(dto);
            mapper.insTodo(insTodoDto);
            InsRepeatInfoDto insRepeatInfoDto = new InsRepeatInfoDto(dto, insTodoDto.getItodo());
            mapper.insRepeat(insRepeatInfoDto);
            return new ResVo(insRepeatInfoDto.getItodo());

        } catch (NullPointerException e) {
            // 반복 없을때 로직
            log.debug("todo service in catch");
            InsTodoDto insTodoDto = new InsTodoDto(dto);
            mapper.insTodo(insTodoDto);
            return new ResVo(insTodoDto.getItodo());
        }
    }

    public ResVo patchTodo(PatchTodoDto dto) {
        return null;
    }

    public ResVo deleteTodo(Integer iuser, Integer itodo) {
        return new ResVo(mapper.deleteTodo(iuser, itodo));
    }
}
