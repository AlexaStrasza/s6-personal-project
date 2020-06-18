package com.alexstrasza.auctioning.models;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Filter;

@Entity
@Table(name = "auction")
public class Auction
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String auctionId;
    public boolean auctionEnded = false;
    public long auctionStartTime;
    public long auctionEndTime;
    @OneToOne(cascade = CascadeType.ALL)
    public ItemBase item;
    public int priceperunit;
    public boolean allowBuyout = false;
    public int buyoutPrice;
    public int highestBid;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bidhistory")
    public List<Bid> bidHistory = new ArrayList<>();
    public String creator;

    // Might be better for the backend to have a lookup table instead of getting these values from the frontend.
    // These fields could be edited in the frontend resulting in the auction to never be searched properly.
    @OneToOne(cascade = CascadeType.ALL)
    public FilterOptions filterOptions;

    public Auction() { }

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
        winLossObject.auctionId = auctionId;
        return winLossObject;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getAuctionId()
    {
        return auctionId;
    }

    public void setAuctionId(String auctionId)
    {
        this.auctionId = auctionId;
    }

    public boolean isAuctionEnded()
    {
        return auctionEnded;
    }

    public void setAuctionEnded(boolean auctionEnded)
    {
        this.auctionEnded = auctionEnded;
    }

    public long getAuctionStartTime()
    {
        return auctionStartTime;
    }

    public void setAuctionStartTime(long auctionStartTime)
    {
        this.auctionStartTime = auctionStartTime;
    }

    public long getAuctionEndTime()
    {
        return auctionEndTime;
    }

    public void setAuctionEndTime(long auctionEndTime)
    {
        this.auctionEndTime = auctionEndTime;
    }

    public ItemBase getItem()
    {
        return item;
    }

    public void setItem(ItemBase item)
    {
        this.item = item;
    }

    public int getPriceperunit()
    {
        return priceperunit;
    }

    public void setPriceperunit(int priceperunit)
    {
        this.priceperunit = priceperunit;
    }

    public boolean isAllowBuyout()
    {
        return allowBuyout;
    }

    public void setAllowBuyout(boolean allowBuyout)
    {
        this.allowBuyout = allowBuyout;
    }

    public int getBuyoutPrice()
    {
        return buyoutPrice;
    }

    public void setBuyoutPrice(int buyoutPrice)
    {
        this.buyoutPrice = buyoutPrice;
    }

    public int getHighestBid()
    {
        return highestBid;
    }

    public void setHighestBid(int highestBid)
    {
        this.highestBid = highestBid;
    }

    public List<Bid> getBidHistory()
    {
        return bidHistory;
    }

    public void setBidHistory(List<Bid> bidHistory)
    {
        this.bidHistory = bidHistory;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public FilterOptions getFilterOptions()
    {
        return filterOptions;
    }

    public void setFilterOptions(FilterOptions filterOptions)
    {
        this.filterOptions = filterOptions;
    }
}
