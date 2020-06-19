package com.alexstrasza.inventory.components;

import com.alexstrasza.inventory.models.AuctionCreationObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMessenger
{
    @Value("${alexstrasza.rabbit.exchange}")
    private String directExchange;

    @Value("${alexstrasza.routing.webapi.inventoryChange}")
    private String webapiRouting;

    @Value("${alexstrasza.routing.auction.auctionCreation}")
    private String auctionCreationRouting;

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void SendAuctionCreation(AuctionCreationObject auctionData, String user)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try
        {
            json = objectMapper.writeValueAsString(auctionData);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        MessageProperties properties = new MessageProperties();
        properties.setHeader("userId", user);
        Message message = new Message(json.getBytes(), properties);
        rabbitTemplate.convertAndSend(directExchange, auctionCreationRouting, message);
    }

    public void SendInventoryUpdate(Object item, String user, String change)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try
        {
            json = objectMapper.writeValueAsString(item);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }

        MessageProperties properties = new MessageProperties();
        properties.setHeader("userId", user);
        properties.setHeader("changeType", change);
        Message message = new Message(json.getBytes(), properties);
        rabbitTemplate.convertAndSend(directExchange, webapiRouting, message);
    }
}
