package com.alexstrasza.currency.controller;

import com.alexstrasza.currency.components.CurrencyHolder;
import com.alexstrasza.currency.models.DataContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("currency/")
public class CurrencyController
{
    @Autowired
    CurrencyHolder currency;

    // Retrieve player currency
    @PostMapping("retrieveBalance")
    public int RetrieveBalance(@RequestBody DataContainer user, Principal principal)
    {
        return currency.GetCurrency(principal.getName());
    }

    // Retrieve floating currency
    @PostMapping("retrieveFloating")
    public int RetrieveFloating(@RequestBody DataContainer user, Principal principal)
    {
        return currency.GetFloatingCurrency(principal.getName());
    }

    // Raise/bid, returns if successful or not
    @PostMapping("placeBid")
    public int PlaceBid(@RequestBody DataContainer amount, @RequestHeader String auctionId, Principal principal)
    {
        return currency.Bid(principal.getName(), auctionId, amount.heldInt);
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("testing")
    public void testing()
    {
//        currency.ChangeCurrency("User 1", 100);
//        currency.CreatePlayerForTesting();
//        currency.Bid("User 1", "test", 1000);
//        currency.PayAuction("User 1", "test", "User 3");
    }
}
