package com.yongxin.weborder.service.task;

import com.yongxin.weborder.bean.HttpProxy;
import com.yongxin.weborder.cache.ProxyCache;
import com.yongxin.weborder.service.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
public class CheckProxyTask
{

    private Logger logger = LoggerFactory.getLogger(CheckProxyTask.class);

    @Autowired
    private Protocol protocol;



//    @Scheduled(fixedDelay=2* 60 * 1000)
    public void checkProxy()
    {
        Set<HttpProxy> list = ProxyCache.getProxy();
        List<HttpProxy>  usableProxy = ProxyCache.getUsableList();
        list.addAll(usableProxy);
        for (HttpProxy proxy : list)
        {
            try
            {
                protocol.init(proxy);
                protocol.initLeftTickets();
            }
            catch (Exception e)
            {
                ProxyCache.removeUsable(proxy);
            }
        }
    }
}
