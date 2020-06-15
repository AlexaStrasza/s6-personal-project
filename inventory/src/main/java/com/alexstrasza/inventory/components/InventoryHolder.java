package com.alexstrasza.inventory.components;

import com.alexstrasza.inventory.models.ItemBase;
import com.alexstrasza.inventory.models.PlayerInventory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryHolder
{
    public List<PlayerInventory> inventories = new ArrayList<>();

    private InventoryHolder() { }

    @Autowired
    RabbitMessager messager;

    public void CreatePlayerForTesting() throws InterruptedException
    {
        System.out.println("Creating some player data");

        CreateInventory("User 1");
        CreateInventory("User 2");
        CreateInventory("User 3");

        AddItemToPlayer("User 1", new ItemBase(0, 1));
        Thread.sleep(100);
        AddItemToPlayer("User 1", new ItemBase(1, 1));
        Thread.sleep(100);
        AddItemToPlayer("User 1", new ItemBase(3, 101));
        Thread.sleep(100);
        AddItemToPlayer("User 2", new ItemBase(0, 1));
        Thread.sleep(100);
        AddItemToPlayer("User 2", new ItemBase(2, 1));
        Thread.sleep(100);
        AddItemToPlayer("User 2", new ItemBase(3, 203));
    }

    public void CreateInventory(String user)
    {
        PlayerInventory playerInventory = new PlayerInventory();
        playerInventory.user = user;

        inventories.add(playerInventory);
    }

    public PlayerInventory GetInventory(String user)
    {
        for (PlayerInventory inventory : inventories)
        {
            if (inventory.user.equals(user)) return inventory;
        }
        return null;
    }

    public void AddItemToPlayer(String user, ItemBase item)
    {
        PlayerInventory inventory = GetInventory(user);
        inventory.AddItemToInventory(item);

        messager.SendInventoryUpdate(item, user, "add");
    }

    public boolean RemoveItemFromPlayer(String user, ItemBase item)
    {
        PlayerInventory inventory = GetInventory(user);
        boolean returnValue = inventory.RemoveItemFromInventory(item);

        if (returnValue)
        {
            messager.SendInventoryUpdate(item, user, "remove");
        }

        return returnValue;
    }
}
