package com.yongxin.weborder.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yongxin.weborder.bean.*;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.service.protocol.common.*;
import com.yongxin.weborder.service.protocol.login.*;
import com.yongxin.weborder.service.protocol.order.*;
import com.yongxin.weborder.service.protocol.ticket.WebLeftTicketQueryWebProtocol;
import com.yongxin.weborder.service.protocol.ticket.WebLeftTicketsInitWebProtocol;
import com.yongxin.weborder.service.ParseTrainService;
import com.yongxin.weborder.service.Protocol;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.utils.OrderHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebProtocolImpl implements Protocol
{

    private Logger logger = LoggerFactory.getLogger(WebProtocolImpl.class);

    private OrderHttpClient client = null;

    @Autowired
    private ParseTrainService parseTrainService;

    @Value("${auto.captcha.code}")
    private String autoCaptchaUrl;

    /**
     * 初始化操作
     * @return
     */
    @Override
    public void init(HttpProxy proxy) throws ProtocolException
    {
        try
        {
            client = new OrderHttpClient();
            if (ObjectUtil.isNotNull(proxy))
            {
                client.setProxy(proxy);
            }
        }
        catch (Exception e)
        {
            logger.error("初始化client失败",e);
            throw new ProtocolException(ResultEnum.INIT_CLIENT_ERROR);
        }
    }



    @Override
    public String loginConf()
    {
        try
        {
            WebLoginConfWebProtocol webLoginConf = new WebLoginConfWebProtocol(this.client);
            Map<Integer,Object> result = webLoginConf.service();
            String data = ObjectUtil.getString(result.get(ResultEnum.LOGIN_CONF.getCode()));
            return data;
        }
        catch (ProtocolException e)
        {
            logger.error("获取loginConf失败");
        }
        return null;
    }

    @Override
    public String getCaptchaImage()
    {
        try
        {
            WebGetCaptchaImageWebProtocol getCaptchaImage = new WebGetCaptchaImageWebProtocol(client);
            Map<Integer,Object> map = getCaptchaImage.service();
            String image = ObjectUtil.getString(map.get(ResultEnum.CAPTCHA_IMAGE.getCode()));
            return image;
        }
        catch (ProtocolException e)
        {
            logger.error("获取图片验证码失败",e);
        }
        return null;
    }

    @Override
    public String getCaptchaCode(String image) throws ProtocolException
    {
        Map<Integer,String> zuobiao = new HashMap<>();
        zuobiao.put(1,"46,39");
        zuobiao.put(2,"123,45");
        zuobiao.put(3,"194,54");
        zuobiao.put(4,"264,48");
        zuobiao.put(5,"35,121");
        zuobiao.put(6,"118,122");
        zuobiao.put(7,"198,121");
        zuobiao.put(8,"268,118");
        StringBuffer strBuffer = new StringBuffer();
        int times = 0;
        while (times < 3)
        {
            try
            {
                OrderHttpClient client = new OrderHttpClient();
                client.addHeader("Content-Type","application/x-www-form-urlencoded");
                client.addParam("imageFile",image);
                Pair<Integer,String> pairResult = client.doPostByForm(this.autoCaptchaUrl);
                JSONObject json = JSONObject.parseObject(pairResult.right);
                JSONArray array = json.getJSONArray("data");
                for (int i = 0 ; i < array.size() ; i++)
                {
                    int dd = array.getInteger(i);
                    String zuo = zuobiao.get(dd);
                    strBuffer.append(zuo).append(",");
                }
                if (ObjectUtil.isNotNull(strBuffer.toString()))
                {
                    break;
                }
            }
            catch (Exception e)
            {
                logger.error("获取验证码结果出错重新获取");
            }
            times++;
        }
        if(ObjectUtil.isNull(strBuffer.toString()))
        {
            throw new ProtocolException(ResultEnum.PASSCODE_ERROR);
        }
        return strBuffer.toString();
    }

    @Override
    public void login(String username, String password, String code) throws ProtocolException
    {
        WebLoginWebProtocol login = new WebLoginWebProtocol(client,username,password,code);
        login.service();
    }


    public String authUamtk() throws ProtocolException
    {
        WebAuthUamtkWebProtocol uamtk = new WebAuthUamtkWebProtocol(this.client);
        Map<Integer,Object> result = uamtk.service();
        String newtk = ObjectUtil.getString(result.get(ResultEnum.AUTH_UAMTK.getCode()));
        return newtk;
    }

    @Override
    public String uamauthclient(String tk) throws ProtocolException
    {
        WebUamauthclientWebProtocol uamauthclient = new WebUamauthclientWebProtocol(this.client,tk);
        Map<Integer,Object> map = uamauthclient.service();
        String token = ObjectUtil.getString(map.get(ResultEnum.UAMTK_CLIENT.getCode()));
        return token;
    }

    @Override
    public void checkCaptchaImage(String code) throws ProtocolException
    {
        WebCaptchaCheckWebProtocol check = new WebCaptchaCheckWebProtocol(client,code);
        check.service();
    }

    @Override
    public Pair<Integer,Object> getUserInfo() throws ProtocolException
    {
        Pair<Integer,Object> res = new Pair<>();

        WebInitMy12306ApiWebProtocol initMy12306Api = new WebInitMy12306ApiWebProtocol(this.client);
        Map<Integer,Object> resultMap = initMy12306Api.service();
        String userinfo = ObjectUtil.getString(resultMap.get(ResultEnum.USER_INFO.getCode()));
        return res;
    }



    public String queryLeftTickets(String leftTicketUrl, String trainDate, String fromStation, String toStation, String fromStationName,
                                   String toStationName) throws ProtocolException
    {
        WebLeftTicketQueryWebProtocol leftTicketQuery = new WebLeftTicketQueryWebProtocol(this.client,leftTicketUrl,trainDate,fromStation,toStation,fromStationName,toStationName);
        Map<Integer,Object> resultMap = leftTicketQuery.service();
        String tickets = ObjectUtil.getString(resultMap.get(ResultEnum.LEFTTICKET_QUREY.getCode()));
        return tickets;
    }

    @Override
    public String initLeftTickets() throws ProtocolException
    {
        WebLeftTicketsInitWebProtocol webLeftTicketsInit = new WebLeftTicketsInitWebProtocol(this.client);
        Map<Integer, Object> returnMap = webLeftTicketsInit.service();
        String CLeftTicketUrl = ObjectUtil.getString(returnMap.get(ResultEnum.LEFTTICKET_INIT.getCode()));
        if (ObjectUtil.isNull(CLeftTicketUrl))
        {
            throw new ProtocolException(ResultEnum.NO_QUERY_URL);
        }
        return CLeftTicketUrl;
    }

    @Override
    public List<TrainInfo> parseTicketsInfo(String ticketsStr)
    {
        List<TrainInfo> trainInfoList = new ArrayList<>();
        if (ObjectUtil.isNotNull(ticketsStr))
        {
            JSONObject obj = JSONObject.parseObject(ticketsStr);
            JSONArray result = obj.getJSONArray("result");
            for (int i = 0; i < result.size() ; i++)
            {
                TrainInfo trainInfo = this.parseTrainService.parseStringToTrainInfo(result.getString(i));
                trainInfoList.add(trainInfo);
            }
        }
        return trainInfoList;
    }

    @Override
    public boolean isNeedPassCode(String loginConf)
    {
        JSONObject obj = JSONObject.parseObject(loginConf);
        return obj.getString("is_login_passCode").equals("Y");
    }

    @Override
    public boolean checkUser() throws ProtocolException
    {
        WebCheckUserWebProtocol checkUserWebProtocol = new WebCheckUserWebProtocol(this.client);
        Map<Integer,Object> map = checkUserWebProtocol.service();
        return ObjectUtil.getBoolean(map.get(ResultEnum.CHECK_URSER.getCode()));
    }

    @Override
    public void submitOrder(String ticketSecure, String trainDate, String fromStationName, String toStationName) throws ProtocolException
    {
        WebSubmitOrderRequestWebProtocol submit = new WebSubmitOrderRequestWebProtocol(this.client,
                ticketSecure,trainDate,fromStationName,toStationName);
        submit.service();

    }

    @Override
    public LeftTicket initDc() throws ProtocolException
    {
        WebInitDcWebProtocol initDc = new WebInitDcWebProtocol(this.client);
        Map<Integer,Object> map = initDc.service();
        String initDCStr = ObjectUtil.getString(map.get(ResultEnum.INITDC.getCode()));
        return LeftTicket.parseStr(initDCStr);
    }

    @Override
    public List<NormalPassenger> getPassengerDTOs(String submitToken) throws ProtocolException
    {
        WebGetPassengerDTOsWebProtocol getPassenger = new WebGetPassengerDTOsWebProtocol(this.client,submitToken);
        Map<Integer,Object> passengers = getPassenger.service();
        String obj = ObjectUtil.getString(passengers.get(ResultEnum.PASSENGER_DTO.getCode()));
        JSONArray passengerarr = JSONObject.parseObject(obj).getJSONArray("normal_passengers");
        return NormalPassenger.parsePassengers(passengerarr);
    }

    @Override
    public void checkOrderInfo(String submitToken,String passengerTicketStr,String oldPassengerStr) throws ProtocolException
    {
        WebCheckOrderInfoWebProtocol checkOrder = new WebCheckOrderInfoWebProtocol(this.client,passengerTicketStr,
                oldPassengerStr,submitToken);
        checkOrder.service();
        logger.info("检查订单成功");
    }

    @Override
    public void getQueueCount(LeftTicket initDc, String trainDate, String stationTrainCode, String seatType, String fromStation, String toStation) throws ProtocolException
    {
        WebGetQueueCountWebProtocol getQueue = new WebGetQueueCountWebProtocol(this.client,trainDate,initDc.getTrainNo(),
                stationTrainCode,seatType,fromStation,toStation,initDc.getLeftTicketStr(),initDc.getPurposeCodes()
                ,initDc.getTrainLocation(),initDc.getSubmitToken());
        getQueue.service();
        logger.info("获取排队队列");
    }

    @Override
    public String getPassengerTicketStr(List<NormalPassenger> orderPassengersList)
    {
        StringBuffer passengerBuf  = new StringBuffer();

        orderPassengersList.forEach( passenger -> {
            passengerBuf.append(passenger.getPassengerTicketStr()).append("_");
        });

        String passengerTicketStr = StringUtils.removeEnd(passengerBuf.toString(),"_");
        return passengerTicketStr;
    }

    @Override
    public String getOldPassengerStr(List<NormalPassenger> orderPassengersList)
    {
        StringBuffer oldPassengerStr  = new StringBuffer();

        orderPassengersList.forEach( passenger -> {
            oldPassengerStr.append(passenger.getOldPassengerStr()).append("_");
        });

        return oldPassengerStr.toString();
    }

    @Override
    public void confirmSingleForQueue(LeftTicket initDc, String passengerTicketStr, String oldPassengerStr) throws ProtocolException
    {
        WebConfirmSingleForQueueWebProtocol confirmQueue = new WebConfirmSingleForQueueWebProtocol(this.client,
                passengerTicketStr,oldPassengerStr,initDc.getPurposeCodes(),initDc.getKeyCheckIsChange(),
                initDc.getLeftTicketStr(),initDc.getTrainLocation(),"",initDc.getSubmitToken());
        confirmQueue.service();
        logger.info("提交排队");
    }

    @Override
    public String queryOrderWaitTime(String submitToken, int times) throws ProtocolException
    {
        // 避免死循环
        if (times > 50)
        {
            return null;
        }
        WebQueryOrderWaitTimeWebProtocol queryWaitTime = new WebQueryOrderWaitTimeWebProtocol(this.client,submitToken);
        Map<Integer, Object> resultMap = queryWaitTime.service();
        String data = ObjectUtil.getString(resultMap.get(ResultEnum.GET_WAIT_QUEUE.getCode()));
        JSONObject obj = JSONObject.parseObject(data);
        int waitTime = obj.getInteger("waitTime");
        if ( waitTime == -1)
        {
            logger.info("排队成功：{}",obj.getString("orderId"));
            return obj.getString("orderId");
        }
        else if (waitTime == -100)
        {
            ObjectUtil.sleep(1);
            queryOrderWaitTime(submitToken, times+1);
        }
        else if (waitTime == -2)
        {
            String msg = obj.getString("msg");
            throw new ProtocolException(ResultEnum.SUBMITORDER_ERROR.getCode(),msg);
        }
        else
        {
            logger.info("正在排队，请稍等 {}s ",waitTime);
            ObjectUtil.sleep(waitTime);
            queryOrderWaitTime(submitToken, times+1);
        }
        return null;
    }

    @Override
    public void resultOrderForDcQueue(String order12306, String submitToken) throws ProtocolException
    {
        WebResultOrderForDcQueueWebProtocol resultForQueue = new WebResultOrderForDcQueueWebProtocol(this.client,submitToken,order12306);
        resultForQueue.service();
    }

    @Override
    public Order12306 queryNoCompleteOrder() throws ProtocolException
    {
        WebQueryMyOrderNoCompleteWebProtocol noComplete = new WebQueryMyOrderNoCompleteWebProtocol(this.client);
        Map<Integer,Object> returnMap = noComplete.service();
        String data = ObjectUtil.getString(returnMap.get(ResultEnum.NO_COMPLETE_ORDER.getCode()));
        if(ObjectUtil.isNotNull(data))
        {
            JSONObject obj =JSONObject.parseObject(data).getJSONArray("orderDBList").getJSONObject(0);
            Order12306 order12306 = JSONObject.toJavaObject(obj,Order12306.class);
            return order12306;
        }
        return null;
    }

    @Override
    public String getStation() throws ProtocolException
    {
        WebGetStation webGetStation = new WebGetStation(this.client);
        Map<Integer, Object> map = webGetStation.service();
        String stationStr = ObjectUtil.getString(map.get(ResultEnum.GET_STATION.getCode()));
        return stationStr;
    }


    @Override
    public Map<String,String> getCookie() throws ProtocolException
    {
        Map<String,String> cookie = new HashMap<>();
        WebLogDeviceWebProtocol logDevice = new WebLogDeviceWebProtocol(this.client);
        Map<Integer, Object> map = logDevice.service();
        JSONObject object  = JSONObject.parseObject(ObjectUtil.getString(map.get(ResultEnum.GET_DEVICE.getCode())));
        String dev = object.getString("dfp");
        String time = object.getString("exp");
        cookie.put("RAIL_DEVICEID",dev);
        cookie.put("RAIL_EXPIRATION",time);
        return cookie;
    }

    @Override
    public void setCookies(Map<String, String> cookies)
    {
        for (Map.Entry<String,String> cookie : cookies.entrySet())
        {
            client.addCookie(cookie.getKey(),cookie.getValue());
        }
    }

    @Override
    public void setCookie(String key, String value)
    {
        client.addCookie(key,value);
    }


}
