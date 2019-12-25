package com.yongxin.weborder.service.task;

import com.yongxin.weborder.bean.*;
import com.yongxin.weborder.cache.ProxyCache;
import com.yongxin.weborder.common.exception.CommonException;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.MailNoticeService;
import com.yongxin.weborder.service.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
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


    @Autowired
    private MailNoticeService mailNoticeService;

    private BuyTicket buyTicket;



    @Async("buyTicketsThreadPool")
    @Retryable(value = RetryException.class, maxAttempts = 3, backoff = @Backoff(delay = 5000, multiplier = 1.5))
    public void buyTickets(BuyTicket buyTicket)
    {
        try
        {
            this.buyTicket = buyTicket;
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
                throw new ProtocolException(ResultEnum.NO_PASSENGERS);
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
            this.mailNoticeService.sendSuccess(buyTicket);

        }
        catch (ProtocolException e)
        {
            if (e.getMessage().contains("您还有未处理的订单"))
            {
                logger.info("您有未处理订单，请先处理");
                return;
            }
            throw new RetryException(e.getMessage());
        }
        catch (Exception e)
        {
            logger.error("购买出错",e);
        }
    }


    @Recover
    public void recover(RetryException e) {
        logger.info("购票出错:{}",e.getMessage());
        this.ticketsQueryTask.leftTickets(this.buyTicket);
    }




}
