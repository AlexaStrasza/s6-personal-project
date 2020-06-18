package com.alexstrasza.inventory.controller;

import com.alexstrasza.inventory.components.InventoryManager;
import com.alexstrasza.inventory.components.RabbitMessager;
import com.alexstrasza.inventory.dao.InventoryDao;
import com.alexstrasza.inventory.dao.UsersDao;
import com.alexstrasza.inventory.entity.UsersEntity;
import com.alexstrasza.inventory.models.AuctionCreationObject;
import com.alexstrasza.inventory.models.DataContainer;
import com.alexstrasza.inventory.entity.ItemBase;
import com.alexstrasza.inventory.entity.InventoryEntity;
import com.alexstrasza.inventory.models.UserModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
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

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private UsersDao usersDao;


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
    public void testing()
    {
        System.out.println("Creating some player data");

//        inventory.CreateInventory("User 1");
//        inventory.CreateInventory("User 2");
//        inventory.CreateInventory("User 3");
//        createUser("test");
//        createUser("test2");
//        createUser("test3");
//        UsersEntity entity = usersDao.findByUsername("test");
//        InventoryEntity playerInventory = new InventoryEntity(entity);
//        inventoryDao.save(playerInventory);
//
        inventory.AddItemToPlayer("test", new ItemBase(0, 1));
        inventory.AddItemToPlayer("test", new ItemBase(1, 1));
        inventory.AddItemToPlayer("test", new ItemBase(2, 1));
        inventory.AddItemToPlayer("test", new ItemBase(3, 101));
//        inventory.AddItemToPlayer("User 2", new ItemBase(0, 1));
//        inventory.AddItemToPlayer("User 2", new ItemBase(2, 1));
//        inventory.AddItemToPlayer("User 2", new ItemBase(3, 203));
    }

    public void createUser(String username)
    {
//        UsersEntity test = usersDao.findByUsername(username);
//        if (test == null)
//        {
//            UsersEntity entity = new UsersEntity(username);
//
//            InventoryEntity playerInventory = new InventoryEntity(entity);
//            inventoryDao.save(playerInventory);
//
//            inventory.AddItemToPlayer(username, new ItemBase(0, 10));
//            inventory.AddItemToPlayer(username, new ItemBase(1, 10));
//            inventory.AddItemToPlayer(username, new ItemBase(2, 10));
//            inventory.AddItemToPlayer(username, new ItemBase(3, 10));
//        }
    }


        // retrieve inventory
    @GetMapping("retrieveInventory")
    public InventoryEntity retrieveInventory(Principal principal)
    {
        return inventory.GetInventory(principal.getName());
    }
}
