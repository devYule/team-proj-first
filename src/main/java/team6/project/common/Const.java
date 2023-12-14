package team6.project.common;

public interface Const {
    /*
    예외 메시지
     */
    /* TODO: 12/13/23
        코드로 변경, 프론트와 약속
        --by Hyunmin */
    String BAD_REQUEST_EX_MESSAGE = "잘못된 요청";
    String BAD_INFO_EX_MESSAGE = "잘못된 요청 데이터";
    String BAD_DATE_INFO_EX_MESSAGE = "잘못된 날짜 정보 입력";
    String BAD_TIME_INFO_EX_MESSAGE = "잘못된 시간 정보 입력";
    String NO_SUCH_DATA_EX_MESSAGE = "해당하는 데이터 없음";
    String TODO_IS_FULL_EX_MESSAGE = "Todo 는 10개 이상 등록할 수 없음";
    String TODO_SAVE_FAIL_EX_MESSAGE = "투두 저장 실패";
    String REPEAT_SAVE_FAIL_EX_MESSAGE = "반복정보 저장 실패";
    String NOT_ENOUGH_INFO_EX_MESSAGE = "제공된 데이터가 충분하지 않음";
    String BAD_REQUEST_TYPE_EX_MESSAGE = "잘못된 요청 타입";
    String RUNTIME_EX_MESSAGE = "알 수 없는 오류로 실패";


    String WEEK = "week";
    String MONTH = "month";
    Integer FIRST_DAY = 1;
    Integer TODO_SELECT_FROM_NUM = 0;
    Integer TODO_SELECT_TO_NUM = 10;
    Integer PLUS_ONE_MONTH_OR_WEEK_OR_DAY = 1;
    Integer TODO_MAX_SIZE = 10;


    // hyesun
    String NICK_NAME_RANGE_EX_MESSAGE = "닉네임은 1자 이상 10자 이하";
}
