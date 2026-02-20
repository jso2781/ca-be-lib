package kr.or.kids.global.system.common.vo;

import kr.or.kids.global.system.common.ApiResultCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@Setter
@ToString
public class ApiPrnDto {

    private String code;
    private String msg;
    private HashMap<String, Object> data = new HashMap<>();

    public ApiPrnDto() {
        // 기본 생성자
    }

    public ApiPrnDto(ApiResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();
    }

    public static ApiPrnDto success(HashMap<String, Object> data) {
        ApiPrnDto dto = new ApiPrnDto(ApiResultCode.SUCCESS);
        dto.setData(data);
        return dto;
    }

    public static ApiPrnDto fail(ApiResultCode resultCode) {
        return new ApiPrnDto(resultCode);
    }

}
