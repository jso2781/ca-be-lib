package kr.or.kids.domain.ca.token.vo;

import lombok.Data;

/**
 * 토큰 데이터조회 결과 VO
 */
@Data
public class TokenDataResVO {
    private String tokenId;         // 토큰 ID
    private Integer adminId;        // 관리자 번호
    private Integer userId;         // 사용자번호
    private String appId;           // 앱ID
    private String refreshToken;    // 리프레시 토큰
    private String accessToken;     // 액세스토큰
    private Integer inAdminId;      // 등록 관리자 번호
    private String inDatetime;      // 등록 시간
    private Integer upAdminId;      // 수정 관리자 번호
    private String upDatetime;      // 수정 시간
}