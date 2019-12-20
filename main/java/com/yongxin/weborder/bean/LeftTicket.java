package com.yongxin.weborder.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Data
public class LeftTicket
{

    /**
     *             ticketInfoPaJ.put("leftTicketStr", leftTicketStr);
     *             ticketInfoPaJ.put("key_check_isChange", key_check_isChange);
     *             ticketInfoPaJ.put("isAsync", isAsync);
     *             ticketInfoPaJ.put("purpose_codes", purpose_codes);
     *             ticketInfoPaJ.put("train_location", train_location);
     *             ticketInfoPaJ.put("submitToken",globalRepeatSubmitToken);
     *             ticketInfoPaJ.put("train_no",train_no);
     */


    private String leftTicketStr;
    private String keyCheckIsChange;
    private String isAsync;
    private String purposeCodes;
    private String trainLocation;
    private String submitToken;
    private String trainNo;


    public static LeftTicket parseStr(String initDcStr)
    {
        LeftTicket leftTicket = new LeftTicket();
        JSONObject obj = JSONObject.parseObject(initDcStr);
        leftTicket.setLeftTicketStr(obj.getString("leftTicketStr"));
        leftTicket.setKeyCheckIsChange(obj.getString("key_check_isChange"));
        leftTicket.setIsAsync(obj.getString("isAsync"));
        leftTicket.setPurposeCodes(obj.getString("purpose_codes"));
        leftTicket.setTrainLocation(obj.getString("train_location"));
        leftTicket.setSubmitToken(obj.getString("submitToken"));
        leftTicket.setTrainNo(obj.getString("train_no"));
        return leftTicket;
    }

    @Override
    public String toString()
    {
        return JSON.toJSONString(this);
    }
}
