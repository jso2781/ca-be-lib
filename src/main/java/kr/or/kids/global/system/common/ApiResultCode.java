package kr.or.kids.global.system.common;

import org.springframework.http.HttpStatus;

public enum ApiResultCode {

    SUCCESS("0", "성공", HttpStatus.OK),
    SYSTEM_ERROR("-1", "시스템 오류", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR("100", "유효성 검증 실패", HttpStatus.BAD_REQUEST),
    DUPLICATE_ERROR("200", "중복 데이터", HttpStatus.CONFLICT),
    AUTH_ERROR("300", "권한 없음", HttpStatus.FORBIDDEN),
    NOT_FOUND("400", "데이터 없음", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("401", "권한 없음", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ApiResultCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getHttpStatus() { return httpStatus; }

    public static ApiResultCode fromCode(String code) {
        for(ApiResultCode rc : values()) {
            if(rc.code.equals(code)) return rc;
        }
        return SYSTEM_ERROR; // 기본값
    }


}
