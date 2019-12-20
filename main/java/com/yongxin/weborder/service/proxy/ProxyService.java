package com.yongxin.weborder.service.proxy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.bean.HttpProxy;
import com.yongxin.weborder.cache.ProxyCache;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.Protocol;
import com.yongxin.weborder.utils.HttpClient;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProxyService
{

    private Logger logger = LoggerFactory.getLogger(ProxyService.class);

    @Value("${http.proxy.url}")
    private String proxyUrl ;


    public boolean checkProxy(HttpProxy httpProxy)
    {
        try
        {
            HttpHost proxy = new HttpHost( httpProxy.getIp(), httpProxy.getPort());
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            DefaultHttpClient httpclient = (DefaultHttpClient) HttpClients.custom()
                    .setRoutePlanner(routePlanner)
                    .build();

            httpclient.getCredentialsProvider().setCredentials(new AuthScope(httpProxy.getIp(), httpProxy.getPort()),
                    new UsernamePasswordCredentials(httpProxy.getUsername(), httpProxy.getPassword()));
            httpclient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);

            RequestConfig requestConfig =  RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
            HttpGet httpGet = new HttpGet("https://www.12306.cn/index/index.html");
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200)
            {
                return true;
            }
        }
        catch (IOException e)
        {
        }
        return false;
    }

    public void getSelfProxy()
    {
        try
        {
            String data = HttpClient.doGet(proxyUrl);
            List<HttpProxy> proxyList = new ArrayList<>();
            if (ObjectUtil.isNotNull(data))
            {
                JSONArray array = JSONArray.parseArray(data);
                for (int i = 0; i < array.size() ;i ++)
                {
                    JSONObject object = array.getJSONObject(i);
                    String proxy = object.getString("proxy");
                    String[] args = proxy.split(":");
                    HttpProxy httpProxy = new HttpProxy();
                    httpProxy.setPort(ObjectUtil.getInt(args[1]));
                    httpProxy.setIp(args[0]);
                    proxyList.add(httpProxy);
                }
            }
            ProxyCache.setUsableProxy(proxyList);
        }
        catch (Exception e)
        {
            logger.error("获取代理失败",e);
        }

    }

}
