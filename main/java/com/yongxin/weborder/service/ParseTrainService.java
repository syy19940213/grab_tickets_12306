package com.yongxin.weborder.service;

import com.alibaba.fastjson.JSONArray;
import com.yongxin.weborder.bean.TrainInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParseTrainService
{

    private Logger logger = LoggerFactory.getLogger(ParseTrainService.class);

    public List<TrainInfo> parseStringToList(JSONArray array)
    {
        List<TrainInfo> list = new ArrayList<>();
        for (int i =0 ; i < array.size() ; i ++)
        {
            list.add(parseStringToTrainInfo(array.getString(i)));
        }
        return list;
    }


    public TrainInfo parseStringToTrainInfo(String str)
    {
        TrainInfo trainInfo  = new TrainInfo();
        String[] args = str.split("\\|");

        String ticketSecure = null;
        try
        {
            ticketSecure = URLDecoder.decode(args[0],"UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            logger.info("ticketSecure解码失败",e);
        }
        trainInfo.setButton(args[1]);
        trainInfo.setIsBuyTime(args[11]);
        trainInfo.setTicketSecure(ticketSecure);
        trainInfo.setTrainCode(args[3]);
        trainInfo.setFcode(args[6]);
        trainInfo.setTcode(args[7]);
        trainInfo.setStartTime(args[8]);
        trainInfo.setArriveTime(args[9]);
        trainInfo.setUseTime(args[10]);
        trainInfo.setTrainDate(args[13]);


        trainInfo.setGjrwNum(args[21]);
        trainInfo.setRwNum(args[23]);
        trainInfo.setRzNum(args[24]);

        trainInfo.setWzNum(args[26]);

        trainInfo.setYwNum(args[28]);
        trainInfo.setYzNum(args[29]);
        trainInfo.setEdzNum(args[30]);
        trainInfo.setYdzNum(args[31]);
        trainInfo.setSwzNum(args[32]);

        trainInfo.setHbStatus(args[37]);

        trainInfo.setYpInfo(args[12]);
        trainInfo.setSeatTypes(args[35]);

        return trainInfo;
    }
}
