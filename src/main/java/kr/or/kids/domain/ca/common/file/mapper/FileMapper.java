package kr.or.kids.domain.ca.common.file.mapper;

import kr.or.kids.domain.ca.common.file.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    /**  파일 목록 조회 */
    public List<FileDataResVO> list(FileDataReqVO param);
    /**  파일 단건 조회 */
    public FileDataResVO data(FileDataReqVO param);
    /**  파일 일련번호 조회 */
    public long nextFileId();
    /**  파일 파일정보 등록 */
    public int insert(FileInsertReqVO param);
    /**  파일 단건 수정 */
    public int update(FileInsertReqVO param);
    /**  파일 단건 삭제 */
    public int deleteOne(FileDeleteReqVO param);
    /**  파일 다건 삭제 */
    public int delete(FileDeleteReqVO param);
    /**  파일 그룹정보 삭제 */
    public int deleteGroup(FileDeleteReqVO param);
    /**  파일 그룹  일련번호 조회 */
    public long nextFileGroupId();
    /**  파일 그룹  일련번호 조회 */
    public long getLastFileGroupId();
        /**  파일 그룹  데이터조회 */
    public FileGroupResData groupData(FileGroupReqData param);
    /**  파일 그룹  정보 등록 */
    public void groupInsert(FileGroupInsertReq param);
    /**  파일 그룹  정보 수정 */
    public void groupUpdate(FileGroupInsertReq param);
    /** 파일 다운로드 사유 로그 등록 */
    public int insertDownloadLog(FileDownloadLogReqVO param);
    /** 파일 정보에서 업무단 개인정보 여부 조회 */
    public FileMetaVO selectFileMetaByFilename(String filename);
}
