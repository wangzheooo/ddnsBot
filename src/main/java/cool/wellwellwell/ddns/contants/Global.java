package cool.wellwellwell.ddns.contants;

import lombok.Data;
import net.mamoe.mirai.Bot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: auther
 * @Date: 2021/1/17 11:26
 * @Description:
 */
@Data
@Component
public class Global {

    @Value("${wizardBot.startStatus}")
    private Integer startStatus;

    @Value("${qq.account}")
    private String account;

    @Value("${qq.password}")
    private String password;

    @Value("${qq.groupnumber}")
    private Long groupnumber;

    @Value("${device.directoryWindows}")
    private String directoryWindows;

    @Value("${device.directoryLinux}")
    private String directoryLinux;

    private String directory;

    //机器人对象
    private Bot wizardBot;

    //线程池
    ExecutorService executor = Executors.newCachedThreadPool();

    public String getDirectory() {
        String system = System.getProperty("os.name");
        if (system.toLowerCase().startsWith("win")) {
            return getDirectoryWindows();
        } else {
            return getDirectoryLinux();
        }
    }
}
