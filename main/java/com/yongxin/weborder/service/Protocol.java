package com.yongxin.weborder.service;

import com.yongxin.weborder.bean.*;
import com.yongxin.weborder.common.bean.Pair;
import com.yongxin.weborder.common.exception.ProtocolException;

import java.util.List;
import java.util.Map;

public interface Protocol
{
    /**
     * 初始化
     * @param proxy 代理
     * @return
     */
    void init(HttpProxy proxy) throws ProtocolException;

    /**
     * 获取loginconf的返回结果
     * @return
     */
    String loginConf();

    /**
     * 获取图片验证码
     * @return
     */
    String getCaptchaImage();

    /**
     * 获取验证码结果
     * @param image
     * @return
     */
    String getCaptchaCode(String image) throws ProtocolException;

    /**
     * 检验图形验证码
     * @param code
     * @return
     */
    void checkCaptchaImage(String code) throws ProtocolException;

    /**
     * 登录接口
     * @param username
     * @param password
     * @param code
     * @return
     */
    void login(String username,String password,String code) throws ProtocolException;

    /**
     * 获取newtk
     * @return
     */
    String authUamtk() throws ProtocolException;

    /**
     * 获取最终的token
     * @param tk
     * @return
     * @throws ProtocolException
     */
    String uamauthclient(String tk) throws ProtocolException;

    Pair<Integer,Object> getUserInfo() throws ProtocolException;


    /**
     * 获取cookie
     *   - RAIL_DEVICEID
     *   - RAIL_EXPIRATION
     * @return
     */
    Map<String,String> getCookie() throws ProtocolException;


    /**
     * 设置cookie
     * @param cookie
     */
    void setCookies(Map<String,String> cookie);

    /**
     * 设置cookie
     * @param key
     * @param value
     */
    void setCookie(String key,String value);


    /**
     * 余票查询接口
     *
     * @param leftTicketUrl
     * @param trainDate
     * @param fromStation
     * @param toStation
     * @param fromStationName
     * @param toStationName
     * @return
     */
    String queryLeftTickets(String leftTicketUrl, String trainDate, String fromStation, String toStation, String fromStationName,
                            String toStationName) throws ProtocolException;
    /**
     * 余票查询初始化
     * @return
     */
    String initLeftTickets() throws ProtocolException;

    /**
     * 解析余票str
     * @param ticketsStr
     * @return
     */
    List<TrainInfo> parseTicketsInfo(String ticketsStr);


    /**
     * 判断是否需要登录验证码
     * @param loginConf
     * @return
     */
    boolean isNeedPassCode(String loginConf);

    /**
     * 提交订单之前，检查用户
     */
    boolean checkUser() throws ProtocolException;

    /**
     * 提交订单信息
     * @param ticketSecure     余票加密串
     * @param trainDate        车次日期
     * @param fromStationName  出发站名称
     * @param toStationName    到达站名称
     */
    void submitOrder(String ticketSecure, String trainDate, String fromStationName, String toStationName) throws ProtocolException;

    /**
     *
     */
    LeftTicket initDc() throws ProtocolException;

    /**
     * 获取联系人信息
     * @param submitToken
     */
    List<NormalPassenger> getPassengerDTOs(String submitToken) throws ProtocolException;

    /**
     * 检查订单
     * @throws ProtocolException
     */
    void checkOrderInfo(String submitToken,String passengerTicketStr,String oldPassengerStr) throws ProtocolException;

    /**
     * 获取队列信息
     * @param initDc
     * @param trainDate
     * @param trainNo
     * @param seatType
     * @param fromStation
     * @param toStation
     * @throws ProtocolException
     */
    void getQueueCount(LeftTicket initDc, String trainDate, String trainNo, String seatType, String fromStation, String toStation) throws ProtocolException;

    /**
     * 组装PassengerTicketStr
     * @param orderPassengersList
     * @return
     */
    String getPassengerTicketStr(List<NormalPassenger> orderPassengersList);

    /**
     * 组装OldPassengerStr
     * @param orderPassengersList
     * @return
     */
    String getOldPassengerStr(List<NormalPassenger> orderPassengersList);

    /**
     * 提交订单
     * @param initDc
     * @param passengerTicketStr
     * @param oldPassengerStr
     */
    void confirmSingleForQueue(LeftTicket initDc, String passengerTicketStr, String oldPassengerStr) throws ProtocolException;

    /**
     * 查询等待结果
     * @param submitToken
     * @param times
     */
    String queryOrderWaitTime(String submitToken, int times) throws ProtocolException;

    /**
     * 提交获取最终结果
     * @param order12306
     * @param submitToken
     * @throws ProtocolException
     */
    void resultOrderForDcQueue(String order12306, String submitToken) throws ProtocolException;

    /**
     * 查询未完成订单
     * @return
     * @throws ProtocolException
     */
    Order12306 queryNoCompleteOrder() throws ProtocolException;


    /**
     * 获取车站信息
     */
    String getStation() throws ProtocolException;
}
