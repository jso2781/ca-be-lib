package kr.or.kids.domain.ca.common.file.vo;

import lombok.Data;
@Data
public class FileDataReqVO {
    private String atchFileId;    // 파일정보 일련버호
    private String srvrFileNm;     // 파일 인코딩명
    private String atchFileGroupId;  // 파일 그룹정보 일련번호
    private String taskSeCd;       // 파일 업무구분코드
    private String taskSeTrgtId;   // 파일 업무 구분 일련번호
}