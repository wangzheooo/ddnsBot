package cool.wellwellwell.ddns.handle;

import cool.wellwellwell.ddns.contants.Global;
import cool.wellwellwell.ddns.service.BotService;
import cool.wellwellwell.ddns.utils.BotUtils;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotOfflineEvent;
import net.mamoe.mirai.event.events.BotReloginEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther: auther
 * @Date: 2021/1/23 11:23
 * @Description:
 */
@Component
public class EventHandle extends SimpleListenerHost {
    private static final Logger logger = LoggerFactory.getLogger(EventHandle.class);

    @Autowired
    private Global global;

    @Autowired
    private BotService botService;

    @NotNull
    @EventHandler
    public ListeningStatus getGroupMessage(@NotNull GroupMessageEvent event) {
        MessageChain message = event.getMessage();
        String messageTemp = BotUtils.filterMessage(message);

        if (messageTemp.equals(".ddns")) {
            Long groupId = event.getSubject().getId();
            global.getExecutor().execute(() ->
                    botService.sendGroupMessage(groupId, (String) (botService.updateIP()).get("msg"))
            );
            return ListeningStatus.LISTENING;
        } else if (messageTemp.equals(".ip")) {
            Long groupId = event.getSubject().getId();
            global.getExecutor().execute(() -> {
                        Map map = botService.getIp();
                        if (map.get("status").equals("success")) {
                            botService.sendGroupMessage(groupId, "IP:" + map.get("content") + ",URL:" + map.get("url"));
                        } else {
                            botService.sendGroupMessage(groupId, (String) map.get("msg"));
                        }
                    }
            );
            return ListeningStatus.LISTENING;
        }
        return ListeningStatus.LISTENING;
    }

    @NotNull
    @EventHandler
    public void getBotOffline(@NotNull BotOfflineEvent event) throws Exception {
        logger.error("掉了");
    }

    @NotNull
    @EventHandler
    public void getBotOffline(@NotNull BotReloginEvent event) throws Exception {
        logger.error("重新登录");
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        // 处理事件处理时抛出的异常
        logger.error("异常" + context + "\n原因:" + exception.toString());
        throw new RuntimeException("在事件处理中发生异常", exception);
    }

}
