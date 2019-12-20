package com.yongxin.weborder.service.protocol.login;

import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.bean.ResultEnum;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.service.protocol.AbstractWebProtocol;
import com.yongxin.weborder.utils.OrderHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WebLoginWebProtocol extends AbstractWebProtocol
{

    private Logger logger = LoggerFactory.getLogger(WebLoginWebProtocol.class);

    private String username;
    private String password;
    private String answer;

    public WebLoginWebProtocol(OrderHttpClient orderHttpClient, String username, String password, String answer)
    {
        super(orderHttpClient);
        this.username = username;
        this.password =  password;
        this.answer = answer;
    }

    @Override
    public Map<Integer, Object> service() throws ProtocolException
    {
        String url = "https://kyfw.12306.cn/passport/web/login";
        Map<Integer,Object> returnMap = new HashMap<>();
        addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        addHeader("Accept-Encoding","gzip, deflate, br");
        addHeader("Accept-Language","zh-CN,zh;q=0.9");
        addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        addHeader("Host","kyfw.12306.cn");
        addHeader("Origin","https://kyfw.12306.cn");

        addParams("appid","otn");
        addParams("username",this.username);
        addParams("password",this.password);
        addParams("answer",this.answer);


        Pair<Integer,String> result = execute(url,POST_BY_FORM);
        String json = result.right;
        logger.info("登录结果"+json);
        if (result.left == 200)
        {
            if (json.startsWith("{"))
            {
                JSONObject jsonObject = JSONObject.parseObject(json);
                Integer code = jsonObject.getInteger("result_code");
                String msg = jsonObject.getString("result_message");
                if (code == 1)
                {
                    throw new ProtocolException(ResultEnum.PASSWORD_ERROR);
                }
                else if (code == 0)
                {
                    String uamtk = jsonObject.getString("uamtk");
                    returnMap.put(ResultEnum.LOGIN_SUCCESS.getCode(),uamtk);
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
