package cool.wellwellwell.ddns.utils;

import net.mamoe.mirai.message.data.MessageChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: auther
 * @Date: 2021/1/17 11:59
 * @Description:
 */
public class BotUtils {
    private static final Logger logger = LoggerFactory.getLogger(BotUtils.class);

    /**
     * 消息过滤,因为只要文字信息,所以接收的信息里,除了文字都过滤
     *
     * @param message 接受的消息
     * @return messageTemp 过滤后的消息
     */
    public static String filterMessage(MessageChain message) {
        String messageTemp = "";
        for (int i = 0; i < message.size(); i++) {
            if (("" + message.get(i)).indexOf("[mirai:") == -1) {
                messageTemp += ("" + message.get(i)).replace("\r", " ").trim();
            }
        }
        return messageTemp;
    }
}
