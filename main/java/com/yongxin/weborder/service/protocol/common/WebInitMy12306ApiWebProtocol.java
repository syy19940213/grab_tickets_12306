package com.yongxin.weborder.service.protocol.common;

import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;

import java.util.HashMap;
import java.util.Map;

public class WebInitMy12306ApiWebProtocol extends AbstractWebProtocol
{
    public WebInitMy12306ApiWebProtocol(OrderHttpClient orderHttpClient)
    {
        super(orderHttpClient);
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/index/initMy12306Api";
        addHeader("Accept","*/*");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Cache-Control","no-cache");
        addHeader("Host","kyfw.12306.cn");
        addHeader("Origin","https://kyfw.12306.cn");

        Pair<Integer, String> pair = execute(url,POST);
        Integer code = ObjectUtil.getInt(pair.left);
        String content = pair.right;
        if (code == 200)
        {
            if (content.startsWith("{"))
            {
                JSONObject object = JSONObject.parseObject(content);
                String data = object.getString("data");
                returnMap.put(ResultEnum.USER_INFO.getCode(),data);
            }
            else
            {
                return retrunUnkownError(content);
            }
        }
        else
        {
            return retrunUnkownError(content);
        }
        return returnMap;
    }
}
