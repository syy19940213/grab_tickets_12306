package com.yongxin.weborder.utils;


import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.common.utils.GtHttpUtil;
import com.yongxin.weborder.common.utils.HttpClientTools;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.bean.HttpProxy;
import com.yongxin.weborder.common.bean.Pair;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.*;
import java.util.zip.GZIPInputStream;


public class OrderHttpClient
{
    private Logger logger = LoggerFactory.getLogger(OrderHttpClient.class);


    private DefaultHttpClient httpClient;

    @Setter
    @Getter
    private Map<String, String> headerMap = new HashMap<>();

    @Setter
    @Getter
    private Map<String, String> cookieMap = new HashMap<>();

    @Setter
    @Getter
    private Map<String, Object> paramMap = new LinkedHashMap<>();


    public OrderHttpClient() throws Exception
    {
        this.httpClient = GtHttpUtil.initClient();
    }


    /**
     * 执行get请求
     *
     * @param url
     * @return
     */
    public Pair<Integer,String> doGet(String url)
    {
        try
        {
            StringBuffer urlBuffer = new StringBuffer();
            urlBuffer.append(url).append("?");
            for (Map.Entry<String,Object> entry : this.paramMap.entrySet())
            {
                urlBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            String realUrl = StringUtils.removeEnd(urlBuffer.toString(),"&");
            realUrl = StringUtils.removeEnd(realUrl,"?");
            HttpGet httpGet = new HttpGet(realUrl);
            RequestConfig requestConfig = getRequestConfig();
            this.setRequstHeader(httpGet);
            httpGet.setConfig(requestConfig);
            HttpResponse response = this.httpClient.execute(httpGet);
            Pair<Integer,String> reslut = getHttpResult(response);
            return reslut;
        }
        catch (IOException e)
        {
            logger.warn("链接超时" + e.getMessage());
        }
        catch (Exception e)
        {
            logger.error("get请求出错", e);
        }
        return null;
    }


    public Pair<Integer,String> doPostByForm(String url)
    {
        try
        {
            List<BasicNameValuePair> pair =new ArrayList<BasicNameValuePair>();

            for (Map.Entry<String,Object> entry : this.paramMap.entrySet())
            {
                pair.add(new BasicNameValuePair(entry.getKey(), ObjectUtil.getString(entry.getValue())));
            }
            RequestConfig requestConfig = getRequestConfig();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new UrlEncodedFormEntity(pair,"UTF-8"));
            this.setRequstHeader(httpPost);
            HttpResponse response = this.httpClient.execute(httpPost);
            Pair<Integer,String> reslut = getHttpResult(response);
            return reslut;
        }
        catch (Exception e)
        {
            logger.error("post请求出错", e);
        }
        return null;
    }

    /**
     * 执行post请求
     * @param url
     * @return
     */
    public Pair<Integer,String> doPost(String url)
    {
        try
        {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = getRequestConfig();
            JSONObject param = new JSONObject();
            for (Map.Entry<String,Object> entry : this.paramMap.entrySet())
            {
                param.put(entry.getKey(),entry.getValue());
            }
            StringEntity stringEntity = new StringEntity(param.toJSONString(),"utf-8");
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(stringEntity);
            this.setRequstHeader(httpPost);

            HttpResponse response = this.httpClient.execute(httpPost);
            Pair<Integer,String> reslut = getHttpResult(response);
            return reslut;
        }
        catch (Exception e)
        {
            logger.error("post请求出错", e);
        }
        return null;
    }


    /**
     * 从response中读取结果
     * @param response
     * @return
     */
    private Pair<Integer, String> getHttpResult(HttpResponse response) throws Exception
    {
        Pair<Integer,String> result = new Pair<>();
        result.left = -1;
        if (ObjectUtil.isNotNull(response))
        {
            result = new Pair<>();
            HttpEntity httpEntity = response.getEntity();
            Integer httpCode =  response.getStatusLine().getStatusCode();
            String content = getResponseString(response);
            result.left = httpCode;
            result.right = content;
        }
        return result;
    }

    protected String getResponseString(HttpResponse res) throws Exception
    {
//        setReponseHeaderString(res);
        String tString = null;
        HttpEntity entity = res.getEntity();
        if (entity != null)
        {
            BufferedReader reader = null;
            InputStream is = null;
            InputStreamReader isReader = null;
            try
            {
                is = entity.getContent();
                //判断响应是否使用了Gzip压缩
                Header ceHeader = res.getFirstHeader("Content-Encoding");
                if (ceHeader != null && ceHeader.getValue().toLowerCase().contains("gzip"))
                {
                    GZIPInputStream gz = new GZIPInputStream(is);
                    isReader = new InputStreamReader(gz, HTTP.UTF_8);
                }
                else
                {
                    isReader = new InputStreamReader(is, HTTP.UTF_8);
                }
                reader = new BufferedReader(isReader);
                StringBuilder sb = new StringBuilder();
                String temp = null;
                while ((temp = reader.readLine()) != null)
                {
                    sb.append(temp);
                }
                tString = sb.toString();
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                if (is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch (IOException e)
                    {
                        logger.error("关闭输入流资源异常", e);
                    }
                }
                if (isReader != null)
                {
                    try
                    {
                        isReader.close();
                    }
                    catch (IOException e)
                    {
                        logger.error("关闭资源异常", e);
                    }
                }
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (IOException e)
                    {
                        logger.error("关闭资源异常", e);
                    }
                }
            }
        }
        return tString;
    }

    /**
     * 设置头
     * @param httpRequestBase
     */
    private void setRequstHeader(HttpRequestBase httpRequestBase)
    {
        for (Map.Entry<String, String> headerEntry : this.headerMap.entrySet())
        {
            httpRequestBase.setHeader(headerEntry.getKey(), headerEntry.getValue());
        }
    }


    /**
     * 初始化http config
     * @return
     */
    private RequestConfig getRequestConfig()
    {
        RequestConfig.Builder requestBuilder = RequestConfig.custom()
                .setConnectTimeout(3 * 1000)
                .setSocketTimeout(3 * 1000)
                .setConnectionRequestTimeout(3 * 1000);
        return requestBuilder.build();
    }


    /**
     * set http proxy
     * @param httpProxy
     */
    public void setProxy(HttpProxy httpProxy) throws Exception
    {
        HttpClientTools.setProxy(this.httpClient,httpProxy.getIp(),httpProxy.getPort(),httpProxy.getUsername(),httpProxy.getPassword());
    }

    /**
     * add http header
     * @param key
     * @param value
     */
    public void addHeader(String key,String value)
    {
        this.headerMap.put(key,value);
    }

    /**
     * 添加参数
     * @param key
     * @param value
     */
    public void addParam(String key,Object value)
    {
        this.paramMap.put(key,value);
    }


    /**
     * add http cookie
     * @param key
     * @param value
     */
    public void addCookie(String key,String value)
    {
        CookieStore store = this.httpClient.getCookieStore();
        BasicClientCookie cookie  = new BasicClientCookie(key,value);
        cookie.setPath("/");
        cookie.setDomain("kyfw.12306.cn");
        store.addCookie(cookie);
        this.httpClient.setCookieStore(store);
    }




}
