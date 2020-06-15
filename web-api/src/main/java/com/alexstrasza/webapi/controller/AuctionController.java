package com.alexstrasza.webapi.controller;

import com.alexstrasza.webapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;

@RestController
@RequestMapping("auction/")
public class AuctionController
{

    private String url = "http://localhost:8082/auction/";

    @Autowired
    private RestTemplate restTemplate;

    // Retrieve auctions with filters
    @PostMapping("retrieveAuctions")
    public AuctionCollection RetrieveAuctions(@RequestBody FilterOptions filters)
    {
        String fullUrl = url + "retrieveAuctions";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FilterOptions> entity = new HttpEntity<>(filters, headers);

        return restTemplate.postForObject(fullUrl, entity, AuctionCollection.class);
    }

    @PostMapping("retrieveUpToDateState")
    public Auction RetrieveUpToDateState(@RequestBody DataContainer auctionId)
    {
        String fullUrl = url + "retrieveUpToDateState";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DataContainer> entity = new HttpEntity<>(auctionId, headers);

        return restTemplate.postForObject(fullUrl, entity, Auction.class);
    }

    @PostMapping("retrieveAllAuctions")
    public AuctionCollection RetrieveAllAuctions()
    {
        String fullUrl = url + "retrieveAllAuctions";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FilterOptions> entity = new HttpEntity<>(null, headers);

        return restTemplate.postForObject(fullUrl, entity, AuctionCollection.class);
    }

    // Create Auction
//    @PostMapping("createNewAuction")
//    public String CreateNewAuction(@RequestBody Auction auction, @RequestHeader String user)
//    {
//        String fullUrl = url + "createNewAuction";
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("user", user);
//
//        HttpEntity<Auction> entity = new HttpEntity<>(auction, headers);
//
//        return restTemplate.postForObject(fullUrl, entity, String.class);
//    }

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
        String fullUrl = url + "placeBid";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("auctionId", auctionId);
        headers.add("user", user);

        HttpEntity<DataContainer> entity = new HttpEntity<>(amount, headers);

        return restTemplate.postForObject(fullUrl, entity, String.class);
    }

    @Autowired
    SimpMessagingTemplate simp;

    public void SendMessage(String destination, Object objToConvert)
    {
        simp.convertAndSend("/topic/" + destination, objToConvert);
    }
}
