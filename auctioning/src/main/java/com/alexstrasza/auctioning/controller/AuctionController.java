package com.alexstrasza.auctioning.controller;

import com.alexstrasza.auctioning.components.AuctionManager;
import com.alexstrasza.auctioning.components.RabbitMessenger;
import com.alexstrasza.auctioning.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("auction/")
public class AuctionController
{
    private String url = "http://localhost:8084/currency/";

    @Autowired
    RabbitMessenger rabbitMessenger;

    @Autowired
    AuctionManager auctioning;

    // Retrieve auctions with filters
    @PostMapping("retrieveAuctions")
    public AuctionCollection RetrieveAuctions(@RequestBody FilterOptions filters)
    {
        return auctioning.GetAuctions(filters.name, filters.type, filters.rarity, filters.buyout);
    }

    @PostMapping("retrieveAllAuctions")
    public AuctionCollection RetrieveAllAuctions()
    {
        return auctioning.GetAllAuctions();
    }

    @PostMapping("retrieveUpToDateState")
    public Auction RetrieveUpToDateState(@RequestBody DataContainer auctionId)
    {
        return auctioning.GetAuction(auctionId.heldString);
    }

    // Canceling an Auction
    @PostMapping("dropAuction")
    public boolean DropAuction(@RequestBody DataContainer auctionId, Principal principal)
    {

        return false;
    }

    @PostMapping("buyout")
    public String Buyout(@RequestBody DataContainer auctionId, Principal principal)
    {
//        Auction auction = auctioning.GetAuction(auctionId.heldString);
//        if (auction == null) return "Error placing bid. Auction not found.";
//        System.out.println(principal.getName());
//        if (!auction.allowBuyout) return "This auction doesn't allow buyout";
//        if (auction.creator.equals(principal.getName())) return "Can't buyout own auction.";
//        if (auction.auctionEnded) return "This auction has ended";
//        rabbitMessager.SendBuyout(new DataContainer(auction.buyoutPrice), auctionId.heldString, principal.getName());
//        return "Sending buyout offer";
        return "Buyout not supported yet";
    }

    // Post Bid
    @PostMapping("placeBid")
    public String PlaceBid(@RequestBody DataContainer amount, @RequestHeader String auctionId, Principal principal)
    {
        Auction auction = auctioning.GetAuction(auctionId);
        if (auction == null) return "Error placing bid. Auction not found.";
        if (auction.creator.equals(principal.getName())) return "Can't place bids on own auction.";
        if (auction.GetPriceTotal() >= amount.heldInt) return "Offer below minimum offer.";
        if (auction.auctionEnded) return "This auction has ended";
        rabbitMessenger.SendBid(amount, auctionId, principal.getName());
        return "Sending bid offer";
    }
}
