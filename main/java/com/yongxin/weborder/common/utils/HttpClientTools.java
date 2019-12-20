package com.yongxin.weborder.common.utils;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClientTools
{
    public static void setProxy(DefaultHttpClient client, String proxyHost, int proxyPort, String username, String passwd) throws Exception
    {
        if (username == null)
        {
            username = "";
            passwd = "";
        }
        client.getCredentialsProvider().setCredentials(new AuthScope(proxyHost, proxyPort), new UsernamePasswordCredentials(username, passwd));
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
    }

    public static class EasySSLSocketFactory extends org.apache.http.conn.ssl.SSLSocketFactory
    {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public EasySSLSocketFactory(KeyStore paramKeyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
        {

            super(paramKeyStore);

            X509TrustManager local1 = new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] paramAnonymousArrayOfX509Certificate, String paramAnonymousString) throws CertificateException
                {
                }

                public void checkServerTrusted(X509Certificate[] paramAnonymousArrayOfX509Certificate, String paramAnonymousString) throws CertificateException
                {
                }

                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };
            this.sslContext.init(null, new TrustManager[] { local1 }, null);
        }

        @Override
        public Socket createSocket() throws IOException
        {
            return this.sslContext.getSocketFactory().createSocket();
        }

        @Override
        public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean) throws IOException, UnknownHostException
        {
            return this.sslContext.getSocketFactory().createSocket(paramSocket, paramString, paramInt, paramBoolean);
        }
    }
}
