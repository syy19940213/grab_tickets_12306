package com.yongxin.weborder.bean;

import lombok.Data;

/**
 * 乘车人信息
 */
@Data
public class Passenger
{
    private String name;
    // 1 成人票
    private Integer ticketType;
}
