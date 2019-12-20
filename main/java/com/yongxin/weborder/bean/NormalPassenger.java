package com.yongxin.weborder.bean;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用联系人
 */
@Data
public class NormalPassenger
{
    private String passengerName;
    private String sexCode;
    private String sexName;
    private String passengerIdTypeCode;
    private String passengerIdTypeName;
    private String passengerIdNo;
    private String passengerType;
    private String passengerFlag;
    private String passengerTypeName;
    private String allEncStr;


    /**
     * 座位类型
     */
    private String seatType;

    /**
     * 1 成人票
     */
    private Integer ticketType;


    /**
     * 获取passengerTicketStr
     * @return
     */
    public String getPassengerTicketStr()
    {
        StringBuffer str = new StringBuffer();
        str.append(this.seatType)
                .append(",0") // 默认0
                .append(",").append(ticketType)
                .append(",").append(passengerName)
                .append(",").append(passengerIdTypeCode)
                .append(",").append(passengerIdNo)
                .append(",").append("")   //手机号
                .append(",").append("N")
                .append(",").append(allEncStr);
        return str.toString();
    }

    /**
     * 获取oldPassengerStr
     * @return
     */
    public String getOldPassengerStr()
    {
        StringBuffer str = new StringBuffer();
        str.append(passengerName)
                .append(",").append(passengerIdTypeCode)
                .append(",").append(passengerIdNo)
                .append(",").append(ticketType);
        return str.toString();
    }


    public static List<NormalPassenger> parsePassengers(JSONArray array)
    {
        List<NormalPassenger> list = new ArrayList<>();
        for (int i = 0; i < array.size() ;i ++)
        {
            JSONObject obj = array.getJSONObject(i);
            NormalPassenger passenger = new NormalPassenger();
            passenger.setPassengerName(obj.getString("passenger_name"));
            passenger.setSexCode(obj.getString("sex_code"));
            passenger.setSexName(obj.getString("sex_name"));
            passenger.setPassengerIdTypeCode(obj.getString("passenger_id_type_code"));
            passenger.setPassengerIdTypeName(obj.getString("passenger_id_type_name"));
            passenger.setPassengerIdNo(obj.getString("passenger_id_no"));
            passenger.setPassengerType(obj.getString("passenger_type"));
            passenger.setPassengerFlag(obj.getString("passenger_flag"));
            passenger.setPassengerTypeName(obj.getString("passenger_type_name"));
            passenger.setAllEncStr(obj.getString("allEncStr"));
            list.add(passenger);
        }
        return list;
    }

    @Override
    public String toString()
    {
        return JSON.toJSONString(this);
    }
}
