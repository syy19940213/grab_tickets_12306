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

public class WebQueryMyOrderNoCompleteWebProtocol extends AbstractWebProtocol
{
    public WebQueryMyOrderNoCompleteWebProtocol(OrderHttpClient orderHttpClient)
    {
        super(orderHttpClient);
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/queryOrder/queryMyOrderNoComplete";
        addHeader("Host","kyfw.12306.cn");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        addHeader("Origin","https://kyfw.12306.cn");
        addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addParams("_json_att","");

        Pair<Integer,String> pair = execute(url,POST_BY_FORM);
        if (ObjectUtil.getInt(pair.left) == 200)
        {
            if (pair.right.startsWith("{"))
            {
                JSONObject obj = JSONObject.parseObject(pair.right);
                String data = obj.getString("data");
                returnMap.put(ResultEnum.NO_COMPLETE_ORDER.getCode(),data);
                return returnMap;
            }
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
