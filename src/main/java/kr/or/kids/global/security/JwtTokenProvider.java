package kr.or.kids.global.security;


import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import kr.or.kids.global.config.util.MessageContextHolder;
import kr.or.kids.global.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.access-token-expiration}")
    private String ACCESS_TOKEN_EXPIRATION;
    
    @Value("${jwt.refresh-token-name}")
    private String REFRESH_TOKEN_NAME;

    public static final String CLAIM_TOKEN_ID = "tokenId";
    public static final String CLAIM_APP_ID   = "appId";

    /**
     * <PRE>
     * AccessToken 생성
     * </PRE>
     * @param userId 회원ID
     * @param expTime(밀리초)
     * @param SECRET_KEY
     * @return
     */
    public String createAccessToken(String issuer, String mbrId, Long expTime, String tokenId, String appId) {
        if(expTime < 0L){
            // 만료시간은 지났습니다.
            throw new ApplicationException(MessageContextHolder.getMessage("ui.token.expired"));
        }

        // 현재시간 기준 이후 ACCESS_TOKEN_EXPIRATION 시간만큼 Access Token 유효시간 설정
        Date expireTime = new Date(System.currentTimeMillis() + expTime);

        // 토큰생성에필요한데이터설정으로토큰생성
        return Jwts.builder()
                .setSubject(mbrId)                                 // userId & 토큰생성주체지정
                .setIssuer(issuer)                                  // 토큰 발행자(kids_user)
                .claim(CLAIM_TOKEN_ID, tokenId)                     // ✅ 추가
                .claim(CLAIM_APP_ID, appId)                         // ✅ 추가(원하면 생략 가능)
                .setIssuedAt(new Date())                            // Access Token 발행 시간
                .setExpiration(expireTime)                          // 만료 시간 설정& compact to String
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)     // key, key 암호화알고리즘설정
                .compact();
    }

    /**
     * <PRE>
     * RefreshToken 생성
     * </PRE>
     * @param expTime(밀리초)
     * @return
     */
    public String createRefreshToken(String issuer, String mbrId, Long expTime) {
        if(expTime < 0L){
            // 만료시간은 지났습니다.
            throw new ApplicationException(MessageContextHolder.getMessage("ui.token.expired"));
        }

     // 현재시간 기준 이후 ACCESS_TOKEN_EXPIRATION 시간만큼 Access Token 유효시간 설정
        Date expireTime = new Date(System.currentTimeMillis() + expTime);

        // 토큰생성에필요한데이터설정으로토큰생성
        return Jwts.builder()
                .setSubject(mbrId)                                  // userId & 토큰생성주체지정
                .setIssuer(issuer)                                  // 토큰 발행자(kids_user)
//                .setSubject(REFRESH_TOKEN_NAME)                     // 토큰 생성 주체지정
                .setIssuedAt(new Date())                            // Refresh Token 발행 시간
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)     // key, key 암호화알고리즘설정
                .compact();                                         // 만료 시간 설정& compact to String
    }

    /**
     * JWT 토큰 문자열에서 토큰데이터를 가져오는 메소드
     * @param accessToken
     * @return
     */
    public Claims getClaims(String token){
        if(!"Y".equals(this.validateToken(token))){
            // 유효하지 않은 JWT 토큰
            throw new ApplicationException(MessageContextHolder.getMessage("ui.token.invalid"));
        }

        if(StringUtils.hasLength(token) && token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        // 토큰 파싱
        Claims claims = parseClaims(token);
        return claims;
    }

    /**
     * JWT 토큰 문자열에서 회원ID(mbrId) 가져오는 메소드
     * @param token - Access Token 혹은 Refresh Token
     * @return Access Token의 토큰정보로부터 회원ID(mbrId)를 반환
     */
    public String getSubject(String token){
        if(!"Y".equals(this.validateToken(token))){
            // 유효하지 않은 JWT 토큰
            throw new ApplicationException(MessageContextHolder.getMessage("ui.token.invalid"));
        }

        if(StringUtils.hasLength(token) && token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        // 토큰 파싱
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    // 토큰 유효성검증메소드
    public String validateToken(String token){
        String returnValue = "";
        try {

            if(StringUtils.hasLength(token) && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return "Y";
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            returnValue = "Invalid JWT Token";
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
            returnValue = "Expired JWT Token";
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            returnValue = "Unsupported JWT Token";
        } catch (IllegalArgumentException e) {
//            log.info("JWT Claims string is empty", e);
            returnValue = "JWT Claims string is empty";
        }
        return returnValue;
    }

    /**
     * 토큰 파싱
     * @param accessToken
     * @return
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * JWT 토큰의 만료시간 얻기
     * @param token -  AccessToken
     * @return 만료시간(milliseconds)
     */
    public long getRemainingMillis(String token) {
        Claims claims = getClaims(token); // 내부에서 Bearer 제거/validate 수행 :contentReference[oaicite:7]{index=7}
        Date exp = claims.getExpiration();
        return exp.getTime() - System.currentTimeMillis();
    }

    public String getTokenId(String token) {
        Claims claims = getClaims(token);
        Object v = claims.get(CLAIM_TOKEN_ID);
        return v == null ? null : String.valueOf(v);
    }

    public String getAppId(String token) {
        Claims claims = getClaims(token);
        Object v = claims.get(CLAIM_APP_ID);
        return v == null ? null : String.valueOf(v);
    }
}
