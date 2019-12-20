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
 * 验证码验证
 */
public class WebCaptchaCheckWebProtocol extends AbstractWebProtocol
{
    private String answer = null;

    public WebCaptchaCheckWebProtocol(OrderHttpClient orderHttpClient, String answer)
    {
        super(orderHttpClient);
        this.answer = answer;
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        String url = "https://kyfw.12306.cn/passport/captcha/captcha-check";
        Map<Integer,Object> returnMap = new HashMap<>();
        addHeader("Accept","*/*");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Host","kyfw.12306.cn");

        addParams("answer",this.answer);
        addParams("rand","sjrand");
        addParams("login_site","E");
        addParams("_",System.currentTimeMillis());

        Pair<Integer,String> result = execute(url,GET);

        if (result.left == 200)
        {
            String json = result.right;
            if (json.startsWith("{"))
            {
                JSONObject jsonObject = JSONObject.parseObject(json);
                Integer code = jsonObject.getInteger("result_code");
                String msg = jsonObject.getString("result_message");
                if (code == 7)
                {
                    throw new ProtocolException(ResultEnum.CAPTCHA_EXPIRE);
                }
                else if (code == 4)
                {
                    returnMap.put(ResultEnum.CAPTCHA_SUCCESS.getCode(),"验证码验证成功");
                    return returnMap;
                }
            }
        }
        else if (result.left == 302)
        {
            throw new ProtocolException(ResultEnum.HTTP_302);
        }
        throw new ProtocolException(ResultEnum.UNKOWN_ERROR);
    }
}
