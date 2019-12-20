package com.yongxin.weborder.service.protocol.common;

import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.service.task.CookieUrlTask;
import com.yongxin.weborder.utils.OrderHttpClient;

import java.util.HashMap;
import java.util.Map;

public class WebLogDeviceWebProtocol extends AbstractWebProtocol
{
    public WebLogDeviceWebProtocol(OrderHttpClient orderHttpClient)
    {
        super(orderHttpClient);
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = CookieUrlTask.cookieUrl;
        if (ObjectUtil.isNull(url))
        {
            throw new ProtocolException(ResultEnum.NO_COOKIE_URL);
        }
        Pair<Integer, String> pair = this.execute(url,GET);
        if (ObjectUtil.getInt(pair.left) == 200)
        {
            String msg = pair.right;
            msg = msg.replace("callbackFunction('","");
            msg = msg.replace("')","");
            returnMap.put(ResultEnum.GET_DEVICE.getCode(),msg);
            return returnMap;
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
