package kr.or.kids.domain.ca.common.file.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import kr.or.kids.domain.ca.common.file.mapper.FileMapper;
import kr.or.kids.domain.ca.common.file.service.FileCryptoService;
import kr.or.kids.domain.ca.common.file.service.FileService;
import kr.or.kids.domain.ca.common.file.vo.*;
import kr.or.kids.global.config.FileProperties;
import kr.or.kids.global.system.common.ApiResultCode;
import kr.or.kids.global.system.common.vo.ApiPrnDto;
import kr.or.kids.global.util.DrugsafeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.or.kids.domain.ca.common.file.vo.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static kr.or.kids.global.system.common.ApiResultCode.SUCCESS;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private String fileSepStr = File.separator;  // 변경: OS에 맞는 파일 구분자 사용

    @Autowired
    private FileProperties fileProperties;

    @Autowired
    private FileMapper fileMapper;

    private final FileCryptoService cryptoService;

    public FileServiceImpl(FileCryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    /**
     * 파일정보 목록 조회
     * @param
     * @return
     */
    public ApiPrnDto list(FileDataReqVO param, int pageNum, int pageSize ) {
        ApiPrnDto result = new ApiPrnDto(SUCCESS);
        HashMap<String, Object> data = new HashMap<>();

        try {
            // UserMapper의 list() 메서드 호출
            FileDataReqVO reqParam = new FileDataReqVO();
            reqParam.setAtchFileGroupId(param.getAtchFileGroupId());
            reqParam.setTaskSeCd(param.getTaskSeCd());
            reqParam.setTaskSeTrgtId(param.getTaskSeTrgtId());
            List<FileDataResVO> fileList = fileMapper.list(reqParam);

            PageHelper.startPage(pageNum, pageSize); // ⭐ PageInfo로 감싸기
            PageInfo<FileDataResVO> pageInfo = new PageInfo<>(fileList);

            data.put("list", fileList);
            data.put("totalCount", pageInfo.getTotal());
            data.put("pageNum", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());
            data.put("pages", pageInfo.getPages()); // 총 페이지 수

            // 데이터가 없어도 성공으로 처리 (HTTP 200)
            if (fileList.size() == 0) {
                result.setMsg("조회된 데이터가 없습니다.");
            } else {
                result.setMsg("파일 목록 조회 완료");
            }

            log.info("파일 목록 조회 완료: {} 건", fileList.size());

        } catch (Exception e) {
            log.error("파일 목록 조회 실패", e);
            result = new ApiPrnDto(ApiResultCode.SYSTEM_ERROR);
            result.setMsg("파일 목록 조회 중 오류가 발생했습니다.");
        }

        result.setData(data);
        return result;
    }

    /**
     * 파일 정보 단건 삭제
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiPrnDto deleteFileOne(FileDeleteReqVO param) {

        ApiPrnDto apiPrnDto = new ApiPrnDto(SUCCESS);
        HashMap<String, Object> bizData = new HashMap<>();

        try {
            // 1. 입력 파라미터 검증
            if (param == null || param.getAtchFileId() == null) {
                throw new IllegalArgumentException("파일 ID가 필요합니다.");
            }
            if (param == null || param.getAtchFileGroupId() == null) {
                throw new IllegalArgumentException("파일 그룹ID가 필요합니다.");
            }

            Long fileId = Long.valueOf(param.getAtchFileId());
            log.info("파일 삭제 시작 - 파일 ID: {}", fileId);

            // 2. 삭제할 파일 정보 조회
            FileDataReqVO reqParam = new FileDataReqVO();
            reqParam.setAtchFileId(String.valueOf(fileId));

            FileDataResVO fileInfo = fileMapper.data(reqParam);

            if (fileInfo == null) {
                log.warn("삭제할 파일이 없습니다 - 파일 ID: {}", fileId);
                apiPrnDto.setMsg("삭제할 파일이 없습니다.");
                bizData.put("deleted", false);
                apiPrnDto.setData(bizData);
                return apiPrnDto;
            }

            log.info("삭제 대상 파일 - ID: {}, 파일명: {}", fileId, fileInfo.getFileNm());

            // 3. 물리적 파일 삭제
            String rootFilePath = fileProperties.getStorePath();
            String fileStrgPath = fileInfo.getFileStrgPathDsctn();
            String serverFileName = fileInfo.getSrvrFileNm();

            // 경로 검증
            if (fileStrgPath == null || serverFileName == null) {
                throw new IllegalStateException("파일 정보가 불완전합니다.");
            }

            // 파일 경로 조합
            Path filePath = Paths.get(rootFilePath, fileStrgPath, serverFileName);
            Path parentDirectory = filePath.getParent();

            log.info("파일 삭제 시도 - 경로: {}", filePath);

            boolean physicalFileDeleted = false;
            boolean directoryDeleted = false;

            // 파일 존재 확인 및 삭제
            if (Files.exists(filePath)) {
                // 보안: 경로 검증
                String canonicalPath = filePath.toRealPath().toString();
                if (!canonicalPath.startsWith(rootFilePath)) {
                    log.error("허용되지 않은 경로 - 경로: {}", canonicalPath);
                    throw new SecurityException("허용되지 않은 파일 경로입니다.");
                }

                // 파일 삭제
                Files.delete(filePath);
                physicalFileDeleted = true;
                log.info("물리적 파일 삭제 성공 - ID: {}, 경로: {}", fileId, filePath);

                // 4. 부모 디렉토리가 비어있으면 삭제
                if (parentDirectory != null && Files.exists(parentDirectory) && Files.isDirectory(parentDirectory)) {
                    try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(parentDirectory)) {
                        if (!dirStream.iterator().hasNext()) {
                            // 빈 디렉토리이면 삭제
                            Files.delete(parentDirectory);
                            directoryDeleted = true;
                            log.info("빈 디렉토리 삭제 성공: {}", parentDirectory);
                        } else {
                            log.info("디렉토리에 다른 파일이 있어 삭제 안함: {}", parentDirectory);
                        }
                    }
                }
            } else {
                log.warn("물리적 파일이 존재하지 않음 - ID: {}, 경로: {}", fileId, filePath);
                // 파일이 없어도 DB에서는 삭제 진행
            }

            // 5. DB에서 파일 정보 삭제
            FileDeleteReqVO deleteParam = new FileDeleteReqVO();
            deleteParam.setAtchFileId(String.valueOf(fileId));
            deleteParam.setAtchFileGroupId(param.getAtchFileGroupId());
            int deletedCount = fileMapper.deleteOne(deleteParam);
            if (deletedCount == 0) {
                log.warn("DB에서 파일 삭제 실패 - ID: {}", fileId);
                throw new RuntimeException("DB에서 파일을 찾을 수 없습니다.");
            }

            log.info("DB에서 파일 삭제 완료 - ID: {}", fileId);

            // 6. 결과 처리
            apiPrnDto.setMsg("파일이 성공적으로 삭제되었습니다.");
            bizData.put("fileId", fileId);
            bizData.put("fileName", fileInfo.getFileNm());
            bizData.put("physicalFileDeleted", physicalFileDeleted);
            bizData.put("directoryDeleted", directoryDeleted);
            bizData.put("deleted", true);

        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("입력 파라미터 오류", e);
            apiPrnDto = DrugsafeUtil.getApiPrnDto("-1", "잘못된 요청입니다: " + e.getMessage());
            throw e;

        } catch (SecurityException e) {
            log.error("보안 오류", e);
            apiPrnDto = DrugsafeUtil.getApiPrnDto("-1", "허용되지 않은 접근입니다.");
            throw e;

        } catch (IOException e) {
            log.error("파일 삭제 중 I/O 오류", e);
            apiPrnDto = DrugsafeUtil.getApiPrnDto("-1", "파일 삭제 실패: " + e.getMessage());
            throw new RuntimeException("파일 삭제 실패", e);

        } catch (Exception e) {
            log.error("파일 삭제 중 예외 발생", e);
            apiPrnDto = DrugsafeUtil.getApiPrnDto("-1", "파일 삭제 실패: " + e.getMessage());
            throw new RuntimeException("파일 삭제 실패", e);
        }

        apiPrnDto.setData(bizData);
        return apiPrnDto;
    }


    /**
     * 파일 정보 단건 삭제
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiPrnDto deleteMultiFile(FileDeleteReqVO param) {

        ApiPrnDto apiPrnDto = new ApiPrnDto(SUCCESS);
        HashMap<String, Object> bizData = new HashMap<>();

        log.info("다중 파일 삭제 시작 - FileDeleteReqVO: {}", param);
        log.info("삭제할 파일 IDs: {}", param.getFileIds());

        // 삭제 결과 추적
        List<Long> successIds = new ArrayList<>();
        List<Long> failIds = new ArrayList<>();
        int physicalFileDeletedCount = 0;
        int directoryDeletedCount = 0;
        Set<Path> directoriesToCheck = new HashSet<>();

        try {
            // 1. 입력 파라미터 검증
            if (param == null || param.getFileIds() == null || param.getFileIds().isEmpty()) {
                throw new IllegalArgumentException("삭제할 파일 ID 목록이 필요합니다.");
            }

            if (param.getAtchFileGroupId() == null) {
                throw new IllegalArgumentException("파일 그룹 ID가 필요합니다.");
            }

            List<Long> fileIds = param.getFileIds();
            log.info("다중 파일 삭제 요청 - 총 {}건", fileIds.size());

            String rootFilePath = fileProperties.getStorePath();

            // 2. 각 파일별로 삭제 처리
            for (Long fileId : fileIds) {
                try {
                    log.info("파일 삭제 처리 시작 - ID: {}", fileId);

                    // 2-1. 파일 정보 조회
                    FileDataReqVO reqParam = new FileDataReqVO();
                    reqParam.setAtchFileId(String.valueOf(fileId));

                    FileDataResVO fileInfo = fileMapper.data(reqParam);

                    if (fileInfo == null) {
                        log.warn("파일 정보 없음 - ID: {}", fileId);
                        failIds.add(fileId);
                        continue;
                    }

                    log.info("삭제 대상 파일 - ID: {}, 파일명: {}", fileId, fileInfo.getFileNm());

                    // 2-2. 물리적 파일 삭제
                    String fileStrgPath = fileInfo.getFileStrgPathDsctn();
                    String serverFileName = fileInfo.getSrvrFileNm();

                    if (fileStrgPath == null || serverFileName == null) {
                        log.warn("파일 경로 정보 불완전 - ID: {}", fileId);
                        failIds.add(fileId);
                        continue;
                    }

                    // 파일 경로 조합
                    Path filePath = Paths.get(rootFilePath, fileStrgPath, serverFileName);
                    Path parentDirectory = filePath.getParent();

                    log.info("파일 삭제 시도 - 경로: {}", filePath);

                    // 파일 존재 확인 및 삭제
                    if (Files.exists(filePath)) {
                        // 보안: 경로 검증
                        String canonicalPath = filePath.toRealPath().toString();
                        if (!canonicalPath.startsWith(rootFilePath)) {
                            log.error("허용되지 않은 경로 - ID: {}, 경로: {}", fileId, canonicalPath);
                            failIds.add(fileId);
                            continue;
                        }

                        // 파일 삭제
                        Files.delete(filePath);
                        physicalFileDeletedCount++;
                        log.info("물리적 파일 삭제 성공 - ID: {}, 경로: {}", fileId, filePath);

                        // 부모 디렉토리 체크 대상에 추가
                        if (parentDirectory != null) {
                            directoriesToCheck.add(parentDirectory);
                        }
                    } else {
                        log.warn("물리적 파일이 존재하지 않음 - ID: {}, 경로: {}", fileId, filePath);
                        // 파일이 없어도 DB 삭제는 진행
                    }

                    // 2-3. DB에서 파일 정보 삭제
                    FileDeleteReqVO deleteParam = new FileDeleteReqVO();
                    deleteParam.setAtchFileId(String.valueOf(fileId));
                    deleteParam.setAtchFileGroupId(param.getAtchFileGroupId());

                    int deletedCount = fileMapper.deleteOne(deleteParam);

                    if (deletedCount > 0) {
                        successIds.add(fileId);
                        log.info("DB에서 파일 삭제 성공 - ID: {}", fileId);
                    } else {
                        failIds.add(fileId);
                        log.warn("DB에서 파일 삭제 실패 - ID: {}", fileId);
                    }

                } catch (Exception e) {
                    log.error("파일 삭제 실패 - ID: {}", fileId, e);
                    failIds.add(fileId);
                }
            }

            // 3. 빈 디렉토리 정리
            for (Path directory : directoriesToCheck) {
                try {
                    if (Files.exists(directory) && Files.isDirectory(directory)) {
                        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
                            if (!dirStream.iterator().hasNext()) {
                                // 빈 디렉토리이면 삭제
                                Files.delete(directory);
                                directoryDeletedCount++;
                                log.info("빈 디렉토리 삭제 성공: {}", directory);
                            } else {
                                log.info("디렉토리에 다른 파일이 있어 삭제 안함: {}", directory);
                            }
                        }
                    }
                } catch (IOException e) {
                    log.warn("디렉토리 삭제 실패: {}", directory, e);
                    // 디렉토리 삭제 실패는 치명적이지 않으므로 계속 진행
                }
            }

            // 4. 결과 처리
            int totalCount = fileIds.size();
            int successCount = successIds.size();
            int failCount = failIds.size();

            String resultMsg = String.format(
                    "파일 삭제 완료 - 전체: %d건, 성공: %d건, 실패: %d건, 물리파일: %d건, 디렉토리: %d건",
                    totalCount, successCount, failCount, physicalFileDeletedCount, directoryDeletedCount
            );

            apiPrnDto.setMsg(resultMsg);

            bizData.put("totalCount", totalCount);
            bizData.put("successCount", successCount);
            bizData.put("failCount", failCount);
            bizData.put("successIds", successIds);
            bizData.put("failIds", failIds);
            bizData.put("physicalFileDeletedCount", physicalFileDeletedCount);
            bizData.put("directoryDeletedCount", directoryDeletedCount);
            bizData.put("fileGroupId", param.getAtchFileGroupId());

            log.info("다중 파일 삭제 결과 - {}", resultMsg);

            // 일부만 실패한 경우 경고 메시지
            if (failCount > 0 && successCount > 0) {
                apiPrnDto.setMsg(resultMsg + " (일부 파일 삭제 실패)");
            } else if (failCount > 0 && successCount == 0) {
                apiPrnDto = DrugsafeUtil.getApiPrnDto("-1", "모든 파일 삭제에 실패했습니다.");
            }

        } catch (IllegalArgumentException e) {
            log.error("입력 파라미터 오류", e);
            apiPrnDto = DrugsafeUtil.getApiPrnDto("-1", "잘못된 요청입니다: " + e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("파일 삭제 중 예외 발생", e);
            apiPrnDto = DrugsafeUtil.getApiPrnDto("-1", "파일 삭제 실패: " + e.getMessage());
            throw new RuntimeException("파일 삭제 실패", e);
        }

        apiPrnDto.setData(bizData);
        return apiPrnDto;
    }

    /**
     * 파일 그룹  일괄 삭제
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiPrnDto deleteGroupFiles(FileDeleteReqVO param) {

        ApiPrnDto apiPrnDto = new ApiPrnDto(SUCCESS);
        HashMap<String, Object> bizData = new HashMap<>();

        // 삭제할 파일 ID 리스트
        List<Long> deletedFileIds = new ArrayList<>();
        // 삭제할 디렉토리 목록 (중복 제거용)
        Set<Path> directoriesToDelete = new HashSet<>();

        try {
            // 1. 입력 파라미터 검증
            if (param == null || param.getAtchFileGroupId() == null) {
                throw new IllegalArgumentException("파일 그룹 ID가 필요합니다.");
            }

            Long fileGroupId = Long.valueOf(param.getAtchFileGroupId());
            log.info("파일 삭제 시작 - 파일 그룹 ID: {}", fileGroupId);

            // 2. 삭제할 파일 목록 조회
            FileDataReqVO reqParam = new FileDataReqVO();
            reqParam.setAtchFileGroupId(String.valueOf(fileGroupId));

            List<FileDataResVO> fileList = fileMapper.list(reqParam);

            if (fileList == null || fileList.isEmpty()) {
                log.warn("삭제할 파일이 없습니다 - 그룹 ID: {}", fileGroupId);
                apiPrnDto.setMsg("삭제할 파일이 없습니다.");
                bizData.put("deletedCount", 0);
                apiPrnDto.setData(bizData);
                return apiPrnDto;
            }

            log.info("삭제 대상 파일 개수: {}", fileList.size());

            // 3. 물리적 파일 삭제
            String rootFilePath = fileProperties.getStorePath();
            int successCount = 0;
            int failCount = 0;

            for (FileDataResVO fileInfo : fileList) {
                try {
                    Long atchFileId = Long.valueOf(fileInfo.getAtchFileId());
                    String fileStrgPath = fileInfo.getFileStrgPathDsctn();
                    String serverFileName = fileInfo.getSrvrFileNm();

                    // 경로 검증
                    if (fileStrgPath == null || serverFileName == null) {
                        log.warn("파일 정보가 불완전함 - ID: {}", atchFileId);
                        failCount++;
                        continue;
                    }

                    // 올바른 경로 조합 (Path 사용)
                    Path filePath = Paths.get(rootFilePath, fileStrgPath, serverFileName);

                    // ⭐ 부모 디렉토리 저장 (UUID 디렉토리)
                    Path parentDirectory = filePath.getParent();

                    log.info("파일 삭제 시도 - ID: {}, 경로: {}", atchFileId, filePath);

                    // 파일 존재 확인 및 삭제
                    if (Files.exists(filePath)) {
                        // 보안: 경로 검증
                        String canonicalPath = filePath.toRealPath().toString();
                        if (!canonicalPath.startsWith(rootFilePath)) {
                            log.error("허용되지 않은 경로 - ID: {}, 경로: {}", atchFileId, canonicalPath);
                            throw new SecurityException("허용되지 않은 파일 경로입니다.");
                        }

                        // 파일 삭제
                        Files.delete(filePath);
                        deletedFileIds.add(atchFileId);
                        successCount++;
                        log.info("물리적 파일 삭제 성공 - ID: {}, 경로: {}", atchFileId, filePath);

                        // ⭐ 부모 디렉토리를 삭제 대상에 추가
                        if (parentDirectory != null) {
                            directoriesToDelete.add(parentDirectory);
                        }

                    } else {
                        log.warn("파일이 존재하지 않음 - ID: {}, 경로: {}", atchFileId, filePath);
                        deletedFileIds.add(atchFileId);  // DB에서는 삭제
                        failCount++;
                    }

                } catch (IOException | SecurityException e) {
                    log.error("파일 삭제 실패 - ID: {}", fileInfo.getAtchFileId(), e);
                    throw new RuntimeException("파일 삭제 중 오류 발생: " + e.getMessage(), e);
                }
            }

            // ⭐ 4. 빈 디렉토리 삭제 (UUID 폴더)
            int deletedDirCount = 0;
            for (Path directory : directoriesToDelete) {
                try {
                    if (Files.exists(directory) && Files.isDirectory(directory)) {
                        // 디렉토리가 비어있는지 확인
                        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
                            if (!dirStream.iterator().hasNext()) {
                                // 빈 디렉토리이면 삭제
                                Files.delete(directory);
                                deletedDirCount++;
                                log.info("빈 디렉토리 삭제 성공: {}", directory);
                            } else {
                                log.info("디렉토리에 다른 파일이 있어 삭제 안함: {}", directory);
                            }
                        }
                    }
                } catch (IOException e) {
                    log.warn("디렉토리 삭제 실패: {}", directory, e);
                    // 디렉토리 삭제 실패는 치명적이지 않으므로 계속 진행
                }
            }

            // 5. DB에서 파일 정보 삭제
            if (!deletedFileIds.isEmpty()) {
                FileDeleteReqVO deleteParam = new FileDeleteReqVO();
                deleteParam.setAtchFileGroupId(String.valueOf(fileGroupId));

                int deletedFiles = fileMapper.delete(deleteParam);
                int deletedGroups = fileMapper.deleteGroup(deleteParam);

                log.info("DB 삭제 완료 - 파일: {}건, 그룹: {}건", deletedFiles, deletedGroups);
            }

            // 6. 결과 처리
            apiPrnDto.setMsg(String.format("파일 삭제 완료 - 성공: %d건, 실패: %d건, 디렉토리 삭제: %d건",
                    successCount, failCount, deletedDirCount));
            bizData.put("fileGroupId", fileGroupId);
            bizData.put("totalCount", fileList.size());
            bizData.put("successCount", successCount);
            bizData.put("failCount", failCount);
            bizData.put("deletedDirCount", deletedDirCount);
            bizData.put("deletedFileIds", deletedFileIds);

        } catch (IllegalArgumentException e) {
            log.error("입력 파라미터 오류", e);
            apiPrnDto = DrugsafeUtil.getApiPrnDto("-1", "잘못된 요청입니다: " + e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("파일 삭제 중 예외 발생", e);
            apiPrnDto = DrugsafeUtil.getApiPrnDto("-1", "파일 삭제 실패: " + e.getMessage());
            throw new RuntimeException("파일 삭제 실패", e);
        }

        apiPrnDto.setData(bizData);
        return apiPrnDto;
    }

    /**
     *  파일 그룹정보 단건 조회
     * @return
     */
    public ApiPrnDto groupData(FileGroupReqData fileGroupReqData) {
        ApiPrnDto result = new ApiPrnDto(ApiResultCode.SUCCESS);
        HashMap<String, Object> resData = new HashMap<>();

        try {

            // || fileGroupReqData.getTaskSeTrgtId() == null
            if(fileGroupReqData.getTaskSeCd() == null) {
                throw new RuntimeException("taskSeCd 또는 taskSeTrgtId  파라미터가 누락되었습니다.");
            }

            /**
             * BizProc
             */
            // 업무코드와 업무 게시물 번호로 그룹ID 조회
            FileGroupResData data = fileMapper.groupData(fileGroupReqData);

            long lastMaxGroupId = 0;
            if(data == null) {  // 그룹데이터에 없으면 시퀀스에서 가져옴
                 lastMaxGroupId =  fileMapper.nextFileGroupId();
                resData.put("atchFileGroupId",lastMaxGroupId);
            }else{             // 그룹데이터에 있으면 시퀀스에서 가져옴
                 lastMaxGroupId = Long.parseLong(data.getAtchFileGroupId());
                resData.put("atchFileGroupId",lastMaxGroupId);
            }

            if(lastMaxGroupId == 0) {
                result = new ApiPrnDto(ApiResultCode.SYSTEM_ERROR);
                result.setMsg("사용자정보가 존재하지 않습니다.");
            } else {
                // data가 null이 아닐 때 실행
               // resData =  //new ObjectMapper().convertValue(resData, new TypeReference<HashMap<String, Object>>() {});
                result.setData(resData);
                result.setMsg("사용자 단건 조회 완료");
            }


        } catch(Exception e) {
            log.error("파일 그룹 아이디 조회 실패", e);
            result = new ApiPrnDto(ApiResultCode.SYSTEM_ERROR);
            result.setMsg("파일 그룹 아이디 오류가 발생했습니다: " + e.getMessage());
        }

       // result.setData(resData);
        return result;
    }


    /**
     * 파일 그룹정보 일련번호 생성
     * @param param
     * @return
     */
    @Transactional
    public ApiPrnDto groupInsert(FileGroupInsertReq param) {

        ApiPrnDto ApiPrnDto             = new ApiPrnDto(SUCCESS);
        HashMap<String, Object> bizData = new HashMap<>();

        try {
            /**
             *  파일 그룹정보  생성
             */
            long nextFileGroupId = fileMapper.nextFileGroupId();
             param.setAtchFileGroupId(String.valueOf(nextFileGroupId));

            param.setUseYn("Y");
            fileMapper.groupInsert(param);

            /**
             * 결과처리
             */
            ApiPrnDto.setMsg("파일 그룹 정보가 등록되었습니다.");
            bizData.put("atchFileGroupId", nextFileGroupId);
        } catch(Exception e) {
            ApiPrnDto = DrugsafeUtil.getApiPrnDto("-1", e.toString());
        }
        ApiPrnDto.setData(bizData);
        return ApiPrnDto;
    }

    /**
     * 파일 그룹정보 업데이트
     * @param param
     * @return
     */
    @Transactional
    public ApiPrnDto groupUpdate(FileGroupInsertReq param) {

        ApiPrnDto ApiPrnDto   = new ApiPrnDto(SUCCESS);
        HashMap<String, Object> bizData = new HashMap<>();

        try {
            /**
             *  파일 그룹정보  수정
             */
            fileMapper.groupUpdate(param);
            /**
             * 결과처리
             */
            ApiPrnDto.setMsg("파일 그룹 정보가 수정되었습니다.");
            bizData.put("atchFileGroupId", param.getAtchFileGroupId());
        } catch(Exception e) {
            ApiPrnDto = DrugsafeUtil.getApiPrnDto("-1", e.toString());
        }
        ApiPrnDto.setData(bizData);
        return ApiPrnDto;
    }

    /**
     * 파일 업로드
     * @return
     */
    @Transactional
    @Override
    public ApiPrnDto uploadFiles(HashMap<String, Object> params, MultipartFile[] uploadFiles) {
        return uploadPost(params, uploadFiles, "attachment", null);
    }

    /**
     * 파일 업로드
     * @return
     */
    private ApiPrnDto uploadPost(HashMap<String, Object> params, MultipartFile[] uploadFiles, String type, String fileName) {

        ApiPrnDto apiPrnDto             = new ApiPrnDto(SUCCESS);
        HashMap<String, Object> data = new HashMap<>();

        String savePath = params.get("savePath").toString();
        String atchFileGroupId = params.get("atchFileGroupId").toString();
        String prvcInclYn = params.get("prvcInclYn").toString();
        String isExcel = params.get("isExcel").toString();
        String uuid =   UUID.randomUUID().toString();

        try {
            /**
             * 년월 기반 경로 생성 (예: 202512)
             */
            String yearMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            String crtDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            /**
             * 저장경로 설정(\data\storage\attach\202512\savePath\)
             */
            String tempSavePath = this.getSavePath(savePath, type, yearMonth,isExcel);

            /**
             * 파일 Write
             */
            List<HashMap<String, Object>> uploadList = new ArrayList<>();

            int fileSeq = 0;
            for (MultipartFile uploadFile: uploadFiles) {
                /**
                 * 파일 정보
                 */
                String orginalName  = uploadFile.getOriginalFilename();
                String fileNm       = orginalName.substring(orginalName.lastIndexOf("/") + 1);
                String extNm        = fileNm.substring(fileNm.lastIndexOf(".") + 1);

                String realFileNm   = null;
                if(fileName != null) {
                    realFileNm   = fileName + "_"+String.valueOf(System.currentTimeMillis())+"." + extNm;
                }
                else {
                    realFileNm   = uuid + "." + extNm;
                }

                String rootFilePath = fileProperties.getStorePath();

                String saveDirectoryPath = "";    //  파일저장  디렉토리 경로
                String saveFullPath = "";         // 파일저장 경로  최종  파일명까지
                String dbInsertSavePath ="";      // DB 저장용 경로 정보

                log.info("prvcInclYn:::::::::"  + prvcInclYn);

                // 암호화된 파일 데이터를 저장할 변수
                byte[] fileDataToSave = null;
                long fileSize = 0;

                // 개인정보이면
            if( (prvcInclYn.equals("1") && isExcel.equals("")) ||  (prvcInclYn.equals("1") && isExcel.equals("0"))) {

                log.info("개인정보 포함 파일 - 암호화 처리 시작");

                // 파일 데이터를 바이트 배열로 변환 후 암호화
                fileDataToSave = cryptoService.encrypt(uploadFile.getBytes());

                saveDirectoryPath = rootFilePath + tempSavePath + uuid;
                saveFullPath = rootFilePath + tempSavePath + uuid + File.separator + realFileNm;
                dbInsertSavePath = this.getSavePath(savePath, type, yearMonth, "") + uuid;

                log.info("암호화 완료 - 원본 크기: {}, 암호화 후 크기: {}",
                        uploadFile.getSize(), fileDataToSave.length);
            }else {

                // 첨부가  액셀이면
                if ((isExcel.equals("1") && prvcInclYn.equals("")) || (isExcel.equals("1") && prvcInclYn.equals("0"))) {
                    log.info("prvcInclYn:::::0000000::::" + prvcInclYn);
                    saveDirectoryPath = rootFilePath + tempSavePath;
                    saveFullPath = rootFilePath + tempSavePath + File.separator + realFileNm;
                    dbInsertSavePath = this.getSavePath(savePath, type, yearMonth, isExcel);

                    // 일반 파일 (암호화 안함)
                    fileDataToSave = uploadFile.getBytes();

                    // 개인정보 포함인 경우 - 암호화 처리
                } else { // 엑셀과  개인정보가 아닌 일반파일
                    saveDirectoryPath = rootFilePath + tempSavePath;
                    saveFullPath = rootFilePath + tempSavePath + File.separator + realFileNm;
                    dbInsertSavePath = this.getSavePath(savePath, type, yearMonth, "");

                    // 일반 파일 (암호화 안함)
                    fileDataToSave = uploadFile.getBytes();
                }

            }
                log.info("saveDirectoryPath:::::::::"  + saveDirectoryPath);
                log.info("saveFullPath::::::::::::::"  + saveFullPath);
                log.info("dbInsertSavePath:::::::::::" + dbInsertSavePath);

                /**
                 * 저장폴더 생성
                 */
                File saveFolder = new File(saveDirectoryPath);
                if(!saveFolder.exists() && !saveFolder.isDirectory()) {
                    saveFolder.mkdirs();
                }

                /**
                 * 파일 저장 - 암호화된 데이터 또는 일반 데이터
                 */
                Path filePath = Paths.get(saveFullPath);
                Files.write(filePath, fileDataToSave);

                // 저장된 파일 크기
                fileSize = Files.size(filePath);

                log.info("@@ File saved - Path: {}, Size: {}", saveFullPath, fileSize);

                /**
                 * DB에 파일 정보 저장
                 */
                // 파일 ID 생성
                long nextFileId = fileMapper.nextFileId();

                log.info("atchFileGroupId::::::"+ atchFileGroupId);
                log.info("nextFileId::::::"+ nextFileId);

                // FileInsertReqVO 생성 및 데이터 설정
                FileInsertReqVO fileInsertReqVO = new FileInsertReqVO();
                fileInsertReqVO.setAtchFileId(String.valueOf(nextFileId));
                fileInsertReqVO.setAtchFileGroupId(atchFileGroupId);
                fileInsertReqVO.setFileSeq(fileSeq);
                fileInsertReqVO.setFileStrgPathDsctn(dbInsertSavePath);   // 저장경로
                fileInsertReqVO.setSrvrFileNm(realFileNm);
                fileInsertReqVO.setFileNm(fileNm);
                fileInsertReqVO.setPrvcInclYyn(prvcInclYn);           // 개인정보 여부
                fileInsertReqVO.setFileCn("");                        // 내용
                fileInsertReqVO.setFileExtnNm(extNm);                 // 확장자
                fileInsertReqVO.setFileSz(fileSize);
                fileInsertReqVO.setCrtDt(crtDt);
                fileInsertReqVO.setUseYn("1");
                fileInsertReqVO.setRgtrId("admin");
                fileInsertReqVO.setMdfrId("admin");

                // 개인정보 암호화 여부 플래그 추가 (DB 컬럼이 있다면)
                // fileInsertReqVO.setPrvcInclYn(prvcInclYn);
                // fileInsertReqVO.setEncryptYn(prvcInclYn.equals("1") ? "1" : "0");

                // DB에 insert
                log.info("fileInsertReqVO::::::"+ fileInsertReqVO);
                fileMapper.insert(fileInsertReqVO);

                log.info("@@ File saved to DB - fileId: {}, fileName: {}, path: {}", nextFileId, orginalName, tempSavePath);

                HashMap<String, Object> updateInfo = new HashMap<String, Object>();
                updateInfo.put("atchFileId"    , nextFileId);    // DB에 저장된 파일 ID 추가
                updateInfo.put("filePath"  , tempSavePath);
                updateInfo.put("fileNm"    , fileNm);
                updateInfo.put("fileType"  , extNm);
                updateInfo.put("fileEncNm" , realFileNm);
                updateInfo.put("fileSize"  , fileSize);
                updateInfo.put("yearMonth" , yearMonth);     // 년월 정보 추가
                updateInfo.put("encrypted" , prvcInclYn.equals("1") ? "Y" : "N");  // 암호화 여부
                uploadList.add(updateInfo);

                fileSeq++;
            }

            data.put("uploadList", uploadList);
        } catch(Exception e) {
            log.error("@@ File upload error: ", e);
            apiPrnDto = DrugsafeUtil.getApiPrnDto("-1", e.toString());
        }

        apiPrnDto.setData(data);
        return apiPrnDto;
    }


    /**
     * 저장경로 설정
     * @param savePath 사용자 지정 경로
     * @param fileType 파일 타입 (attachment, image 등)
     * @param yearMonth 년월 (예: 202512)
     * @return 최종 저장 경로 (예: /attachment/202512/savePath/)
     */
    private String getSavePath(String savePath, String fileType, String yearMonth, String isExcel) {

        if (StringUtils.hasLength(savePath)) {
            savePath = savePath.replaceAll("\\.", "");
        }

        // 앞에 \ 있으면 제거
        if (StringUtils.hasLength(savePath) && savePath.startsWith(File.separator)) {
            savePath = savePath.substring(1);
        }

        log.info("rootPath = {}", fileProperties.getStorePath());

        //  엑셀파일 첨부인겨우 수정: \attach\ca\202601\
        if(isExcel.equals("1")) {
            savePath = File.separator + "attach" + File.separator + savePath + File.separator + "excel" + File.separator + yearMonth + File.separator;
        }else{
            savePath = File.separator + "attach" + File.separator + savePath + File.separator + yearMonth + File.separator;
        }

        // 마지막 \ 보장
        if (!savePath.endsWith(File.separator)) {
            savePath = savePath + File.separator;
        }

        log.info("savePath(before return) = {}", savePath);

        return savePath;
    }


    @Override
    public void saveDownloadLog(FileDownloadLogReqVO param) {

        // 접속행위 일련번호 생성
        param.setCntnLogSn(UUID.randomUUID().toString());

        int result = fileMapper.insertDownloadLog(param);

        if (result != 1) {
            log.error("파일 다운로드 사유 로그 저장 실패: {}", param);
            throw new IllegalStateException("파일 다운로드 로그 저장 실패");
        }

        log.info("파일 다운로드 사유 로그 저장 완료 - cntnLogSn={}", param.getCntnLogSn());
    }
}
