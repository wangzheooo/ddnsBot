package cool.wellwellwell.ddns.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: auther
 * @Date: 2021/4/19 9:11
 * @Description:收集这么多网站是为了防止有一个不行了,其他可以代替 需要代理才能访问的, 所以代理访问后就是代理的ip
 * https://ipinfo.io/
 * http://ifconfig.me/ip
 */
public class IpUtils {
    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);

    /**
     * 总测试
     * getIpA:214,482,223,237,233,478
     * getIpB:290,238,247,248,242,431
     * getIpC:1777,1781,1823,1799,1798,1807
     */
    /*@Test
    public void test() {
        Long st = System.currentTimeMillis();
        Map map = getIp();
        Long sp = System.currentTimeMillis();
        System.out.println(map.get("status"));
        System.out.println(map.get("msg"));
        if (map.get("status").equals("success")) {
            System.out.println(map.get("url"));
            System.out.println(map.get("content"));
        }
        System.out.println("耗时:" + (sp - st));
    }*/

    /**
     * 获取当前公网ip
     */
    public static Map getIp() {
        Map map = getIpA();
        if (map.get("status").equals("success")) {
            return map;
        }
        map = getIpB();
        if (map.get("status").equals("success")) {
            return map;
        }
        return getIpC();
    }

    /**
     * 返回body内容
     *
     * @return
     */
    public static Map getBody(String url) {
        Map resultMap = new HashMap();
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            logger.info("getBody,网络异常,异常url:" + url);
            resultMap.put("status", "fail");
            resultMap.put("msg", "网络异常,异常url:" + url);
            resultMap.put("url", url);
            return resultMap;
        }
        String content = document.body().text();
        if (StrUtil.isNotEmpty(content)) {
            logger.info("getBody,获取到body信息,body:" + content + ", url:" + url);
            resultMap.put("status", "success");
            resultMap.put("msg", "获取到body信息");
            resultMap.put("url", url);
            resultMap.put("content", content);
            return resultMap;
        } else {
            logger.info("getBody,获取到body信息为空,url:" + url);
            resultMap.put("status", "fail");
            resultMap.put("msg", "获取到body信息为空");
            resultMap.put("url", url);
            return resultMap;
        }
    }

    /**
     * 判断ip
     *
     * @return
     */
    public static Map isIp(Map map) {
        Map resultMap = new HashMap();
        if (map.get("status").equals("success")) {
            if (StringUtil.isIP((String) map.get("content"))) {
                logger.info("isIp,正则判断为IP,ip:" + map.get("content") + ",url:" + map.get("url"));
                map.put("msg", map.get("msg") + "-->内容判定为IP");
                return map;
            } else {
                logger.info("isIp,正则判断为不是IP,body内容:" + map.get("content") + ",url:" + map.get("url"));
                map.put("msg", map.get("msg") + "-->内容判定为不是IP,body内容:" + map.get("content") + ",url:" + map.get("url"));
                return resultMap;
            }
        } else {
            return map;
        }

    }

    /**
     * A,只返回ip的,以后添加新的url,只需要在LISTA中添加即可
     * http://ip.getlove.cn/ ,稳定性:60,耗时:103,102,130,169,258,122,105
     * http://ip.3322.net/ ,云运营商提供的接口,稳定性:99,耗时:365,103,1422,170,310,83,228
     * http://myip.ipip.net/s ,ipip的,稳定性:99,耗时:321,313,746,333,3361,1640,144
     * http://ident.me/ ,能返回代理ip,稳定性:60,耗时:484,1498,490,492,520,514,451
     * http://api.ipify.org/ ,能返回代理ip,api接口商,稳定性:90,耗时:590,558,1567,592,1738,7391,519
     * http://ip.cip.cc/ ,个人接口,稳定性:30,耗时:78,1307,83,131,0,74,318
     * https://api.ip.sb/ip ,稳定性:60,耗时:4736,4613,4464,4659,5790,3843,3782
     * <p>
     * https://api64.ipify.org,能返回ipv6,暂时没用
     */
    private final static List<String> LISTA = Arrays.asList(
            "http://ip.getlove.cn/",
            "http://ip.3322.net/",
            "http://myip.ipip.net/s",
            "http://ident.me/",
            "http://api.ipify.org/",
            "http://ip.cip.cc/",
            "https://api.ip.sb/ip");

    public static Map speedTestIpA() {
        Map map = new HashMap();
        Long st;
        Long sp;
        for (String url : LISTA) {
            st = System.currentTimeMillis();
            logger.info("speedTestIpA,当前url:" + url);
            map = getBody(url);
            map = isIp(map);
            sp = System.currentTimeMillis();
            if (map.get("status").equals("success")) {
                System.out.println("speedTestIpA,成功:" + map.get("content"));
            } else {
                System.out.println("speedTestIpA,失败");
            }
            System.out.println("speedTestIpA,耗时:" + (sp - st));
            System.out.println("---------------------------------------------------");
        }
        return map;
    }

    public static Map getIpA() {
        Map map = new HashMap();
        for (String url : LISTA) {
            logger.info("getIpA,当前url:" + url);
            map = getBody(url);
            if (map.get("status").equals("success")) {
                logger.info("getIpA,截取body中的IP区域,区域内容:" + map.get("content"));
                map.put("msg", map.get("msg") + "-->截取body中的IP区域");
            }
            map = isIp(map);
            if (map.get("status").equals("success")) {
                map.put("msg", map.get("msg") + "-->返回IP");
                break;
            }
        }
        return map;
    }

    /**
     * B,各种自定义的显示数据,有json,有web等等,统一获取body后用正则截取
     * http://pv.sohu.com/cityjson ,搜狐,稳定性:99,耗时:285,290,326
     * http://ip.dhcp.cn/?json ,个人接口,稳定性:50,耗时:124,139,120
     * https://ip.tool.lu/ ,稳定性:50,耗时:3278,3258,3314
     * <p>
     * https://ip138.com/
     * 如果加新的网址,需要改INTB,在getIpB()加方法,新写一个getIpBX()
     */
    private final static int INTB = 3;

    public static Map speedTestIpB() {
        Map map = new HashMap();
        Long st;
        Long sp;
        for (int i = 1; i <= INTB; i++) {
            st = System.currentTimeMillis();
            if (i == 1) {
                map = getIpB1();
            }
            if (i == 2) {
                map = getIpB2();
            }
            if (i == 3) {
                map = getIpB3();
            }
            sp = System.currentTimeMillis();
            if (map.get("status").equals("success")) {
                System.out.println("speedTestIpB,成功:" + map.get("content"));
            } else {
                System.out.println("speedTestIpB,失败");
            }
            System.out.println("speedTestIpB,耗时:" + (sp - st));
            System.out.println("---------------------------------------------------");
        }
        return map;
    }

    public static Map getIpB() {
        Map map = new HashMap();
        for (int i = 1; i <= INTB; i++) {
            if (i == 1) {
                map = getIpB1();
            }
            if (i == 2) {
                map = getIpB2();
            }
            if (i == 3) {
                map = getIpB3();
            }
            if (map.get("status").equals("success")) {
                break;
            }
        }
        return map;
    }

    public static Map getIpBUtil(String funName, String url, String rgex) {
        Map resultMap = new HashMap();
        logger.info(funName + ",当前url:" + url);
        Map map = getBody(url);
        if (map.get("status").equals("success")) {
            String ip = StringUtil.getSubUtilSimple((String) map.get("content"), rgex);
            if (StrUtil.isEmpty(ip)) {
                logger.info(funName + ",未截取到body区域内的IP");
                resultMap.put("status", "fail");
                resultMap.put("msg", "-->未截取到body区域内的IP");
                return resultMap;
            } else {
                logger.info(funName + ",截取body中的IP区域,区域内容:" + ip);
                map.put("msg", map.get("msg") + "-->截取body中的IP区域");
                map.put("content", ip);
                map = isIp(map);
                if (map.get("status").equals("success")) {
                    map.put("msg", map.get("msg") + "-->返回IP");
                }
                return map;
            }
        } else {
            return map;
        }
    }


    /**
     * http://pv.sohu.com/cityjson
     * var returnCitySN = {"cip": "***.***.***.***", "cid": "******", "cname": "山东省**市"};
     * 搜狐的,稳定性:99
     */
    public static Map getIpB1() {
        return getIpBUtil("getIpB1", "http://pv.sohu.com/cityjson", "\"cip\": \"(.*?)\", \"cid\"");
    }

    /**
     * http://ip.dhcp.cn/?json
     * {
     * "IP": "***.***.***.***",
     * "Address": {
     * "Country": "中国",
     * "Province": "山东省",
     * "City": "**市"
     * },
     * "ISP": "电信"
     * }
     * 个人接口,稳定性:50
     */
    public static Map getIpB2() {
        return getIpBUtil("getIpB2", "http://ip.dhcp.cn/?json", "\"IP\": \"(.*?)\", \"Address");
    }

    /**
     * https://ip.tool.lu/
     * 当前IP: ***.***.***.*** 归属地: 中国 山东省 **市
     * 稳定性:50
     */
    public static Map getIpB3() {
        return getIpBUtil("getIpB3", "https://ip.tool.lu/", "当前IP:(.*?)归属地");
    }

    /**
     * C,登录光猫控制台,获取光猫信息
     * 不过光猫的公网IP显示,估计也是调用的自家服务器吧
     * 稳定性:100,耗时:1804,1811,1786,1788,1774,1811
     * 注:这个方法用来垫底,因为耗时有点高,估计获取过程中也是调用的某个运营商的api,不过稳定性肯定最高
     * 该方法定制化比较强,不同的光猫版本,可能抓取方法都不一样,但是大致方向是一样的,获取公网IP的接口需要自己手动去找
     */
    public static Map speedTestIpC() {
        Long st = System.currentTimeMillis();
        Map map = getIpC();
        Long sp = System.currentTimeMillis();
        if (map.get("status").equals("success")) {
            System.out.println("speedTestIpC,成功:" + map.get("content"));
        } else {
            System.out.println("speedTestIpC,失败");
        }
        System.out.println("speedTestIpC,耗时:" + (sp - st));
        System.out.println("---------------------------------------------------");
        return map;
    }

    public static Map getIpC() {
        Map resultMap = new HashMap();
        String url = "http://192.168.1.1/cgi-bin/luci";
        String urlGwinfo = url + "/admin/settings/gwinfo?get=part";
        logger.info("getIpC,当前url:" + url);
        try {
            String body;
            Connection.Response res = Jsoup.connect(url).data("username", "你的账号", "psd", "你的密码").method(Connection.Method.POST).execute();
            String sessionId = res.cookie("sysauth");

            Document doc = Jsoup.connect(urlGwinfo).cookie("sysauth", sessionId).ignoreContentType(true).get();
            body = doc.body().text();
            JSONObject json = JSONUtil.parseObj(body);
            String ip = (String) json.get("WANIP");

            if (StringUtil.isIP(ip)) {
                logger.info("getIpC,获取IP成功");
                resultMap.put("status", "success");
                resultMap.put("msg", "-->获取IP成功");
                resultMap.put("url", url);
                resultMap.put("content", ip);
                return resultMap;
            } else {
                logger.info("getIpC,IP获取失败,请检查url,url:" + url);
                resultMap.put("status", "fail");
                resultMap.put("msg", "IP获取失败,请检查url,url:" + url);
                return resultMap;
            }
        } catch (IOException e) {
            logger.info("getIpC,网络异常,url:" + url);
            resultMap.put("status", "fail");
            resultMap.put("msg", "网络异常,url:" + url);
            return resultMap;
        }
    }
}
