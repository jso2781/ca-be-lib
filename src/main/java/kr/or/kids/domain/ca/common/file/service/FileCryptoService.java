package kr.or.kids.domain.ca.common.file.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@Slf4j
@Service
public class FileCryptoService {


    private final String secretKey;
    private final String algorithm;

    // ⭐ 생성자 주입
    public FileCryptoService(
            @Value("${file.encryptkey}") String secretKey,
            @Value("${file.algorithm}") String algorithm
    ) {
        this.secretKey = secretKey;
        this.algorithm = algorithm;

        log.info("FileCryptoService 초기화 완료");
        log.info("Algorithm: {}", algorithm);
        log.info("Key length: {} bytes", secretKey.getBytes().length);

        // 키 길이 검증 (AES-256은 32바이트 필요)
        if (secretKey.getBytes().length != 32) {
            log.error("잘못된 키 길이: {} bytes (32 bytes 필요)", secretKey.getBytes().length);
            throw new IllegalArgumentException("Secret key must be 32 bytes for AES-256");
        }
    }


    public byte[] encrypt(byte[] fileData) throws Exception {
        // 1. IV 생성 (매번 랜덤하게 생성하여 보안성 향상)
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        log.info("SECRET_KEY.getBytes::::::::"+ secretKey.getBytes());

        // 2. Cipher 초기화
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        // 3. 암호화 실행
        byte[] encrypted = cipher.doFinal(fileData);

        // 4. 복호화를 위해 IV를 암호화된 데이터 앞에 붙여서 저장
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return combined;
    }

    public byte[] decrypt(byte[] combinedData) throws Exception {
        // 1. 데이터에서 IV 추출 (앞 16바이트)
        byte[] iv = new byte[16];
        System.arraycopy(combinedData, 0, iv, 0, iv.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 2. 실제 암호문 추출
        int encryptedSize = combinedData.length - 16;
        byte[] encrypted = new byte[encryptedSize];
        System.arraycopy(combinedData, 16, encrypted, 0, encryptedSize);

        // 3. 복호화 실행
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        return cipher.doFinal(encrypted);
    }
}