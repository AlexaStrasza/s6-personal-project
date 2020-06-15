package com.alexstrasza.webapi.models;

import java.util.ArrayList;
import java.util.List;

public class Auction
{
    public boolean auctionEnded = false;
    public String id;
    public long auctionStartTime;
    public long auctionEndTime;
    public ItemBase item;
    public int priceperunit;
    public boolean allowBuyout = false;
    public int buyoutPrice;
    public int highestBid;
    public List<Bid> bidHistory = new ArrayList<>();
    public String creator;

    // Might be better for the backend to have a lookup table instead of getting these values from the frontend.
    // These fields could be edited in the frontend resulting in the auction to never be searched properly.
    public FilterOptions filterOptions;

    public Auction() { }

    public Bid PlaceBid(int amount, String user)
    {
        Bid bid = new Bid(amount, user);
        bidHistory.add(bid);
        return bid;
    }
}
