package com.yongxin.weborder.bean;

import com.alibaba.fastjson.JSON;
import com.yongxin.weborder.common.utils.ObjectUtil;
import lombok.Data;

@Data
public class TrainInfo
{
    private String ticketSecure;
    private String trainCode;
    private String fcode;
    private String tcode;
    private String startTime;
    private String arriveTime;
    private String trainDate;
    private String useTime; //历时

    private String button; // 预定按钮
    private String isBuyTime; // 是否是购买时间

    private String gjrwNum; //高级软卧
    private String swzNum;  //商务座
    private String ydzNum;  //一等座
    private String edzNum;  //二等座
    private String rwNum;   //软卧  一等卧
    private String rzNum;   //软座
    private String wzNum;   //无座
    private String ywNum;   //硬卧
    private String yzNum;   //硬座

    private String seatTypes;   //座位类型
    private String ypInfo; // 余票信息

    private String hbStatus;//是否支持候补  1支持  0不支持


    @Override
    public String toString()
    {
        return JSON.toJSONString(this);
    }

    /**
     * 判断是否有余票
     * @param seatName
     * @return
     */
    public boolean hasTickets(String seatName)
    {
        if (seatName.equals("高级软卧") && ypNum(gjrwNum))
        {
            return true;
        }
        else if (seatName.equals("商务座") && ypNum(swzNum))
        {
            return true;
        }
        else if (seatName.equals("一等座") && ypNum(ydzNum))
        {
            return true;
        }
        else if (seatName.equals("二等座") && ypNum(edzNum))
        {
            return true;
        }
        else if ( (seatName.equals("软卧") || seatName.equals("一等卧") )&& ypNum(rwNum))
        {
            return true;
        }
        else if (seatName.equals("软座") && ypNum(rzNum))
        {
            return true;
        }
        else if (seatName.equals("无座") && ypNum(wzNum))
        {
            return true;
        }
        else if (seatName.equals("硬卧") && ypNum(ywNum))
        {
            return true;
        }
        else if (seatName.equals("硬座") && ypNum(yzNum))
        {
            return true;
        }

        return false;
    }


    /**
     * 判断是否有余票
     * @param num
     * @return
     */
    private boolean ypNum(String num)
    {
        if (num.equals("无") || num.equals("*"))
        {
            return false;
        }
        else if (num.equals("有") || ObjectUtil.getInt(num) > 0)
        {
            return true;
        }
        return false;
    }
}
