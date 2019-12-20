package com.yongxin.weborder.service.protocol.order;

import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;

import java.util.HashMap;
import java.util.Map;

public class WebQueryOrderWaitTimeWebProtocol extends AbstractWebProtocol
{
    private String submitToken;

    public WebQueryOrderWaitTimeWebProtocol(OrderHttpClient orderHttpClient, String submitToken)
    {
        super(orderHttpClient);
        this.submitToken = submitToken;
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime";
        addHeader("Host","kyfw.12306.cn");
        addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");

        addParams("random",System.currentTimeMillis());
        addParams("tourFlag","dc");
        addParams("_json_att","");
        addParams("REPEAT_SUBMIT_TOKEN",this.submitToken);

        Pair<Integer,String> pair = execute(url,GET);
        if (ObjectUtil.getInt(pair.left) == 200)
        {
            if (pair.right.startsWith("{"))
            {
                JSONObject object = JSONObject.parseObject(pair.right);
                String data = object.getString("data");
                returnMap.put(ResultEnum.GET_WAIT_QUEUE.getCode(),data);
                return returnMap;
            }
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
