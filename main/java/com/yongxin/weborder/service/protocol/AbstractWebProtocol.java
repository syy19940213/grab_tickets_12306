package com.yongxin.weborder.service.protocol;

import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.utils.OrderHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public abstract class AbstractWebProtocol
{

    private Logger logger = LoggerFactory.getLogger(AbstractWebProtocol.class);

    protected OrderHttpClient orderHttpClient;

    protected final String POST = "POST";

    protected final String GET = "GET";

    protected final String POST_BY_FORM = "POST_BY_FORM";

    private final String[] hex = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56",
            "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D",
            "6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84",
            "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B",
            "9C", "9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2",
            "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9",
            "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0",
            "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
            "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

    public AbstractWebProtocol(OrderHttpClient orderHttpClient)
    {
        this.orderHttpClient = orderHttpClient;
    }


    /**
     * 执行http请求
     * @param url
     * @param method
     * @return
     */
    public Pair<Integer, String> execute(String url, String method) throws ProtocolException
    {
        Pair<Integer,String> result = new Pair<>();
        result.left = -1;
        this.addCommonHeader();
        if (POST.equals(method))
        {
            result =  this.orderHttpClient.doPost(url);
        }
        else if (GET.equals(method))
        {
            result = this.orderHttpClient.doGet(url);
        }
        else if (POST_BY_FORM.equals(method))
        {
            result = this.orderHttpClient.doPostByForm(url);
        }
        this.clear();
        if (null != result && result.left == 200 && result.right.contains("网络可能存在问题"))
        {
            result.left = 302;
        }
        if (null == result)
        {
            throw new ProtocolException(ResultEnum.NETWORK_ERROR);
        }
        logger.info("协议执行返回结果{},{},{}",this.getClass().getName(),result.left,result.right);
        return result;
    }

    private void addCommonHeader()
    {
        this.orderHttpClient.getHeaderMap().put("Cache-Control","no-cache");
        this.orderHttpClient.getHeaderMap().put("Connection","keep-alive");
        this.orderHttpClient.getHeaderMap().put("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.36");

    }

    /**
     * 业务处理
     * @return
     */
    public abstract Map<Integer,Object> service() throws ProtocolException;

    public void addHeader(String key,String value)
    {
        this.orderHttpClient.getHeaderMap().put(key,value);
    }

    public void addParams(String key,Object value)
    {
        this.orderHttpClient.getParamMap().put(key,value);
    }

    /**
     * 清空header 和  params
     */
    public void clear()
    {
        this.orderHttpClient.getHeaderMap().clear();
        this.orderHttpClient.getParamMap().clear();
    }

    protected Map<Integer,Object> retrunUnkownError(String msg)
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        returnMap.put(ResultEnum.UNKOWN_ERROR.getCode(),msg);
        return returnMap;
    }


    protected String escape(String s)
    {
        StringBuffer sbuf = new StringBuffer();
        int len = s.length();
        for (int i = 0; i < len; i++)
        {
            int ch = s.charAt(i);
            if (ch == ' ')
            { // space : map to '+'
                sbuf.append('+');
            }
            else if ('A' <= ch && ch <= 'Z')
            { // 'A'..'Z' : as it was
                sbuf.append((char) ch);
            }
            else if ('a' <= ch && ch <= 'z')
            { // 'a'..'z' : as it was
                sbuf.append((char) ch);
            }
            else if ('0' <= ch && ch <= '9')
            { // '0'..'9' : as it was
                sbuf.append((char) ch);
            }
            else if (ch == '-' || ch == '_' // unreserved : as it was
                    || ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '/' || ch == '(' || ch == ')')
            {
                sbuf.append((char) ch);
            }
            else if (ch <= 0x007F)
            { // other ASCII : map to %XX
                sbuf.append('%');
                sbuf.append(hex[ch]);
            }
            else
            { // unicode : map to %uXXXX
                sbuf.append('%');
                sbuf.append('u');
                sbuf.append(hex[(ch >>> 8)]);
                sbuf.append(hex[(0x00FF & ch)]);
            }
        }
        return sbuf.toString();
    }


    /**
     * 字符串utf-8编码
     * @param str
     * @return
     */
    public String encodeStringUTF8(String str)
    {
        try
        {
            return URLEncoder.encode(str, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("字符编码错误", e);
        }
        return null;
    }


    /**
     * 转换格式
     * @param trainDate
     * @return
     */
    public String getChromeTrainDateString(String trainDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try
        {
            Date date = sdf.parse(trainDate);
            //return date.toString();
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd yyyy '00:00:00' 'GMT'Z '(中国标准时间)'", Locale.ENGLISH);
            String ds = sdf2.format(date);
            return ds;
        }
        catch (ParseException e)
        {
            logger.error("转换日期格式失败", e);
        }
        return null;
    }

}
