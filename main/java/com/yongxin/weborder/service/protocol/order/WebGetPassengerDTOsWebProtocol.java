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

public class WebGetPassengerDTOsWebProtocol extends AbstractWebProtocol
{
    private String submitToken;

    public WebGetPassengerDTOsWebProtocol(OrderHttpClient orderHttpClient, String submitToken)
    {
        super(orderHttpClient);
        this.submitToken = submitToken;
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";
        addHeader("Host","kyfw.12306.cn");
        addHeader("Accept","*/*");
        addHeader("Origin","https://kyfw.12306.cn");
        addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addParams("REPEAT_SUBMIT_TOKEN",this.submitToken);
        addParams("_json_att","");

        Pair<Integer, String> pair = execute(url,POST_BY_FORM);
        if (
                ObjectUtil.getInt(pair.left) == 200)
        {
            if (pair.right.startsWith("{"))
            {
                JSONObject object = JSONObject.parseObject(pair.right);
                String data = object.getString("data");
                returnMap.put(ResultEnum.PASSENGER_DTO.getCode(),data);
                return returnMap;
            }
        }
        else if (ObjectUtil.getInt(pair.left) == 302)
        {
            throw new ProtocolException(ResultEnum.HTTP_302);
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
