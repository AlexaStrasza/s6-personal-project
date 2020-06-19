package com.alexstrasza.auctioning.components;
import com.alexstrasza.auctioning.dao.AuctionDao;
import com.alexstrasza.auctioning.dao.UsersDao;
import com.alexstrasza.auctioning.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class AuctionManager
{
    @Autowired
    RabbitMessenger rabbitMessenger;

    @Autowired
    private AuctionDao auctionDao;

    @Autowired
    private UsersDao userDao;

    public AuctionManager() { }

    @Scheduled(cron = "0 */1 * * * *")
    @SchedulerLock(name = "TaskScheduler_scheduledTask", lockAtLeastFor = "30s", lockAtMostFor = "1m")
    public void CheckEndedAuctions()
    {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);

        List<Auction> indexedAuctions = auctionDao.findAllByAuctionEndedIsFalse();
        List<Auction> auctionsToRemove = new ArrayList<>();

        System.out.println("Checking for auctions to conclude");
        System.out.println("Active Auctions: " + indexedAuctions.size());
        for (Auction auction : indexedAuctions)
        {
            if (!auction.auctionEnded)
            {
                if (calendar.getTimeInMillis() > auction.auctionEndTime)
                {
                    auction.auctionEnded = true;

                    if (auction.creator.equals(auction.GetWinner()))
                    {
                        rabbitMessenger.NotifyBids(new Bid(-20, ""), auction.auctionId, "false");
                    }
                    else
                    {
                        rabbitMessenger.NotifyBids(new Bid(-10, auction.GetWinner()), auction.auctionId, "false");
                    }
                    // Do stuff when auction is over
                    // Notify currency service with involved users
                    rabbitMessenger.SendCurrencyUpdate(auction.ConstructWinLossObject(), auction.creator);
                    // Notify item system for item transfer
                    rabbitMessenger.SendInventoryUpdate(auction.item, auction.GetWinner());
                    auctionsToRemove.add(auction);
                }
            }
        }

        indexedAuctions.removeAll(auctionsToRemove);

        System.out.println(auctionsToRemove.size() + " auctions concluded");
    }

    public Auction GetAuction(String auctionId)
    {
        return auctionDao.findByAuctionId(auctionId);
    }

    public AuctionCollection GetAuctions(String name, String type, String rarity, boolean buyout)
    {
        // Should really be done in database with a query to filter results.
        List<Auction> auctions = auctionDao.findAll();

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
        holder.auctions = auctionDao.findAll();
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
        auction.auctionId = auction.creator + "-" + auction.item.itemBaseId + "-" + auction.item.stackSize + "-" + UUID.randomUUID();

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

        auctionDao.save(auction);
    }

//    public Bid PlaceBuyout(String auctionId, String user)
//    {
//        Auction auction = GetAuction(auctionId);
//        if (auction == null) return null;
//
//        Bid bid = new Bid(auction.buyoutPrice, user);
//        auction.bidHistory.add(bid);
//        auctionDao.save(auction);
//
//        auction.auctionEnded = true;
//        return bid;
//    }

    public Bid PlaceBid(int bidAmount, String auctionId, String user)
    {
        // Get the auction
        Auction auction = GetAuction(auctionId);

        if (auction == null) return null;

        if (bidAmount >= auction.GetPriceTotal())
        {
            Bid bid = new Bid(bidAmount, user);
            auction.bidHistory.add(bid);
            auctionDao.save(auction);
            return bid;
        }
        else return null;
    }
}
