package com.yongxin.weborder.common.exception;

import lombok.Data;


/**
 * 公共异常
 */
@Data
public class CommonException extends Exception
{

    private Integer code;

    public CommonException(String msg)
    {
        super(msg);
    }

    public CommonException(Integer code,String msg)
    {
        super(msg);
        this.code = code;
    }
}
