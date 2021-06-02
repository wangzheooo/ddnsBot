package cool.wellwellwell.ddns.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 获取域名的二级域名
     *
     * @param host
     * @return
     */
    public static String getSecondaryDomain(String host) {
        int lastDelimiter = host.lastIndexOf(".", host.lastIndexOf(".") - 1);
        return host.substring(0, lastDelimiter);
    }

    /**
     * 正则截取字符串,返回单个字符串，若匹配到多个的话就返回第一个
     *
     * @param soap 内容
     * @param rgex 正则
     * @return     
     */
    public static String getSubUtilSimple(String soap, String rgex) {
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            return m.group(1).trim();
        }
        return "";
    }

    //判断是不是ip
    public static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();
        return ipAddress;
    }

}
