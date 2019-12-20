package com.yongxin.weborder.service.protocol.login;

import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;
import java.util.HashMap;
import java.util.Map;

public class WebAuthUamtkWebProtocol extends AbstractWebProtocol
{
    public WebAuthUamtkWebProtocol(OrderHttpClient orderHttpClient)
    {
        super(orderHttpClient);
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer, Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/passport/web/auth/uamtk";

        addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        addHeader("Host","kyfw.12306.cn");
        addHeader("Origin","https://kyfw.12306.cn");
        addHeader("Referer","https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin");

        addParams("appid","otn");

        Pair<Integer,String> result  = execute(url,POST_BY_FORM);
        Integer httpCode = result.left;
        if (httpCode == 200)
        {
            JSONObject obj = JSONObject.parseObject(result.right);
            Integer resultCode = obj.getInteger("result_code");
            if (resultCode == 0 )
            {
                String newapptk = obj.getString("newapptk");
                returnMap.put(ResultEnum.AUTH_UAMTK.getCode(),newapptk);
                return returnMap;
            }

        }
        else if (httpCode == 302)
        {
            throw new ProtocolException(ResultEnum.HTTP_302);
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
