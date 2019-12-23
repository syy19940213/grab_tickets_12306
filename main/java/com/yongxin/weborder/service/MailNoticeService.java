package com.yongxin.weborder.service;

import com.yongxin.weborder.bean.BuyTicket;

public interface MailNoticeService
{

    /**
     * 发送余票提醒
     * @param buyTicket
     */
    void sendLeftTicketsNotice(BuyTicket buyTicket);

    /**
     * 购票成功
     * @param buyTicket
     */
    void sendSuccess(BuyTicket buyTicket);

}
