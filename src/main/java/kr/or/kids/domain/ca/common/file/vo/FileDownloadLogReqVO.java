package kr.or.kids.domain.ca.common.file.vo;
import lombok.Data;

@Data
public class FileDownloadLogReqVO {

    private String cntnLogSn;      // 접속행위일련번호
    private String acsrId;         // 접속자 ID
    private String trgtMenuNm;     // 대상 메뉴명
    private String qna;            // 다운로드 사유
    private String urlAddr;        // 요청 URL
    private String prvcInclYn;     // 개인정보 포함 여부 (Y/N)
    private String rgtrId;         // 등록자 ID
    private Long atchFileId;       // 파일정보 일련번호

}