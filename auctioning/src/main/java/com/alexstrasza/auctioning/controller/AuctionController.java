package com.alexstrasza.auctioning.controller;

import com.alexstrasza.auctioning.components.Auctioning;
import com.alexstrasza.auctioning.components.RabbitMessager;
import com.alexstrasza.auctioning.models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auction/")
public class AuctionController
{
    private String url = "http://localhost:8084/currency/";

    @Autowired
    RabbitMessager rabbitMessager;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    Auctioning auctioning;

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
    public boolean DropAuction(@RequestBody DataContainer auctionId, @RequestHeader String user)
    {

        return true;
    }

    // Post Bid
    @PostMapping("placeBid")
    public String PlaceBid(@RequestBody DataContainer amount, @RequestHeader String auctionId, @RequestHeader String user)
    {
        if (auctioning.GetAuction(auctionId) == null) return "Error placing bid. Auction not found.";
        System.out.println(user);
        if (auctioning.GetAuction(auctionId).creator.equals(user)) return "Can't place bids on own auction.";
        if (auctioning.GetAuction(auctionId).GetPriceTotal() >= amount.heldInt) return "Offer below minimum offer.";
        String fullUrl = url + "placeBid";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("auctionId", auctionId);
        headers.add("user", user);

        HttpEntity<DataContainer> entity = new HttpEntity<>(amount, headers);

        int returnValue = restTemplate.postForObject(fullUrl, entity, Integer.class);

        switch (returnValue)
        {
            case -1:
                // User didnt exist in currency system??
                return "Error posting bid";
            case 0:
                // Not enough currency
                return "Not enough available funds";
            case 1:
                // Successfully placed data in currency system

                Bid bid = auctioning.PlaceBid(amount.heldInt, auctionId, user);

                // Notify other subscribed users of new bid
                rabbitMessager.NotifyBids(bid, auctionId);

                return "Bid successfully placed";
            case 2:
                // Higher bid already placed
                return "Higher bid already placed";
        }

        return "Error when posting bid, are servers down?";
    }
}
