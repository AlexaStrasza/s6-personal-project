package com.alexstrasza.auctioning.models;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Filter;

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

    public int GetPriceTotal()
    {
        return item.stackSize * priceperunit;
    }

    public int GetHighestBid()
    {
        int highest = 0;
        for (Bid bid : bidHistory)
        {
            if (bid.bid > highest) highest = bid.bid;
        }
        return highest;
    }

    private Bid GetHighestBidObj()
    {
        Bid highest = new Bid();
        for (Bid bid : bidHistory)
        {
            if (bid.bid > highest.bid) highest = bid;
        }
        return highest;
    }

    public String GetWinner()
    {
        String bidder = GetHighestBidObj().bidder;
        if (bidder.equals(""))
        {
            // Highest bid is de pregenerated bid to show minimum bid
            // Returning item to owner
            return creator;
        }
        else
        {
            return bidder;
        }
    }

    public WinLossObject ConstructWinLossObject()
    {
        WinLossObject winLossObject = new WinLossObject();

        winLossObject.winner = GetWinner();

        List<String> losers = new ArrayList<>();
        for (Bid bid : bidHistory)
        {
            if (!bid.bidder.equals(winLossObject.winner)) losers.add(bid.bidder);
        }

        winLossObject.losers = losers;
        winLossObject.auctionId = id;
        return winLossObject;
    }
}
