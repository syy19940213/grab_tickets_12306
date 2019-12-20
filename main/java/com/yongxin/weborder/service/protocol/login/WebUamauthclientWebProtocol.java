package com.yongxin.weborder.service.protocol.login;

import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;

import java.util.HashMap;
import java.util.Map;

public class WebUamauthclientWebProtocol extends AbstractWebProtocol
{

    private String tk;

    public WebUamauthclientWebProtocol(OrderHttpClient orderHttpClient, String tk)
    {
        super(orderHttpClient);
        this.tk = tk;
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer, Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/otn/uamauthclient";
        addHeader("Accept","*/*");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        addHeader("Host","kyfw.12306.cn");
        addHeader("Origin","https://kyfw.12306.cn");
        addParams("tk",tk);
        Pair<Integer,String> map = execute(url,POST_BY_FORM);
        Integer code = map.left;
        if (code == 200)
        {
            returnMap.put(ResultEnum.UAMTK_CLIENT.getCode(),map.right);
            return returnMap;
        }
        else if (code == 302)
        {
            throw new ProtocolException(ResultEnum.HTTP_302);
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }

}
