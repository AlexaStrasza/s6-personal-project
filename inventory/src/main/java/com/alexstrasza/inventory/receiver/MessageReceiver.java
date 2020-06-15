package com.alexstrasza.inventory.receiver;

import com.alexstrasza.inventory.components.InventoryHolder;
import com.alexstrasza.inventory.models.ItemBase;
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
    InventoryHolder inventory;

    @RabbitListener(queues = "${alexstrasza.queue.inventory.auctionProcessing}")
    public void auctionDataReceiver(Message message) throws JsonProcessingException // JSON
    {
        String userId = message.getMessageProperties().getHeader("userId").toString();
        ObjectMapper objectMapper = new ObjectMapper();
        ItemBase item = objectMapper.readValue(new String(message.getBody(), StandardCharsets.UTF_8), ItemBase.class);

        inventory.AddItemToPlayer(userId, item);
    }
}
