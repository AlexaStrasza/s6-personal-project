package com.alexstrasza.currency.controller;

import com.alexstrasza.currency.components.CurrencyManager;
import com.alexstrasza.currency.models.DataContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("currency/")
public class CurrencyController
{
    @Autowired
    CurrencyManager currency;

    // Retrieve player currency
    @GetMapping("retrieveBalance")
    public int RetrieveBalance(Principal principal)
    {
        return currency.GetCurrency(principal.getName());
    }

    // Retrieve floating currency
    @GetMapping("retrieveFloating")
    public int RetrieveFloating(Principal principal)
    {
        return currency.GetFloatingCurrency(principal.getName());
    }

    // Raise/bid, returns if successful or not
//    @PostMapping("placeBid")
//    public int PlaceBid(@RequestBody DataContainer amount, @RequestHeader String auctionId, Principal principal)
//    {
//        return currency.Bid(principal.getName(), auctionId, amount.heldInt);
//    }
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
