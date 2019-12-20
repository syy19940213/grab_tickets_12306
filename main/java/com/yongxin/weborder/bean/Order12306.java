package com.yongxin.weborder.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class Order12306
{
    private String sequence_no;
    private String order_date;
    private Integer ticket_totalnum;
    private Integer ticket_price_all;
    private String ticket_total_price_page;
    private String cancel_flag;
    private String start_train_date_page;
    private String start_time_page;
    private String arrive_time_page;
    private String train_code_page;
    private List<Tickets> tickets;


    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("订单号:").append(this.sequence_no).append(" ")
                .append("车次:").append(this.train_code_page).append(" ")
                .append("发车日期:").append(this.start_time_page).append(" ")
                .append("乘车人:").append(tickets.toString());
        return buffer.toString();
    }
}