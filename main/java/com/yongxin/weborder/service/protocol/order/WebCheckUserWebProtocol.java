package com.yongxin.weborder.service.protocol.order;

import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;

import java.util.HashMap;
import java.util.Map;

public class WebCheckUserWebProtocol extends AbstractWebProtocol
{
    public WebCheckUserWebProtocol(OrderHttpClient orderHttpClient)
    {
        super(orderHttpClient);
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/login/checkUser";
        addHeader("Host","kyfw.12306.cn");
        addHeader("Origin","https://kyfw.12306.cn");
        addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        addHeader("Accept","*/*");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");

        Pair<Integer, String> result = execute(url,POST_BY_FORM);
        Integer code = result.left;
        String msg = result.right;
        if (code == 200)
        {
            if (msg.startsWith("{"))
            {
                JSONObject object = JSONObject.parseObject(msg);
                JSONObject data = object.getJSONObject("data");
                boolean flag = ObjectUtil.getBoolean(data.get("flag"));
                if (flag)
                {
                    returnMap.put(ResultEnum.CHECK_URSER.getCode(),flag);
                    return returnMap;
                }
            }
        }
        else if (code == 302)
        {
            throw new ProtocolException(ResultEnum.HTTP_302);
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
