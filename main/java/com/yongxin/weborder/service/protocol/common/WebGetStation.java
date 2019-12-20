package com.yongxin.weborder.service.protocol.common;

import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;

import java.util.HashMap;
import java.util.Map;

public class WebGetStation extends AbstractWebProtocol
{


    public WebGetStation(OrderHttpClient client)
    {
        super(client);
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();

        String url = "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js";
        addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Host","kyfw.12306.cn");
        addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");

        addParams("station_version","1.9125");

        Pair<Integer, String> pair =  this.execute(url,GET);
        if (ObjectUtil.getInt(pair.left) == 200)
        {
            String msg =   pair.right;
            msg = msg.replace("var station_names ='","");
            msg = msg.replace("';","");
            returnMap.put(ResultEnum.GET_STATION.getCode(),msg);
            return returnMap;
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
