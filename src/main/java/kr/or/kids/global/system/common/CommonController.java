package kr.or.kids.global.system.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CommonController {

    @Autowired
    public CommonComponent common;

    /**
     * 기본 페이지 사이즈
     */
    private int basePageSize = 10;




}