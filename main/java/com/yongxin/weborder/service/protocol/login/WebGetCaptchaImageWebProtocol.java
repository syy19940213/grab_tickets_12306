package com.yongxin.weborder.service.protocol.login;

import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取验证码
 */
public class WebGetCaptchaImageWebProtocol extends AbstractWebProtocol
{
    public WebGetCaptchaImageWebProtocol(OrderHttpClient orderHttpClient)
    {
        super(orderHttpClient);
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        Map<Integer,Object> returnMap = new HashMap<>();
        String url = "https://kyfw.12306.cn/passport/captcha/captcha-image64";
        addHeader("Accept","text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Host","kyfw.12306.cn");
        addHeader("X-Requested-With","XMLHttpRequest");
        addParams("login_site","E");
        addParams("module","login");
        addParams("rand","sjrand");
        addParams("_",System.currentTimeMillis());
        Pair<Integer,String> result = execute(url,GET);
        if (result.left == 200)
        {
            String json = result.right;
            if (json.startsWith("{"))
            {
                JSONObject jsonObject = JSONObject.parseObject(json);
                String image = jsonObject.getString("image");
                returnMap.put(ResultEnum.CAPTCHA_IMAGE.getCode(),image);
                return returnMap;
            }
        }
        else if (result.left == 302)
        {
            throw new ProtocolException(ResultEnum.HTTP_302);
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }

}
