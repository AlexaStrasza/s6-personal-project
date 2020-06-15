package com.alexstrasza.currency.controller;

import com.alexstrasza.currency.components.CurrencyHolder;
import com.alexstrasza.currency.models.DataContainer;
import com.alexstrasza.currency.models.PlayerCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("currency/")
public class CurrencyController
{
    @Autowired
    CurrencyHolder currency;

    // Retrieve player currency
    @PostMapping("retrieveBalance")
    public int RetrieveBalance(@RequestBody DataContainer user)
    {
        return currency.GetCurrency(user.heldString);
    }

    // Retrieve floating currency
    @PostMapping("retrieveFloating")
    public int RetrieveFloating(@RequestBody DataContainer user)
    {
        return currency.GetFloatingCurrency(user.heldString);
    }

    // Raise/bid, returns if successful or not
    @PostMapping("placeBid")
    public int PlaceBid(@RequestBody DataContainer amount, @RequestHeader String auctionId, @RequestHeader String user)
    {
        return currency.Bid(user, auctionId, amount.heldInt);
    }
    // Make user pay for auction

    // Refund invested money from auction


    @GetMapping("testing")
    public void testing()
    {
        currency.CreatePlayerForTesting();
    }
}
