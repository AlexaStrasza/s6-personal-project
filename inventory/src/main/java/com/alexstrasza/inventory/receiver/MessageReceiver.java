package com.alexstrasza.inventory.receiver;

import com.alexstrasza.inventory.components.InventoryManager;
import com.alexstrasza.inventory.dao.InventoryDao;
import com.alexstrasza.inventory.dao.UsersDao;
import com.alexstrasza.inventory.entity.InventoryEntity;
import com.alexstrasza.inventory.entity.UsersEntity;
import com.alexstrasza.inventory.entity.ItemBase;
import com.alexstrasza.inventory.models.UserModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MessageReceiver
{
    @Autowired
    private UsersDao usersDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private InventoryManager inventory;

    @RabbitListener(queues = "${alexstrasza.queue.inventory.userCreate}")
    public void createUser(Message message)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        UserModel user = null;
        try
        {
            user = objectMapper.readValue(new String(message.getBody(), StandardCharsets.UTF_8), UserModel.class);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        UsersEntity test = usersDao.findByUsername(user.getUsername());
        if (test == null)
        {
            UsersEntity entity = new UsersEntity(user.getUsername());

            InventoryEntity playerInventory = new InventoryEntity(entity);
            inventoryDao.save(playerInventory);

            inventory.AddItemToPlayer(user.getUsername(), new ItemBase(0, 10));
            inventory.AddItemToPlayer(user.getUsername(), new ItemBase(1, 10));
            inventory.AddItemToPlayer(user.getUsername(), new ItemBase(2, 10));
            inventory.AddItemToPlayer(user.getUsername(), new ItemBase(3, 10));
        }
    }

    @RabbitListener(queues = "${alexstrasza.queue.inventory.auctionProcessing}")
    public void auctionDataReceiver(Message message) // JSON
    {
        String userId = message.getMessageProperties().getHeader("userId").toString();
        ObjectMapper objectMapper = new ObjectMapper();
        ItemBase item = null;
        try
        {
            item = objectMapper.readValue(new String(message.getBody(), StandardCharsets.UTF_8), ItemBase.class);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }

        if (item != null)
        {
            inventory.AddItemToPlayer(userId, item);
        }
    }
}
