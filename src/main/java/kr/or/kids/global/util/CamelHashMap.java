package kr.or.kids.global.util;

import org.apache.ibatis.type.Alias;
import org.springframework.jdbc.support.JdbcUtils;

import java.util.HashMap;

/**
 * 카멜케이스 HashMap 클래스
 *
 * @author Myeong.Jae.Cheol
 */
@Alias("CamelHashMap")
public class CamelHashMap<V> extends HashMap<String, V> {

    @Override
    public V put(String key, V value) {
        String camelCaseKey = JdbcUtils.convertUnderscoreNameToPropertyName(key);
        return super.put(camelCaseKey, value);
    }
}