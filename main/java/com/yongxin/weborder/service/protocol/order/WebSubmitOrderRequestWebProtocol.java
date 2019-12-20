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

public class WebSubmitOrderRequestWebProtocol extends AbstractWebProtocol
{
    private String secretStr;
    private String trainDate;
    private String queryFromStationName;
    private String queryToStationName;



    public WebSubmitOrderRequestWebProtocol(OrderHttpClient orderHttpClient
                                , String secretStr, String trainDate,  String queryFromStationName, String queryToStationName)
    {
        super(orderHttpClient);
        this.secretStr = secretStr;
        this.trainDate = trainDate;
        this.queryFromStationName = queryFromStationName;
        this.queryToStationName = queryToStationName;
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {

        this.orderHttpClient.addCookie("_jc_save_showIns","true");

        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
        addHeader("Host","kyfw.12306.cn");
        addHeader("Accept","*/*");
        addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Origin","https://kyfw.12306.cn");
        addHeader("X-Requested-With", "XMLHttpRequest");
        addHeader("Referer","https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc");

        addParams("secretStr",secretStr);
        addParams("train_date",trainDate);
        addParams("back_train_date",ObjectUtil.getDay());
        addParams("tour_flag","dc");
        addParams("purpose_codes","ADULT");
        addParams("query_from_station_name",queryFromStationName);
        addParams("query_to_station_name",queryToStationName);
        addParams("undefined","");

        Pair<Integer,String> reslut = execute(url,POST_BY_FORM);
        Integer code = reslut.left;
        String msg = reslut.right;
        if (code == 200)
        {
            if (msg.startsWith("{"))
            {
                JSONObject object = JSONObject.parseObject(msg);
                if (object.getBoolean("status"))
                {
                    returnMap.put(ResultEnum.SUBMIT_SUCCESS.getCode(),msg);
                    return returnMap;
                }
                else
                {
                    throw new ProtocolException(ResultEnum.SUBMITORDER_ERROR.getCode(),object.getString("messages"));
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
