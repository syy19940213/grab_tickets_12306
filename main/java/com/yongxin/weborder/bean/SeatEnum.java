package com.yongxin.weborder.bean;

public enum SeatEnum
{
	SEAT_0("无座",  ""),
	SEAT_1("一等座", "M"),
	SEAT_2("二等座",  "O"),
	SEAT_3("硬座", "1"),
	SEAT_4("软座", "2"),
	SEAT_7("硬卧", "3"),
	SEAT_9("软卧",  "4"),
	SEAT_11("商务座",  "9"),
	SEAT_12("特等座", "P"),
	SEAT_14("高级软卧",  "6"),
	SEAT_15("动卧", "F"),
	SEAT_16("高级动卧","A"),
	SEAT_17("一等软座","7"),
	SEAT_18("二等软座","8"),
	SEAT_19("包厢硬卧","5"),
	SEAT_23("软卧代二等座","O"),
	SEAT_24("硬卧代硬座","1"),
	SEAT_20("一人软包","H");

	private SeatEnum(String seatName ,String seatCode12306)
	{
		this.seatName = seatName;
		this.seatCode12306 = seatCode12306;
	}


	private String seatName;
	private String seatCode12306;


	public String getSeatName()
	{
		return seatName;
	}

	public void setSeatName(String seatName)
	{
		this.seatName = seatName;
	}

	public String getSeatCode12306()
	{
		return seatCode12306;
	}

	public void setSeatCode12306(String seatCode12306)
	{
		this.seatCode12306 = seatCode12306;
	}


	/**
	 * 根据坐席名称获取code
	 * @param seatName
	 * @return
	 */
	public static String getSeatCodeByName(String seatName)
	{
		for (SeatEnum s : SeatEnum.values())
		{
			if (seatName.equals(s.getSeatName()))
			{
				return s.getSeatCode12306();
			}
		}
		return null;
	}
}
