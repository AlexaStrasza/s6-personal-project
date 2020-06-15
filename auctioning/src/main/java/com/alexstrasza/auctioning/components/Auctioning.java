package com.alexstrasza.auctioning.components;
import com.alexstrasza.auctioning.models.*;
import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Auctioning
{
    @Autowired
    RabbitMessager rabbitMessager;

    public List<Auction> indexedAuctions = new ArrayStack<>();

    private Auctioning() { }

    @Scheduled(fixedRate = 5000)
    private void CheckEndedAuctions()
    {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
//        auction.auctionStartTime = ;

        System.out.println("Checking for auctions to conclude");
        System.out.println("Active Auctions: " + indexedAuctions.size());
        int i = 0;
        for (Auction auction : indexedAuctions)
        {
            if (!auction.auctionEnded)
            {
                if (calendar.getTimeInMillis() > auction.auctionEndTime)
                {
                    auction.auctionEnded = true;
                    i++;
                }
            }
        }
        System.out.println(i + " auctions concluded");
    }

    @Scheduled(fixedRate = 5000)
    private void ResolveConcludedAuctions()
    {
        List<Auction> auctionsToRemove = new ArrayList<>();

        int i = 0;
        for (Auction auction : indexedAuctions)
        {
            if (auction.auctionEnded)
            {
                // Do stuff when auction is over
                // Notify currency service with involved users
                rabbitMessager.SendCurrencyUpdate(auction.ConstructWinLossObject(), auction.creator);
                // Notify item system for item transfer
                rabbitMessager.SendInventoryUpdate(auction.item, auction.GetWinner());
                auctionsToRemove.add(auction);
            }
        }

        indexedAuctions.removeAll(auctionsToRemove);
    }

    public Auction GetAuction(String auctionId)
    {
        for (Auction auction : indexedAuctions)
        {
            if (auction.id.equals(auctionId))
            {
                return auction;
            }
        }
        return null;
    }

    public AuctionCollection GetAuctions(String name, String type, String rarity, boolean buyout)
    {
        // Should really be done in database with a query to filter results.
        List<Auction> auctions = new ArrayList<>(indexedAuctions);

        List<Auction> toRemove = new ArrayList<>();
        for (Auction auction : auctions)
        {
            if (auction.auctionEnded)
            {
                toRemove.add(auction);
                continue;
            }

            if (!name.isEmpty())
            {
                if (!CheckName(name, auction))
                {
                    toRemove.add(auction);
                    continue;
                }
            }

            if (!type.equals("Any"))
            {
                if (!CheckType(type, auction))
                {
                    toRemove.add(auction);
                    continue;
                }
            }
            if (!rarity.equals("Any"))
            {
                if (!CheckRarity(rarity, auction))
                {
                    toRemove.add(auction);
                    continue;
                }
            }
            if (buyout)
            {
                if (!CheckBuyout(auction))
                {
                    toRemove.add(auction);
                }
            }
        }

        auctions.removeAll(toRemove);
        AuctionCollection holder = new AuctionCollection();
        holder.auctions = auctions;
        return holder;
    }

    private boolean CheckName(String name, Auction auction)
    {
        return auction.filterOptions.name.contains(name);
    }

    private boolean CheckType(String type, Auction auction)
    {
        return auction.filterOptions.type.equals(type);
    }

    private boolean CheckRarity(String rarity, Auction auction)
    {
        return auction.filterOptions.rarity.equals(rarity);
    }

    private boolean CheckBuyout(Auction auction)
    {
        return auction.allowBuyout;
    }

    public AuctionCollection GetAllAuctions()
    {
        AuctionCollection holder = new AuctionCollection();
        holder.auctions = indexedAuctions;
        return holder;
    }

    public void StartAuction(AuctionCreationObject auctionData, String creator)
    {
        Auction auction = new Auction();
        auction.item = new ItemBase(auctionData.itemId, auctionData.amount);

        auction.priceperunit = auctionData.pricePerUnit;
        auction.allowBuyout = auctionData.allowBuyout;
        auction.buyoutPrice = auctionData.buyoutPrice;
        auction.filterOptions = auctionData.filterOptions;
        auction.creator = creator;
        auction.id = auction.creator + "-" + auction.item.itemBaseId + "-" + auction.item.stackSize + "-" + UUID.randomUUID();

        indexedAuctions.add(auction);

        // Setup start and end times
        Bid bid = new Bid();
        bid.bidder = "";
        bid.bid = auction.item.stackSize * auction.priceperunit;
        auction.bidHistory.add(bid);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        auction.auctionStartTime = calendar.getTimeInMillis();

        calendar.add(Calendar.MINUTE, 2);

        auction.auctionEndTime = calendar.getTimeInMillis();

        System.out.println(indexedAuctions.size());
    }

    public Bid PlaceBid(int bidAmount, String auctionId, String user)
    {
        // Get the auction
        Auction auction = GetAuction(auctionId);

        if (auction == null) return null;

        if (bidAmount >= auction.GetPriceTotal())
        {
            return auction.PlaceBid(bidAmount, user);
        }
        else return null;
    }
}
