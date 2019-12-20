package com.yongxin.weborder.utils;

import com.yongxin.weborder.common.utils.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainUtils
{
    private static Logger logger = LoggerFactory.getLogger(TrainUtils.class);

    public static boolean isTime12306()
    {
        String day = ObjectUtil.getDay();
        String starttime = day + " 06:01:00";
        String endtime = day + " 22:59:00";
        if (ObjectUtil.getTimeStampByString(starttime,"yyyy-MM-dd HH:mm:ss") < System.currentTimeMillis()
                && ObjectUtil.getTimeStampByString(endtime,"yyyy-MM-dd HH:mm:ss") > System.currentTimeMillis())
        {
            return true;
        }
        return false;
    }



}
