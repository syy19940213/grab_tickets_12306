package com.yongxin.weborder.service.impl;

import com.yongxin.weborder.bean.BuyTicket;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.MailNoticeService;
import com.yongxin.weborder.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailNoticeServiceImpl implements MailNoticeService
{

    private Logger logger = LoggerFactory.getLogger(MailNoticeServiceImpl.class);

    @Autowired
    private MailService mailService;



    @Override
    public void sendLeftTicketsNotice(BuyTicket buyTicket)
    {
        try
        {
            String to  = buyTicket.getEmail();
            if (ObjectUtil.isNull(to))
            {
                return;
            }
            StringBuffer subject = new StringBuffer();
            subject.append(buyTicket.getTrainNo()).append("-").append(buyTicket.getTrainDate()).append("发现余票");
            StringBuffer content = new StringBuffer();
            content.append("车次：").append(buyTicket.getTrainNo()).append("\n")
                    .append("乘车日期：").append(buyTicket.getTrainDate()).append("\n")
                    .append("坐席:").append(buyTicket.getSeatName()).append("\n");
            mailService.sendSimpleMail(to,subject.toString(),content.toString());
        }
        catch (Exception e)
        {
            logger.error("发送余票提醒失败",e);
        }
    }

    @Override
    public void sendSuccess(BuyTicket buyTicket)
    {
        try
        {
            String to  = buyTicket.getEmail();
            if (ObjectUtil.isNull(to))
            {
                return;
            }
            StringBuffer subject = new StringBuffer();
            subject.append(buyTicket.getTrainNo()).append("-").append(buyTicket.getTrainDate()).append("购票成功");
            StringBuffer content = new StringBuffer();
            content.append("车次：").append(buyTicket.getTrainNo()).append("\n")
                    .append("乘车日期：").append(buyTicket.getTrainDate()).append("\n")
                    .append("坐席:").append(buyTicket.getSeatName()).append("\n")
                    .append("账号:").append(buyTicket.getAccount12306()).append("\n")
                    .append("请尽快去12306支付");
            mailService.sendSimpleMail(to,subject.toString(),content.toString());
        }
        catch (Exception e)
        {
            logger.error("购票成功发送失败",e);
        }
    }

}
