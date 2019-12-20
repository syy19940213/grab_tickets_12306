package com.yongxin.weborder.service.protocol.common;

import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;
import java.util.HashMap;
import java.util.Map;

public class WebLoginConfWebProtocol extends AbstractWebProtocol
{
    public WebLoginConfWebProtocol(OrderHttpClient orderHttpClient)
    {
        super(orderHttpClient);
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/login/conf";
        addHeader("Cache-Control", "max-age=0");
        addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        addHeader("Content-Type", "application/x-www-form-urlencoded");
        addHeader("Accept-Encoding", "gzip, deflate, sdch, br");
        addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        addHeader("Upgrade-Insecure-Requests", "1");
        addHeader("Referer", "https://kyfw.12306.cn/otn/resources/login.html");
        addHeader("Origin", "https://kyfw.12306.cn");
        Pair<Integer,String> result = execute(url,GET);
        if (result.left == 200)
        {
            JSONObject obj = JSONObject.parseObject(result.right);
            String data = obj.getString("data");
            returnMap.put(ResultEnum.LOGIN_CONF.getCode(),data);
            return returnMap;
        }
        else if (result.left == 302)
        {
            throw new ProtocolException(ResultEnum.HTTP_302);
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
