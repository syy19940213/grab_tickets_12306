package com.yongxin.weborder.service.schedule;

import com.yongxin.weborder.service.proxy.ProxyService;
import com.yongxin.weborder.utils.TrainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
public class GetProxySchedule
{

    private Logger logger = LoggerFactory.getLogger(GetProxySchedule.class);



    @Autowired
    private ProxyService proxyService;

    @Scheduled(fixedDelay= 5 * 60 * 1000)
    public void getProxyIp()
    {
        if (!TrainUtils.isTime12306())
        {
            return;
        }
        try
        {
            this.proxyService.getSelfProxy();
        }
        catch (Exception e)
        {
            logger.error("获取ip3366出错",e);
        }
    }


}
