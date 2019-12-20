package com.yongxin.weborder.cache;

import com.yongxin.weborder.bean.Station;
import com.yongxin.weborder.common.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

public class StationCache
{
    private static List<Station> stationList = new ArrayList<>();


    /**
     * 根据站点名称获取三字码
     * @param name
     * @return
     */
    public static String getCodeByName(String name)
    {
        for (Station station : stationList)
        {
            if (station.getName().equals(name))
            {
                return station.getCode();
            }
        }
        return null;
    }


    /**
     * 解析站点字符串
     * @param str
     */
    public static void parseStaionStr(String str)
    {
        String[] args = str.split("@");
        for (int i = 0 ; i < args.length ; i++)
        {
            String stationStr = args[i];
            if (ObjectUtil.isNotNull(stationStr))
            {
                Station station = parseStaion(stationStr);
                stationList.add(station);
            }
        }

    }

    private static Station parseStaion(String stationStr)
    {
        Station station = new Station();
        String[] args = stationStr.split("\\|");
        station.setJp(args[0]);
        station.setName(args[1]);
        station.setCode(args[2]);
        station.setQp(args[3]);
        station.setPy(args[4]);
        station.setNum(args[5]);
        return station;
    }

}
