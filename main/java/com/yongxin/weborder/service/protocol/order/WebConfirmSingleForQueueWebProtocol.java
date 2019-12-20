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

public class WebConfirmSingleForQueueWebProtocol extends AbstractWebProtocol
{
    private String passengerTicketStr;
    private String oldPassengerStr;
    private String purposeCodes;
    private String keyCheckIsChange;
    private String leftTicketStr;
    private String trainLocation;
    private String chooseSeats;
    private String submitToken;

    public WebConfirmSingleForQueueWebProtocol(OrderHttpClient orderHttpClient,String passengerTicketStr,String oldPassengerStr,
                                               String purposeCodes,String keyCheckIsChange,String leftTicketStr,String trainLocation,
                                               String chooseSeats,String submitToken) throws ProtocolException
    {
        super(orderHttpClient);
        this.passengerTicketStr = passengerTicketStr;
        this.oldPassengerStr = oldPassengerStr;
        this.purposeCodes = purposeCodes;
        this.keyCheckIsChange = keyCheckIsChange;
        this.leftTicketStr = leftTicketStr;
        this.trainLocation = trainLocation;
        this.chooseSeats = ObjectUtil.getString(chooseSeats);
        this.submitToken = submitToken;
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
        addHeader("Host","kyfw.12306.cn");
        addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        addHeader("Origin","https://kyfw.12306.cn");
        addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");

        addParams("passengerTicketStr",this.passengerTicketStr);
        addParams("oldPassengerStr",this.oldPassengerStr);
        addParams("randCode","");
        addParams("purpose_codes",this.purposeCodes);
        addParams("key_check_isChange",this.keyCheckIsChange);
        addParams("leftTicketStr",this.leftTicketStr);
        addParams("train_location",this.trainLocation);
        addParams("choose_seats",this.chooseSeats);
        addParams("seatDetailType","000");
        addParams("whatsSelect","1");
        addParams("roomType","00");
        addParams("dwAll","N");
        addParams("_json_att","");
        addParams("REPEAT_SUBMIT_TOKEN",this.submitToken);
        Pair<Integer,String> pair = execute(url,POST_BY_FORM);
        if (ObjectUtil.getInt(pair.left) == 200)
        {
            if (pair.right.startsWith("{"))
            {
                JSONObject obj = JSONObject.parseObject(pair.right);
                if (obj.getBoolean("status"))
                {
                    String data = obj.getString("data");
                    returnMap.put(ResultEnum.CONFIRM_SUCCESS.getCode(),data);
                    return returnMap;
                }
                else
                {
                    throw  new ProtocolException(ResultEnum.CONFIRMQUEUE_ERROR.getCode(),obj.getString("data"));
                }

            }
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
