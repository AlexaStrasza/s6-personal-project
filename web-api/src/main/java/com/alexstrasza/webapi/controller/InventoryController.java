package com.alexstrasza.webapi.controller;

import com.alexstrasza.webapi.models.Auction;
import com.alexstrasza.webapi.models.AuctionCreationObject;
import com.alexstrasza.webapi.models.DataContainer;
import com.alexstrasza.webapi.models.PlayerInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("inventory/")
public class InventoryController
{
    private String url = "http://localhost:8083/inventory/";

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("createNewAuction")
    public String CreateNewAuction(@RequestBody AuctionCreationObject auction, @RequestHeader String user)
    {
        String fullUrl = url + "createNewAuction";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user", user);

        HttpEntity<AuctionCreationObject> entity = new HttpEntity<>(auction, headers);

        return restTemplate.postForObject(fullUrl, entity, String.class);
    }

    @PostMapping("retrieveInventory")
    public PlayerInventory RetrieveInventory(@RequestBody DataContainer user)
    {
        String fullUrl = url + "retrieveInventory";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DataContainer> entity = new HttpEntity<>(user, headers);

        return restTemplate.postForObject(fullUrl, entity, PlayerInventory.class);
    }
}
