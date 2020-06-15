package com.alexstrasza.inventory.controller;

import com.alexstrasza.inventory.components.InventoryHolder;
import com.alexstrasza.inventory.components.RabbitMessager;
import com.alexstrasza.inventory.models.AuctionCreationObject;
import com.alexstrasza.inventory.models.DataContainer;
import com.alexstrasza.inventory.models.ItemBase;
import com.alexstrasza.inventory.models.PlayerInventory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("inventory/")
public class InventoryController
{
    @Autowired
    InventoryHolder inventory;

    @Autowired
    RabbitMessager messager;

    // post auction
    @PostMapping("createNewAuction")
    public String CreateNewAuction(@RequestBody AuctionCreationObject auctionData, @RequestHeader String user)
    {
        if (inventory.GetInventory(user).DoesPlayerOwnItems(auctionData.itemId, auctionData.amount))
        {
            messager.SendAuctionCreation(auctionData, user);

            inventory.RemoveItemFromPlayer(user, new ItemBase(auctionData.itemId, auctionData.amount));
            return "Auction Placed";
        }
        return "Item not owned";
    }

    @GetMapping("testing")
    public void testing() throws InterruptedException
    {
        System.out.println("Creating some player data");

        inventory.CreateInventory("User 1");
        inventory.CreateInventory("User 2");
        inventory.CreateInventory("User 3");

        inventory.AddItemToPlayer("User 1", new ItemBase(0, 1));
        inventory.AddItemToPlayer("User 1", new ItemBase(1, 1));
        inventory.AddItemToPlayer("User 1", new ItemBase(3, 101));
        inventory.AddItemToPlayer("User 2", new ItemBase(0, 1));
        inventory.AddItemToPlayer("User 2", new ItemBase(2, 1));
        inventory.AddItemToPlayer("User 2", new ItemBase(3, 203));
    }

        // retrieve inventory
    @PostMapping("retrieveInventory")
    public PlayerInventory retrieveInventory(@RequestBody DataContainer user)
    {
        return inventory.GetInventory(user.heldString);
    }
}
