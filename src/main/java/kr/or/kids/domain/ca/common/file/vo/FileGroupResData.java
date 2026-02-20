package kr.or.kids.domain.ca.common.file.vo;
import lombok.Data;

@Data
public class FileGroupResData {

    private String atchFileGroupId;  // 파일 그룹정보 일련번호
    private Integer fileSeq;       // 파일 순서 번호
    private String taskSeCd;       // 파일 업무구분코드
    private String taskSeTrgtId;   // 파일 업무 구분 일련번호
    private String prvcInclYn;     // 개인정보 여부
    private String useYn;          // 파일 정보 사용여부
    private String regDt;          // 파일 등록일시
    private String rgtrId;         // 파일 정보 등록자 ID
    private String mdfcnDt;        // 파일 수정일시
    private String mdfrId;         // 파일정보 수정자 ID

}
