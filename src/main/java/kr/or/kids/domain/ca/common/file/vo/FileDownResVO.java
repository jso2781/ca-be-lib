package kr.or.kids.domain.ca.common.file.vo;

import org.springframework.core.io.Resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(name = "파일 다운로드 정보", description = "파일 다운로드 정보 Result VO")
public class FileDownResVO {

    /** 첨부파일ID */
    private String atchFileId;

    /** 파일명 */
    private String filename;

    /** 컨텐츠 타입 */
    private String contentType;

    /** 컨텐츠 길이 */
    private long contentLength;

    /** 파일 리소스 */
    private Resource resource;
}