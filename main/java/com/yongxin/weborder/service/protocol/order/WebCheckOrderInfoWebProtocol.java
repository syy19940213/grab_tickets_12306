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

public class WebCheckOrderInfoWebProtocol extends AbstractWebProtocol
{
    private String passengerTicketStr;
    private String oldPassengerStr;
    private String submitToken;


    public WebCheckOrderInfoWebProtocol(OrderHttpClient orderHttpClient, String passengerTicketStr, String oldPassengerStr, String submitToken)
    {
        super(orderHttpClient);
        this.passengerTicketStr = passengerTicketStr;
        this.oldPassengerStr = oldPassengerStr;
        this.submitToken = submitToken;
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url ="https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
        addHeader("Host","kyfw.12306.cn");
        addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        addHeader("Origin","https://kyfw.12306.cn");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addParams("cancel_flag","2");
        addParams("bed_level_order_num","000000000000000000000000000000");
        addParams("passengerTicketStr",this.passengerTicketStr);
        addParams("oldPassengerStr",this.oldPassengerStr);
        addParams("tour_flag","dc");
        addParams("randCode","");
        addParams("whatsSelect","1");
        addParams("_json_att","");
        addParams("REPEAT_SUBMIT_TOKEN",this.submitToken);
        Pair<Integer,String> pair = execute(url,POST_BY_FORM);
        if (ObjectUtil.getInt(pair.left) == 200)
        {
            if (pair.right.startsWith("{"))
            {
                JSONObject object = JSONObject.parseObject(pair.right);
                if (object.getBoolean("status"))
                {
                    String data = object.getString("data");
                    returnMap.put(ResultEnum.CHECKORDER.getCode(), data);
                    return returnMap;
                }
            }
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
