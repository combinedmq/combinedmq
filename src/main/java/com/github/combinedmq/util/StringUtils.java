package com.github.combinedmq.util;

/**
 * @author xiaoyu
 */
public class StringUtils {
    /**
     * 将字符串转换成unicode码
     *
     * @param str 要转换的目标字符串
     * @return
     */
    public static String stringToUnicode(String str) {
        char[] cs = str.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : cs) {
            int len = Integer.toHexString(c).length();
            builder.append("\\u");
            for (int i = 0; i < 4 - len; i++) {
                builder.append("0");
            }
            builder.append(Integer.toHexString(c));
        }
        return builder.toString();
    }

    /**
     * 将unicode转换成字符串
     *
     * @param unicodeStr 要转换的目标字符串
     * @return
     */
    public static String unicodeToString(String unicodeStr) {
        String[] strs = unicodeStr.split("\\\\u");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].length() < 4 || i == 0) {
                builder.append(strs[i]);
            } else if (strs[i].length() == 4) {
                builder.append((char) Integer.parseInt(strs[i], 16));
            } else {
                builder.append((char) Integer.parseInt(strs[i].substring(0, 4), 16));
                builder.append(strs[i].substring(4));
            }
        }
        return builder.toString();
    }

    /**
     * 按字节大小截取字符串
     *
     * @param targetStr 要截取的目标字符串
     * @param len       要截取的字节大小
     * @return
     * @throws Exception
     */
    public static String substring(String targetStr, int len) throws Exception {
        String cst = "utf-8";
        if (targetStr.getBytes(cst).length <= len) {
            return targetStr;
        }
        char[] cs = targetStr.toCharArray();
        int sum = 0;
        String s;
        StringBuilder builder = new StringBuilder();
        for (char c : cs) {
            s = c + "";
            sum += s.getBytes(cst).length;
            if (sum > len) {
                break;
            }
            builder.append(s);
        }
        return builder.toString();
    }

}