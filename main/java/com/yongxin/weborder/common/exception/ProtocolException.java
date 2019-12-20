package com.yongxin.weborder.common.exception;

import com.yongxin.weborder.bean.ResultEnum;
import lombok.Data;

/**
 * 协议错误
 */
@Data
public class ProtocolException extends CommonException
{

    private static final long serialVersionUID = -4672849643026505242L;


    private Integer code;

    public ProtocolException(Integer code,String msg)
    {
        super(msg);
        this.code = code;
    }

    public ProtocolException(ResultEnum resultEnum)
    {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

}
