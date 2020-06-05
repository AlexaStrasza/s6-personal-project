package com.alexstrasza.auctioning.controller;

import com.alexstrasza.auctioning.models.Auction;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("auction/")
public class AuctionController
{


    // Retrieve auctions with filters
    @PostMapping("retrieveAuctions")
    public List<Auction> RetrieveAuctions(@RequestBody byte[] data, @RequestHeader String SocketConnection, @RequestHeader String TimeStamp)
    {
        return new ArrayList<>();
    }
    // Create Auction
    @PostMapping("new")
    public boolean CreateNewAuction(@RequestBody Auction data)
    {

        return true;
    }
    // Canceling an Auction
    // Post Bid

}
