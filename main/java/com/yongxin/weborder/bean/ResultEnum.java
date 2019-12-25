package com.yongxin.weborder.bean;

public enum ResultEnum
{
    /**
     * 成功的
     */
    CAPTCHA_IMAGE(1000,"获取验证码成功"),
    CAPTCHA_SUCCESS(1001,"验证码验证成功"),
    LOGIN_CONF(1002,"登录conf获取成功"),
    LOGIN_SUCCESS(1003,"登录成功"),
    AUTH_UAMTK(1004,"auth uamtk成功"),
    UAMTK_CLIENT(1005,"uamtk client 初始化成功"),
    USER_INFO(1006,"获取用户信息成功"),
    CHECK_URSER(1007,"检查用户成功"),
    SUBMIT_SUCCESS(1008,"提交成功"),
    INITDC(1009,"initDc成功"),
    PASSENGER_DTO(1010,"提交获取联系人成功"),
    CHECKORDER(1011,"检查订单成功"),
    GETQUEUE_SUCCESS(1012,"获取队列成功"),
    CONFIRM_SUCCESS(1013,"提交订单成功"),
    GET_WAIT_QUEUE(1014,"获取等待队列"),
    RESULT_ORDER(1015,"获取排队结果成功"),
    NO_COMPLETE_ORDER(1016,"查询未完成订单"),
    LEFTTICKET_QUREY(1017,"余票查询成功"),
    LEFTTICKET_INIT(1018,"余票查询初始化成功"),
    GET_DEVICE(1019,"获取设备信息成功"),
    INDEX_LOGIN_CONF(1020,"index login conf success"),
    GET_STATION(1021,"获取站点信息成功"),

    /**
     * 可重试的
     */
    CAPTCHA_EXPIRE(2000,"验证码过期,请重新获取"),
    GETCOOKIE_ERROR(2001,"获取cookei的url失败"),
    INIT_CLIENT_ERROR(2002,"初始化client失败"),
    PASSCODE_ERROR(2003,"获取验证码结果失败"),
    NO_COOKIE_URL(2004,"没有找到获取cookie的url"),
    SUBMITORDER_ERROR(2005,"订单提交失败"),
    GET_QUEUE_COUNT_ERROR(2006,"获取队列出错"),
    NO_QUERY_URL(2007,"没有查询到余票url"),
    NETWORK_ERROR(2008,"网络链接异常"),


    /**
     * 公共异常
     */
    HTTP_302(302,"302异常"),


    /**
     * 退出异常
     */
    PASSWORD_ERROR(9000,"密码输入错误"),
    CONFIRMQUEUE_ERROR(9001,"提交订单失败"),
    NO_PASSENGERS(9002,"没有查询到乘车人"),




    UNKOWN_ERROR(9999,"未知异常")
    ;





    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }


}
