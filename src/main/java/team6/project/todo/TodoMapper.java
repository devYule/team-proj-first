package team6.project.todo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team6.project.todo.model.proc.InsRepeatInfoDto;
import team6.project.todo.model.proc.InsTodoDto;

@Mapper
public interface TodoMapper {

    void insTodo(InsTodoDto dto);

    void insRepeat(InsRepeatInfoDto dto);

    int deleteTodo(@Param("iuser") Integer iuser, @Param("itodo") Integer itodo);
}
