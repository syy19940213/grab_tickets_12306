package com.yongxin.weborder.bean;


import lombok.Data;

@Data
public class HttpProxy
{
    private String ip;
    private Integer port;

    private String username;
    private String password;

    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append(ip).append(":").append(port);
        return buf.toString();
    }
}
