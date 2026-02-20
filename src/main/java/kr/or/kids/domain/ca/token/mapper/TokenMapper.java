package kr.or.kids.domain.ca.token.mapper;

import kr.or.kids.domain.ca.token.vo.*;
import kr.or.kids.domain.ca.token.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TokenMapper {

    /**  토근 목록 조회 */
    List<TokenListResVO> list();
    /**  토근 단건 조회 */
    TokenDataResVO data(TokenUserIdReqVO param);
    /**  토근 단건 등록 */
    void insert(TokenInsertReqVO param);
    /**  토근 사용자 수정 */
    int userUpdate(TokenUserUpdateReqVO param);
    /**  토근 관리자 수정 */
    int adminUpdate(TokenUserUpdateReqVO param);
    /**  토근 정보 삭제 */
    int delete(TokenDeleteReqVO param);

}
