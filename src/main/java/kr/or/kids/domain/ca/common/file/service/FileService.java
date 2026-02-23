package kr.or.kids.domain.ca.common.file.service;


import kr.or.kids.domain.ca.common.file.vo.*;
import kr.or.kids.global.system.common.vo.ApiPrnDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

/**
 * 파일 서비스 인터페이스
 */
public interface FileService {

    /**
     * 파일 목록
     * @return API 응답 DTO
     */
    ApiPrnDto list(FileDataReqVO param, int pageNum, int  pageSize);

    /**
     * 파일 업로드
     * @param savePath 저장 경로
     * @param uploadFiles 업로드할 파일 배열
     * @return API 응답 DTO
     */
    ApiPrnDto uploadFiles(HashMap<String, Object> param, MultipartFile[] uploadFiles);

    /**
     * 파일 단건석제
     * @return API 응답 DTO
     */
    ApiPrnDto deleteFileOne(FileDeleteReqVO param);

    /**
     * 파일 단건석제
     * @return API 응답 DTO
     */
    ApiPrnDto deleteMultiFile(FileDeleteReqVO param);

    /**
     * 파일 일괄 석제
     * @return API 응답 DTO
     */
    ApiPrnDto deleteGroupFiles(FileDeleteReqVO param);

    /**
     * 파일 그룹정보 단건조회
     * @return API 응답 DTO
     */
    public ApiPrnDto groupData(FileGroupReqData fileGroupReqData);

    /**
     * 파일 그룹정보 등록
     * @return API 응답 DTO
     */
    ApiPrnDto groupInsert(FileGroupInsertReq param);
    /**
     * 그룹정보 수정
     * @return API 응답 DTO
     */
    ApiPrnDto groupUpdate(FileGroupInsertReq param);
    /**
     * 파일 그 파일 다운로드  등록
     * @return API 응답 DTO
     */
    void saveDownloadLog(FileDownloadLogReqVO param);

    /**
     * 파일 다운로드 (OutputStream 방식 - 대용량 파일에 적합)
     * @param filename 다운로드할 암호화된 파일명
     * @param response HttpServletResponse
     */
    void downloadStream(String filename, HttpServletResponse response);

	/**
	 * 파일 다운로드 정보 객체 반환
	 * @param paramVo 첨부파일ID(atchFileId), 암호화된 파일명(srvrFileNm)으로 파일 정보 검색 파라메터 VO
	 * @return 파일 다운로드 정보 객체 반환
	 */
	FileDownResVO downloadFile(FileDataReqVO paramVo);
}
