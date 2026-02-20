package kr.or.kids.domain.ca.common.file.vo;
import lombok.Data;

@Data
public class FileUpdateReqVO {

    private String atchFileId;          // 파일정보 일련번호
    private String atchFileGroupId;     // 파일 그룹정보 일련번호
    private Integer fileSeq;          // 파일 순서
    private String fileStrgPathDsctn; // 파일 저장 경로
    private String srvrFileNm;        // 파일 인코딩 명
    private String fileNm;            // 파일 명
    private String fileExtnNm;        // 파일 확장자명
    private String fileCn;            // 파일 컨텐츠
    private Long fileSz;              // 파일 사이즈 정보
    private String crtDt;             // 파일 등록일
    private String useYn;             // 파일 사용여부
    private String regDt;             // 파일 정보 등록일시
    private String rgtrId;            // 파일 등록자 ID
    private String mdfcnDt;           // 파일 정보 수정일시
    private String mdfrId;            // 파일 수정자 ID

}
