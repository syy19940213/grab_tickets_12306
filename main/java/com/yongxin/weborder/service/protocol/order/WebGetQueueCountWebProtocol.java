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

public class WebGetQueueCountWebProtocol extends AbstractWebProtocol
{
    private String trainDate;
    private String trainNo;
    private String stationTrainCode;
    private String seatType;
    private String fromStationTelecode;
    private String toStationTelecode;
    private String leftTicket;
    private String purposeCodes;
    private String trainLocation;
    private String submitToken;

    public WebGetQueueCountWebProtocol(OrderHttpClient orderHttpClient,String trainDate, String trainNo, String stationTrainCode, String seatType, String fromStationTelecode
                            , String toStationTelecode, String leftTicket, String purposeCodes, String trainLocation, String submitToken)
    {
        super(orderHttpClient);
        this.trainDate = trainDate;
        this.trainNo = trainNo;
        this.stationTrainCode = stationTrainCode;
        this.seatType = seatType;
        this.fromStationTelecode = fromStationTelecode;
        this.toStationTelecode = toStationTelecode;
        this.leftTicket = leftTicket;
        this.purposeCodes = purposeCodes;
        this.trainLocation = trainLocation;
        this.submitToken = submitToken;
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
        addHeader("Host","kyfw.12306.cn");
        addHeader("Origin","https://kyfw.12306.cn");
        addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Referer","https://kyfw.12306.cn/otn/confirmPassenger/initDc");

        addParams("train_date",getChromeTrainDateString(this.trainDate));
        addParams("train_no",this.trainNo);
        addParams("stationTrainCode",this.stationTrainCode);
        addParams("seatType",this.seatType);
        addParams("fromStationTelecode",this.fromStationTelecode);
        addParams("toStationTelecode",this.toStationTelecode);
        addParams("leftTicket",this.leftTicket);
        addParams("purpose_codes",this.purposeCodes);
        addParams("train_location",this.trainLocation);
        addParams("json_att","");
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
                    returnMap.put(ResultEnum.GETQUEUE_SUCCESS.getCode(),data);
                    return returnMap;
                }
                else
                {
                    throw new ProtocolException(ResultEnum.GET_QUEUE_COUNT_ERROR.getCode(),object.getString("messages"));
                }

            }
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
