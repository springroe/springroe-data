package io.github.springroe.data.mybatis.util;

public class SqlUtil {

    public static String getIfNotNull(String testValue, String content) {
        return "<if test=\"" + testValue + " neq null and " + testValue + " neq ''\">" + content + "</if>";
    }

    public static String getIfTrue(String testValue, String content) {
        return "<if test=\"" + testValue + "\">" + content + "</if>";
    }

    public static String getIfContain(String testValue, String containValue, String content, boolean contain) {
        if (contain) {
            return "<if test=\"" + testValue + ".contains(\"" + containValue.toUpperCase() + "\") or " + testValue + ".contains(\"" + containValue.toLowerCase() + "\")\">" + content + "</if>";
        } else {
            return "<if test=\"!(" + testValue + ".contains(\"" + containValue.toUpperCase() + "\") or " + testValue + ".contains(\"" + containValue.toLowerCase() + "\"))\">" + content + "</if>";
        }
    }

}
