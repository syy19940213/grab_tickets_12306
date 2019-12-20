package com.yongxin.weborder.service.protocol.ticket;

import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;

import java.util.HashMap;
import java.util.Map;

public class WebLeftTicketQueryWebProtocol extends AbstractWebProtocol
{

    private String trainDate;
    private String fromStation;
    private String toStation;
    private String fromStationName;
    private String toStationName;
    private String leftTicketUrl;

    public WebLeftTicketQueryWebProtocol(OrderHttpClient orderHttpClient, String leftTicketUrl, String trainDate, String fromStation, String toStation,
                                         String fromStationName, String toStationName)
    {
        super(orderHttpClient);
        this.trainDate = trainDate;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.fromStationName = fromStationName;
        this.toStationName = toStationName;
        this.leftTicketUrl = leftTicketUrl;
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {

        orderHttpClient.addCookie( "_jc_save_fromStation",
                escape(this.fromStationName + "," + this.fromStation));
        orderHttpClient.addCookie( "_jc_save_toStation",
                escape(this.toStationName + "," +this.toStation));
        String toDate = ObjectUtil.getDay();
        orderHttpClient.addCookie("_jc_save_fromDate", this.trainDate);
        orderHttpClient.addCookie( "_jc_save_toDate", toDate);
        orderHttpClient.addCookie( "_jc_save_wfdc_flag", "dc");
        String url = "https://kyfw.12306.cn/otn/" + leftTicketUrl;
        Map<Integer,Object> returnMap = new HashMap<>();
        addHeader("Host","kyfw.12306.cn");
        addHeader("X-Requested-With","XMLHttpRequest");
        addHeader("Sec-Fetch-Site","same-origin");
        addHeader("Sec-Fetch-Mode","cors");
        addHeader("Accept", "*/*");
        addHeader("Referer", "https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Pragma","no-cache");


        addParams("leftTicketDTO.train_date", this.trainDate);
        addParams("leftTicketDTO.from_station", this.fromStation);
        addParams("leftTicketDTO.to_station", this.toStation);
        addParams("purpose_codes", "ADULT");
        Pair<Integer,String> pair = execute(url,GET);
        if (ObjectUtil.getInt(pair.left) == 200)
        {
            if (pair.right.startsWith("{"))
            {
                JSONObject obj = JSONObject.parseObject(pair.right);
                String data = obj.getString("data");
                returnMap.put(ResultEnum.LEFTTICKET_QUREY.getCode(),data);
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
