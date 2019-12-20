package com.yongxin.weborder.service.schedule;

import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.impl.GetJsCookieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 获取cookie 要用的url
 */
@Component
public class CookieUrlSchedule
{

    private Logger logger = LoggerFactory.getLogger(CookieUrlSchedule.class);

    public static String cookieUrl = null;

    private long endTime;


    @Autowired
    private GetJsCookieService getJsCookieService;

    /**
     * 十秒钟获取一次url
     */
    @Scheduled(fixedDelay= 60 * 1000)
    public void getCookieUrlTask()
    {
        try
        {
            if (ObjectUtil.isNull(cookieUrl) || isNeedUpdate())
            {

                String url = this.getJsCookieService.getCookieUrl(null,null);
                if (ObjectUtil.isNotNull(url))
                {
                    logger.info("获取到cookieUrl:" + url);
                    CookieUrlSchedule.cookieUrl = url;
                    endTime = System.currentTimeMillis();
                }
            }
        }
        catch (Exception e)
        {
            logger.error("获取cookieUrl出错",e);
        }
    }

    /**
     * 是否需要更新
     * @return
     */
    private boolean isNeedUpdate()
    {
        if (ObjectUtil.getLong(endTime) == 0)
        {
            return true;
        }
        if (System.currentTimeMillis() - endTime > 30 * 60 * 1000)
        {
            return true;
        }
        return false;
    }
}
