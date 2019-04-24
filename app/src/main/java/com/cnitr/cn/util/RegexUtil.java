package com.cnitr.cn.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YangChen on 2018/5/18.
 */

public class RegexUtil {

    // 密码
    public static final String REGEX_PASSWORD = "^[0-9A-Za-z]{6,20}$";

    // 手机号码
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";

    // 邮箱账号
    public static final String REGEX_EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

    /**
     * 正则表达式
     *
     * @param text
     * @param regex
     * @return
     */
    public static boolean commonRegex(String text, String regex) {

        boolean result = false;
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
