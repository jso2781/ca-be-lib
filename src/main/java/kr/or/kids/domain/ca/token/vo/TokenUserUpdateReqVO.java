package kr.or.kids.domain.ca.token.vo;

import lombok.Data;

/**
 * 토큰 사용자수정 요청 VO
 */
@Data
public class TokenUserUpdateReqVO {

    private String tokenId;         // 토큰 ID
    private Long adminId;        // 관리자 번호
    private Long userId;         // 사용자번호
    private String appId;           // 앱ID
    private String refreshToken;    // 리프레시 토큰
    private String accessToken;     // 액세스토큰
    private Integer upAdminId;      // 수정 관리자 번호

}
