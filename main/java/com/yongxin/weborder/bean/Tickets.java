package com.yongxin.weborder.bean;

import lombok.Data;

@Data
public class Tickets
{
    private String ticket_no;
    private String sequence_no;
    private String batch_no;
    private String coach_no;
    private String coach_name;
    private String seat_no;
    private String seat_name;
    private String seat_type_code;
    private String seat_type_name;
    private String ticket_type_code;
    private String ticket_type_name;
    private Integer ticket_price;
    private String str_ticket_price_page;
    private passengerDTO passengerDTO;

    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("车厢:").append(coach_name).append(" ")
                .append("座位:").append(seat_name).append(" ")
                .append("坐席:").append(seat_type_name).append(" ")
                .append("车票:").append(ticket_type_name).append(" ")
                .append("乘车人:").append(passengerDTO.getPassenger_name());
        return buffer.toString();
    }
}

