package com.yongxin.weborder.service.task;

import com.alibaba.fastjson.JSON;
import com.yongxin.weborder.bean.HttpProxy;
import com.yongxin.weborder.bean.TrainInfo;
import com.yongxin.weborder.bean.BuyTicket;
import com.yongxin.weborder.cache.ProxyCache;
import com.yongxin.weborder.common.exception.ProtocolException;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.MailNoticeService;
import com.yongxin.weborder.service.Protocol;
import com.yongxin.weborder.service.impl.MailNoticeServiceImpl;
import com.yongxin.weborder.utils.TrainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


/**
 * 余票查询接口
 */
@Service
public class TicketsQueryTask
{

    private Logger logger = LoggerFactory.getLogger(TicketsQueryTask.class);

    @Autowired
    private BuyTicketsTask buyTicketsTask;

    @Autowired
    private Protocol protocol;

    @Autowired
    private MailNoticeService mailNoticeService;


    @Async("leftTicketsThreadPool")
    public void leftTickets(BuyTicket buyTicket)
    {
        long count = 0;
        HttpProxy proxy = null;
        while( System.currentTimeMillis() < buyTicket.getEndTime())
        {
            try
            {
                if(!TrainUtils.isTime12306())
                {
                    logger.info("当前不再12306买票时间段");
                    ObjectUtil.sleep(60);
                    continue;
                }

                if(ObjectUtil.isNull(proxy))
                {
                    proxy = ProxyCache.getUsableProxy();
                }

                if(ObjectUtil.isNull(proxy))
                {
                    Random random = new Random();
                    int time = 1000 + random.nextInt(3000);
                    logger.info("休眠一会{}ms",time);
                    ObjectUtil.sleepMillis(time);
                }
                logger.info("车票查询线程开启，查询车次:{},坐席{}, 当前代理 {}" , buyTicket.getTrainList(),buyTicket.getSeatList(),proxy);
                protocol.init(proxy);
                String leftTicketUrl = protocol.initLeftTickets();
                String ticketStr = protocol.queryLeftTickets(leftTicketUrl,buyTicket.getTrainDate(), buyTicket.getFromStation(),
                        buyTicket.getToStation(), buyTicket.getFromStationName(), buyTicket.getToStationName());
                List<TrainInfo> trainInfoList = protocol.parseTicketsInfo(ticketStr);
                count ++;
                for (TrainInfo trainInfo : trainInfoList)
                {
                    for (String trainNo : buyTicket.getTrainList())
                    {
                        if (trainInfo.getTrainCode().equals(trainNo))
                        {
                            logger.info("第{}次有效查询,查找到相关车次信息:{}",count, JSON.toJSON(trainInfo).toString());
                            if (trainInfo.getIsBuyTime().equals("IS_TIME_NOT_BUY"))
                            {
                                logger.info("{} - {}",trainInfo.getTrainCode() , trainInfo.getButton());
                                continue;
                            }
                            buyTicket.setRealFromStation(trainInfo.getFcode());
                            buyTicket.setRealToStation(trainInfo.getTcode());
                            buyTicket.setTrainNo(trainNo);
                            buyTicket.setTrainInfo(trainInfo);
                            boolean letfTicketFlag = false;
                            for (String seat : buyTicket.getSeatList())
                            {
                                letfTicketFlag = trainInfo.hasTickets(seat);
                                if (letfTicketFlag)
                                {
                                    buyTicket.setSeatName(seat);
                                    // 去买票
                                    this.buyTicketsTask.buyTickets(buyTicket);
                                    logger.info("发现余票去买票：{} - {}",buyTicket.getTrainNo(),buyTicket.getSeatName());
                                    mailNoticeService.sendLeftTicketsNotice(buyTicket);
                                    return;
                                }
                            }
                            if (!letfTicketFlag)
                            {
                                logger.info("{}无余票，{}:" ,buyTicket.getTrainList(),buyTicket.getSeatList() );
                            }
                        }
                    }
                }
            }
            catch (ProtocolException e)
            {
                proxy = null;
                logger.info("余票查询出错:"+e.getMessage());
                ProxyCache.removeUsable(proxy);
            }
            catch (Exception e)
            {
                proxy = null;
                logger.error("查询余票出错",e);
                ProxyCache.removeUsable(proxy);
            }
        }
    }



}
