package com.yongxin.weborder.common.utils;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpProtocolParams;

import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

public class GtHttpUtil
{
    public static DefaultHttpClient initClient() throws Exception
    {
        return initClientWithOutTime(3, 3);
    }

    /**
     * 初始化client
     * @param connTimeOut
     * @param soTimeOut
     * @return
     * @throws Exception
     */
    public static DefaultHttpClient initClientWithOutTime(int connTimeOut, int soTimeOut) throws Exception
    {
        KeyStore localKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        localKeyStore.load(null, null);

        HttpClientTools.EasySSLSocketFactory localSSLSocketFactoryEx = new HttpClientTools.EasySSLSocketFactory(localKeyStore);
        localSSLSocketFactoryEx.setHostnameVerifier(HttpClientTools.EasySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        BasicHttpParams localBasicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(localBasicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(localBasicHttpParams, "UTF-8");

        SchemeRegistry localSchemeRegistry = new SchemeRegistry();
        localSchemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        localSchemeRegistry.register(new Scheme("https", localSSLSocketFactoryEx, 443));

        ThreadSafeClientConnManager tcc = new ThreadSafeClientConnManager(localBasicHttpParams, localSchemeRegistry);
        tcc.setDefaultMaxPerRoute(1000);
        tcc.setMaxTotal(1000 * 1000);

        DefaultHttpClient client = new DefaultHttpClient(tcc, localBasicHttpParams);
        client.getConnectionManager().closeIdleConnections(50 * 1000, TimeUnit.MILLISECONDS);
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connTimeOut * 1000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeOut * 1000);
        client.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        return client;
    }
}
