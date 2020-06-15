package com.alexstrasza.auctioning.models;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Bid
{
    public int bid;
    public long bidDate;
    public String bidder;

    public Bid() { }

    public Bid (int amount, String user)
    {
        bid = amount;
        bidder = user;
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        bidDate = c.getTimeInMillis();
    }
}
