package com.yongxin.weborder.service.protocol.order;

import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;

import java.util.HashMap;
import java.util.Map;

public class WebInitDcWebProtocol extends AbstractWebProtocol
{

    public WebInitDcWebProtocol(OrderHttpClient orderHttpClient)
    {
        super(orderHttpClient);
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/confirmPassenger/initDc";
        addHeader("Host","kyfw.12306.cn");
        addHeader("Origin","https://kyfw.12306.cn");
        addHeader("Content-Type","application/x-www-form-urlencoded");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addParams("_json_att","");
        Pair<Integer,String> pair = execute(url,POST_BY_FORM);
        if (ObjectUtil.getInt(pair.left) == 200)
        {
            String content = pair.right;
            String globalRepeatSubmitToken = ObjectUtil.regString("(?<=var globalRepeatSubmitToken = ').*?(?=')", content);
            String leftTicketStr = ObjectUtil.regString("(?<='leftTicketStr':').*?(?=')", content);
            String key_check_isChange = ObjectUtil.regString("(?<='key_check_isChange':').*?(?=')", content);
            String isAsync = ObjectUtil.regString("(?<='isAsync':').*?(?=')", content);
            String purpose_codes = ObjectUtil.regString("(?<='purpose_codes':').*?(?=')", content);
            String train_location = ObjectUtil.regString("(?<='train_location':').*?(?=')", content);
            String train_no = ObjectUtil.regString("(?<='train_no':').*?(?=')", content);

            JSONObject ticketInfoPaJ = new JSONObject();
            ticketInfoPaJ.put("leftTicketStr", leftTicketStr);
            ticketInfoPaJ.put("key_check_isChange", key_check_isChange);
            ticketInfoPaJ.put("isAsync", isAsync);
            ticketInfoPaJ.put("purpose_codes", purpose_codes);
            ticketInfoPaJ.put("train_location", train_location);
            ticketInfoPaJ.put("submitToken",globalRepeatSubmitToken);
            ticketInfoPaJ.put("train_no",train_no);
            returnMap.put(ResultEnum.INITDC.getCode(),ticketInfoPaJ);
            return returnMap;
        }
        else if (ObjectUtil.getInt(pair.left) == 302)
        {
            throw new ProtocolException(ResultEnum.HTTP_302);
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }

}
