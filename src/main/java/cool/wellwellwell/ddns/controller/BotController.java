package cool.wellwellwell.ddns.controller;

import cool.wellwellwell.ddns.service.BotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Auther: auther
 * @Date: 2021/1/14 19:50
 * @Description:基本没用,要什么controller,这可是机器人,直接跟他聊天就行了
 */

@RestController
public class BotController {
    private static final Logger logger = LoggerFactory.getLogger(BotController.class);

    @Autowired
    private BotService botService;

    /*@GetMapping("test")
    public void test() {
        logger.info("test开始");
        Map map = botService.updateIP();
        System.out.println(map.toString());
    }*/
}
