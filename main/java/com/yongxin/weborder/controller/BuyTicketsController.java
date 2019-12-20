package com.yongxin.weborder.controller;

import com.yongxin.weborder.bean.BuyTicket;
import com.yongxin.weborder.cache.StationCache;
import com.yongxin.weborder.common.utils.ObjectUtil;
import com.yongxin.weborder.service.task.BuyTicketsTask;
import com.yongxin.weborder.service.task.TicketsQueryTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api")
public class BuyTicketsController
{
    @Autowired
    private TicketsQueryTask ticketsQueryTask;


    /**
     * 买票
     * {
     *     "trainDate":"2019-12-20",
     *     "fromStationName":"广州",
     *     "toStationName":"宜昌东",
     *     "trainNo":"G1146",
     *     "account12306":"XXXX",
     *     "password12306":"XXXX",
     *     "seatName":"二等座",
     *     "passengers":[
     *         {
     *             "name":"XXX",
     *             "ticketType":"1"
     *         }
     *     ]
     * }
     * @param buyTicket
     * @return
     */
    @RequestMapping(value = "/buyTickets",method = RequestMethod.POST)
    @ResponseBody
    public String buyTickets(@RequestBody BuyTicket buyTicket)
    {
        String fromCode = StationCache.getCodeByName(buyTicket.getFromStationName());
        String toCode = StationCache.getCodeByName(buyTicket.getToStationName());
        if (ObjectUtil.hasNull(fromCode,toCode))
        {
            return "站点信息有误";
        }
        buyTicket.setEndTime(ObjectUtil.parseTimestamp(buyTicket.getTrainDate(),"yyyy-MM-dd").getTime());
        buyTicket.setFromStation(fromCode);
        buyTicket.setToStation(toCode);
        this.ticketsQueryTask.leftTickets(buyTicket);
        return "ok";
    }


    /**
     * 预售票
     * @param buyTicket
     * @return
     */
    @RequestMapping(value = "/advanceBooking",method = RequestMethod.POST)
    @ResponseBody
    public String advanceBooking(@RequestBody BuyTicket buyTicket)
    {
        return "ok";
    }


}
