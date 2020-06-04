package com.alexstrasza.auctioning;

import org.apache.tomcat.util.digester.ArrayStack;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Auction
{
    public String id;
    public Time auctionStartTime;
    public ItemBase itemSold;
    public int amount;
    public int priceperunit;
    public int pricetotal;
    public boolean allowBuyout = false;
    public int buyoutPrice;
    public List<Bid> bidHistory = new ArrayList<>();
    public int creator;
}
