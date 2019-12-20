package com.yongxin.weborder.thread;

import com.yongxin.weborder.cache.StationCache;
import com.yongxin.weborder.service.Protocol;
import com.yongxin.weborder.service.impl.WebProtocolImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetStationThread extends Thread
{

    private Logger logger = LoggerFactory.getLogger(GetStationThread.class);

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Protocol protocol = new WebProtocolImpl();
                protocol.init(null);
                String stationStr = protocol.getStation();
                StationCache.parseStaionStr(stationStr);
                logger.info("获取站点信息完成");
                return;
            }
            catch (Exception e)
            {
                logger.error("获取站点信息失败",e);
            }
        }
    }
}
