package com.yongxin.weborder.service.protocol.ticket;

import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;

import java.util.HashMap;
import java.util.Map;

public class WebLeftTicketsInitWebProtocol extends AbstractWebProtocol
{

    public WebLeftTicketsInitWebProtocol(OrderHttpClient orderHttpClient)
    {
        super(orderHttpClient);
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {

        String url = "https://kyfw.12306.cn/otn/leftTicket/init";
        addHeader("Host","kyfw.12306.cn");
        addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Referer", "https://www.12306.cn/index/");
        addParams("linktypeid", "dc");
        Pair<Integer,String> pair = execute(url,GET);
        if (ObjectUtil.getInt(pair.left) == 200)
        {
            Map<Integer,Object> returnMap = new HashMap<>();
            String CLeftTicketUrl = ObjectUtil.regString("(?<=var CLeftTicketUrl = ').*?(?=')", pair.right);
            returnMap.put(ResultEnum.LEFTTICKET_INIT.getCode(),CLeftTicketUrl);
            return returnMap;
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
