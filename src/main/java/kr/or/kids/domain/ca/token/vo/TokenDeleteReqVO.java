package kr.or.kids.domain.ca.token.vo;

import lombok.Data;

/**
 * 토큰 삭제 요청 VO
 */
@Data
public class TokenDeleteReqVO {
    private String tokenId;         // 토큰 ID
}