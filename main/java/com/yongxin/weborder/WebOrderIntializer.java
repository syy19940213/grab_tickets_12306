package com.yongxin.weborder;

import com.yongxin.weborder.thread.GetStationThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

@Repository
public class WebOrderIntializer implements ApplicationListener
{

    private static Boolean init = false;

    private Logger logger = LoggerFactory.getLogger(WebOrderIntializer.class);


    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (init) {
            return;
        }
        synchronized (init) {
            // TODO 网上资料显示该接口会调两次，但实际仅有一次，可能与版本相关，有待长期验证是不是特定场景触发
            if (init) {
                logger.error("tomcat has bean initialized ! ");
                return;
            }
            try {
                init = true;


                // 获取站点信息
                new GetStationThread().start();

            } catch (Exception e) {
                logger.error("", e);
                System.exit(1);
            }
        }
    }

}
