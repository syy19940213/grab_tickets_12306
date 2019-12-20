package com.yongxin.weborder.service.task;

import com.yongxin.weborder.bean.*;
import com.yongxin.weborder.cache.ProxyCache;
import com.yongxin.weborder.common.exception.CommonException;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 购买线程
 */
@Service
public class BuyTicketsTask
{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Protocol protocol;

    @Autowired
    @Lazy
    private TicketsQueryTask ticketsQueryTask;



    @Async("buyTicketsThreadPool")
    public void buyTickets(BuyTicket buyTicket)
    {
        try
        {
//            HttpProxy proxy = new HttpProxy();
//            proxy.setIp("127.0.0.1");
//            proxy.setPort(7888);

            // 买票用本机ip
            HttpProxy proxy = ProxyCache.getUsableProxy();

            protocol.init(proxy);

            Map<String,String> cookie = protocol.getCookie();
            protocol.setCookies(cookie);

            String loginConf = protocol.loginConf();
            logger.info("获取loginConf返回结果" +  loginConf);
            boolean passCodeFlag = protocol.isNeedPassCode(loginConf);
            String answer = null;
            if (passCodeFlag)
            {
                String image = protocol.getCaptchaImage();
                logger.info("获取验证码成功");
                if (ObjectUtil.isNotNull(image))
                {
                    answer = protocol.getCaptchaCode(image);
                    logger.info("获取到验证码结果");
                    protocol.checkCaptchaImage(answer);
                }
            }
            protocol.login(buyTicket.getAccount12306(),buyTicket.getPassword12306(),answer);
            String newTk  = protocol.authUamtk();
            String token = protocol.uamauthclient(newTk);
            logger.info("获取tk成功" + token);


            protocol.checkUser();
            protocol.submitOrder(buyTicket.getTrainInfo().getTicketSecure(),
                    buyTicket.getTrainDate(),
                    buyTicket.getFromStationName(),
                    buyTicket.getToStationName());

            LeftTicket initDc = protocol.initDc();
            logger.info("获取submittoekn成功:"+initDc.toString());
            List<NormalPassenger> normalPassengers = protocol.getPassengerDTOs(initDc.getSubmitToken());

            // 获取到需要下单的乘车人
            List<NormalPassenger> orderPassengersList = new ArrayList<>();
            normalPassengers.forEach( normalPassenger -> {
                buyTicket.getPassengers().forEach( passenger -> {
                    if (normalPassenger.getPassengerName().equals(passenger.getName()))
                    {
                        String seatType = SeatEnum.getSeatCodeByName(buyTicket.getSeatName());
                        if(ObjectUtil.isNull(seatType))
                        {
                            if (buyTicket.getTrainNo().startsWith("G") ||
                                    buyTicket.getTrainNo().startsWith("C") ||
                                    buyTicket.getTrainNo().startsWith("D"))
                            {
                                seatType = "O";
                            }
                            else
                            {
                                seatType = "1";
                            }
                        }
                        // 设置购买坐席
                        normalPassenger.setSeatType(seatType);
                        normalPassenger.setTicketType(passenger.getTicketType());
                        orderPassengersList.add(normalPassenger);
                    }
                });
            });

            if (ObjectUtil.isEmpty(orderPassengersList))
            {
                throw new CommonException("没有查找到联系人");
            }

            String passengerTicketStr = protocol.getPassengerTicketStr(orderPassengersList);
            String oldPassengerStr = protocol.getOldPassengerStr(orderPassengersList);

            protocol.checkOrderInfo(initDc.getSubmitToken(),passengerTicketStr,oldPassengerStr);

            protocol.getQueueCount(initDc,buyTicket.getTrainDate(),buyTicket.getTrainNo(),orderPassengersList.get(0).getSeatType(),
                    buyTicket.getRealFromStation(),buyTicket.getRealToStation());

            protocol.confirmSingleForQueue(initDc,passengerTicketStr,oldPassengerStr);

            String sequence_no = protocol.queryOrderWaitTime(initDc.getSubmitToken(),0);

            // 有时候查不到订单
            ObjectUtil.sleep(3);
            Order12306 order12306 = protocol.queryNoCompleteOrder();
            if (ObjectUtil.isNull(order12306) && ObjectUtil.isNull(sequence_no))
            {
                logger.info("下单完成:"+ sequence_no);
            }
            else
            {
                logger.info("下单完成：" + order12306.toString());
            }

        }
        catch (ProtocolException e)
        {
            if (e.getMessage().contains("您还有未处理的订单"))
            {
                logger.info("您有未处理订单，请先处理");
                return;
            }
            this.ticketsQueryTask.leftTickets(buyTicket);
            logger.error("购票出错",e);
        }
        catch (Exception e)
        {
            this.ticketsQueryTask.leftTickets(buyTicket);
            logger.error("购买出错",e);
        }
    }



}
