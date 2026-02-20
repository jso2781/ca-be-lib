package kr.or.kids.domain.ca.common.file.vo;
import lombok.Data;

@Data
public class FileGroupReqData {

    private String atchFileGroupId;  // 파일 그룹정보 일련번호
    private Integer fileSeq;       // 파일 순서 번호
    private String taskSeCd;       // 파일 업무구분코드
    private String taskSeTrgtId;   // 파일 업무 구분 일련번호

}
