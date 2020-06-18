package com.alexstrasza.inventory.controller;

import com.alexstrasza.inventory.components.InventoryManager;
import com.alexstrasza.inventory.components.RabbitMessager;
import com.alexstrasza.inventory.models.AuctionCreationObject;
import com.alexstrasza.inventory.models.DataContainer;
import com.alexstrasza.inventory.entity.ItemBase;
import com.alexstrasza.inventory.entity.InventoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("inventory/")
public class InventoryController
{
    @Autowired
    RabbitMessager messager;

    @Autowired
    InventoryManager inventory;

    // post auction
    @PostMapping("createNewAuction")
    public String CreateNewAuction(@RequestBody AuctionCreationObject auctionData, Principal principal)
    {
        if (inventory.GetInventory(principal.getName()).DoesPlayerOwnItems(auctionData.itemId, auctionData.amount))
        {
            messager.SendAuctionCreation(auctionData, principal.getName());

            inventory.RemoveItemFromPlayer(principal.getName(), new ItemBase(auctionData.itemId, auctionData.amount));
            return "Auction Placed";
        }
        return "Item not owned";
    }

    @GetMapping("testing")
    public void testing() throws InterruptedException
    {
        System.out.println("Creating some player data");

//        inventory.CreateInventory("User 1");
//        inventory.CreateInventory("User 2");
//        inventory.CreateInventory("User 3");
//
//        inventory.AddItemToPlayer("User 1", new ItemBase(0, 1));
//        inventory.AddItemToPlayer("User 1", new ItemBase(1, 1));
//        inventory.AddItemToPlayer("User 1", new ItemBase(3, 101));
//        inventory.AddItemToPlayer("User 2", new ItemBase(0, 1));
//        inventory.AddItemToPlayer("User 2", new ItemBase(2, 1));
//        inventory.AddItemToPlayer("User 2", new ItemBase(3, 203));
    }

        // retrieve inventory
    @PostMapping("retrieveInventory")
    public InventoryEntity retrieveInventory(@RequestBody DataContainer user, Principal principal)
    {
        return inventory.GetInventory(principal.getName());
    }
}
