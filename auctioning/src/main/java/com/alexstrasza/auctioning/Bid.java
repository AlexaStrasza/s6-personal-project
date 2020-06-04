package com.alexstrasza.auctioning;

import java.sql.Time;

public class Bid implements Comparable<Bid>
{
    public int bid;
    public Time bidDate;
    @Override
    public int compareTo(Bid other)
    {
        return bidDate.compareTo(other.bidDate);
    }
}
