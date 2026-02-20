package kr.or.kids.global.util;

import kr.or.kids.global.system.common.vo.ApiPrnDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class DrugsafeUtil {

    public static List<Integer> RemoveNegativeNum(List<Integer> numbers) {
        // Iterator를 사용하여 배열 순회
        Iterator<Integer> iterator = numbers.iterator();
        List<Integer> returnNum = new ArrayList<Integer>();
        while (iterator.hasNext()) {
            // 현재 요소 가져오기
            Integer number = iterator.next();
            // 음수인지 확인
       /*   if (number < 0) {
                iterator.remove();
            }
        */
            if (0 < number) {
                returnNum.add(number);
            }
        }
        return returnNum;
    }

    /**
     * Dto to HashMap 변환
     *
     * @param obj
     * @return
     */
    public static HashMap<String, Object> convertDtoToHashMap(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        HashMap<String, Object> map = objectMapper.convertValue(obj, HashMap.class);
        return map;
    }

    /**
     * HashMap to VO 변환
     *
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T convertHashMapToVo(HashMap<String, Object> map, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * HashMap의 key를 UPPER SNAKE로 변환
     *
     * @param map
     */
    public static void convertKeysToSnake(HashMap<String, Object> map) {
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            map.put(DrugsafeUtil.camelToSnake(entry.getKey()), entry.getValue());
        }
    }

    /**
     * CAMEL TO UPPER SNAKE
     *
     * @param str
     * @return
     */
    public static String camelToSnake(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i > 0) result.append('_');
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString().toUpperCase();
    }

    /**
     * API 공통 DTO 생성 메서드
     *
     * @param code
     * @param msg
     * @return
     */
    public static ApiPrnDto getApiPrnDto(String code, String msg) {
        ApiPrnDto result = new ApiPrnDto();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(new HashMap<String, Object>());
        return result;
    }


    /**
     * 날짜 포맷 메서드
     *
     * @param type
     * @return
     */
    public static String dateFromat(String type) {
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat;

        if ("yyyyMMdd".equals(type)) {
            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        } else if ("yyyy-MM-dd".equals(type)) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else if ("yyyy-MM-dd HH:mm:ss".equals(type)) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if ("yyyyMMddHHmmss".equals(type)) {
            simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        } else if ("HHmmss".equals(type)) {
            simpleDateFormat = new SimpleDateFormat("HHmmss");
        } else if ("E".equals(type)) {
            simpleDateFormat = new SimpleDateFormat("E");
        } else if ("오늘은 E요일".equals(type)) {
            simpleDateFormat = new SimpleDateFormat("오늘은 E요일 입니다");
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        return simpleDateFormat.format(nowDate);
    }


    /**
     * API 공통 DTO 생성 메서드
     *
     * @param
     * @param
     * @return
     */
    public static String markingFunc(String part, String value) {

        String rtrResult = value;

        if (part.equals("name")) {
            int len = value.length();

            if (len == 2) {
                rtrResult = value.substring(0, 1) + "*";
            } else if (len > 2) {
                String first = value.substring(0, 1);
                String last = value.substring(len - 1);
                String middle = "*".repeat(len - 2);
                rtrResult = first + middle + last;
            }

        } else if (part.equals("tel")) {


            // 숫자만 추출
            String digits = value.replaceAll("[^0-9]", "");

            String first;
            String middle;
            String last;

            // -----------------------------
            // 1) 지역번호/휴대폰 판별
            // -----------------------------
            if (digits.startsWith("02")) {
                // 02 지역번호
                first = digits.substring(0, 2);
                last = digits.substring(digits.length() - 4);
                middle = digits.substring(2, digits.length() - 4);

            } else {
                // 010 또는 기타 3자리 지역번호
                first = digits.substring(0, 3);
                last = digits.substring(digits.length() - 4);
                middle = digits.substring(3, digits.length() - 4);
            }

            // 가운데 자리 마스킹
            middle = "*".repeat(middle.length());

            // 최종 출력 (항상 하이픈 포함)
            rtrResult =  first + "-" + middle + "-" + last;


        }

        return rtrResult;

    }

    public String getClientIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // X-Forwarded-For: client, proxy1, proxy2 ...
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("X-Real-IP");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }

    public static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // JVM에 SHA-256이 없을 확률은 사실상 없지만, 런타임 예외로 감싸서 올립니다.
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    private static String toHex(byte[] bytes) {
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


}