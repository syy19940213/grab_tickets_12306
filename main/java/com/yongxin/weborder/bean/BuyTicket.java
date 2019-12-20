package com.yongxin.weborder.bean;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 购票信息
 */
@Data
public class BuyTicket
{
    // 出发站code
    private String fromStation;

    // 出发站名称
    private String fromStationName;

    // 到达code
    private String toStation;

    // 到达站名称
    private String toStationName;


    // 真实出发站code
    private String realFromStation;
    // 真实到达站code
    private String realToStation;

    // 出发日期
    private String trainDate;

    // 车次
    private String trainNo;

    // 结束时间
    private long endTime;

    // 查询到车票信息
    private TrainInfo trainInfo;

    // 座位类型
    private String seatName;

    // 多车次
    private LinkedList<String> trainList;

    // 多坐席
    private LinkedList<String> seatList;

    // 乘车人信息
    private List<Passenger> passengers;

    // 12306账号
    private String account12306;
    private String password12306;
}
