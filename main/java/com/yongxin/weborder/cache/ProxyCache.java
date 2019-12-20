package com.yongxin.weborder.cache;

import com.yongxin.weborder.bean.HttpProxy;
import com.yongxin.weborder.common.utils.ObjectUtil;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class ProxyCache
{

    private static Logger logger = LoggerFactory.getLogger(ProxyCache.class);
    /**
     * 最大爬去代理数
     */
    private static int MAX_SIZE = 1000;

    /**
     * 爬取到的代理
     */
    private static Set<HttpProxy> proxyList = new ConcurrentHashSet<>();

    /**
     * 可用代理
     */
    private static List<HttpProxy> usableProxy = new ArrayList<>();



    public static Set<HttpProxy> getProxy()
    {
        return proxyList;
    }

    public static void addProxy(HttpProxy httpProxy)
    {
        if (proxyList.size() > MAX_SIZE)
        {
            proxyList.remove(0);
        }
        proxyList.add(httpProxy);
    }


    public static void setUsableProxy(List<HttpProxy> proxy)
    {
        usableProxy = proxy;
        logger.info("可用代理数{}" , proxy.size());
    }

    public static List<HttpProxy> getUsableList()
    {
        return usableProxy;
    }

    /**
     * 获取可用的代理
     * @return
     */
    public static HttpProxy getUsableProxy()
    {
        if (ObjectUtil.isEmpty(usableProxy))
        {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(usableProxy.size());
        return usableProxy.get(index);
    }

    public static void addUsabelProxy(HttpProxy proxy)
    {
        usableProxy.add(proxy);
        logger.info("当前可用代理数:" + usableProxy.size());

    }

    public synchronized static void removeUsable(HttpProxy httpProxy)
    {
        usableProxy.remove(httpProxy);
    }
}
