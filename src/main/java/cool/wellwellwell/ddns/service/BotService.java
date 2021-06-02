package cool.wellwellwell.ddns.service;

import cn.hutool.core.util.StrUtil;
import cool.wellwellwell.ddns.contants.Global;
import cool.wellwellwell.ddns.utils.DnsUtil;
import cool.wellwellwell.ddns.utils.IpUtils;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.PlainText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: auther
 * @Date: 2021/1/17 11:23
 * @Description:
 */
@Service
public class BotService {
    private static final Logger logger = LoggerFactory.getLogger(BotService.class);

    @Autowired
    Global global;

    @Autowired
    private DnsUtil dnsUtil;

    /**
     * 发送群消息
     *
     * @return map status-状态;msg-执行信息;result-返回值
     */
    public Map sendGroupMessage(Long groupId, String content) {
        Map resultMap = new HashMap();
        if (groupId != null && content != null && content != "") {
            MessageReceipt messageReceipt = global.getWizardBot().getGroup(groupId).sendMessage(new PlainText(content));
            if (messageReceipt.isToGroup()) {
                logger.info("sendMessage,success");
                resultMap.put("status", "success");
                resultMap.put("msg", "success");
                resultMap.put("result", "success");
                return resultMap;
            }
            logger.info("sendMessage,没有发到群");
            resultMap.put("status", "fail");
            resultMap.put("msg", "没有发到群");
            return resultMap;
        } else {
            logger.info("sendMessage,群号或内容不能为空");
            resultMap.put("status", "fail");
            resultMap.put("msg", "群号或内容不能为空");
            return resultMap;
        }
    }

    /**
     * ddns动态修改域名ip
     * 获取域名绑定ip
     * 获取当前公网ip
     * 进行比较
     * 如果不一样则进行动态修改
     *
     * @return map status-状态;msg-执行信息;result-返回值
     */
    public Map updateIP() {
        Map resultMap = new HashMap();
        String host = "service.wellwellwell.cool";
        //获取域名绑定ip
        String domainIp = dnsUtil.getIpByHost(host);
        if (StrUtil.hasBlank(domainIp)) {
            logger.info("updateIP,查询出错或域名不存在,请检查您的域名");
            resultMap.put("status", "fail");
            resultMap.put("msg", "查询出错或域名不存在,请检查您的域名");
            return resultMap;
        }
        logger.info("updateIP,获取到域名绑定的ip");
        //获取当前公网ip
        Map mapIp = IpUtils.getIp();
        String ip;
        if (mapIp.get("status").equals("success")) {
            ip = (String) mapIp.get("content");
        } else {
            return mapIp;
        }

        if (!domainIp.equals(ip)) {
            String result = dnsUtil.updateBindIp(host, ip);
            if ("success".equals(result)) {
                logger.info("updateIP,更新成功,新的公网IP:" + ip);
                resultMap.put("status", "success");
                resultMap.put("msg", "更新成功,新的公网IP:" + ip);
                return resultMap;
            } else {
                logger.info("updateIP,更新失败,请检查");
                resultMap.put("status", "fail");
                resultMap.put("msg", "更新失败,请检查");
                return resultMap;
            }
        } else {
            logger.info("updateIP,无需更新");
            resultMap.put("status", "nothing");
            resultMap.put("msg", "无需更新");
            return resultMap;
        }
    }

    /**
     * 获取当前公网ip
     *
     * @return map status-状态;msg-执行信息;result-返回值
     */
    public Map getIp() {
        return IpUtils.getIp();
    }

}
