package cool.wellwellwell.ddns.task;

import cool.wellwellwell.ddns.contants.Global;
import cool.wellwellwell.ddns.service.BotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BotSchedulerTask {
    private static final Logger logger = LoggerFactory.getLogger(BotSchedulerTask.class);

    @Autowired
    private Global global;

    @Autowired
    private BotService botService;

    // */1 * * * * ? 每秒执行一次
    // 0 0 */1 * * ? 每1小时执行一次,每个整点执行
    @Scheduled(cron = "0 0 */1 * * ?")
    private void autoUpdateIp() {
        logger.info("autoUpdateIp");
        Map map = botService.updateIP();
        if (global.getStartStatus() == 1) {
            if (!map.get("status").equals("nothing")) {
                botService.sendGroupMessage(global.getGroupnumber(), (String) map.get("msg"));
            }
        }
    }
}
