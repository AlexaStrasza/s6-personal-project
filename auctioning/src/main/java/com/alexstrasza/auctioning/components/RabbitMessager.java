package com.alexstrasza.auctioning.components;

import com.alexstrasza.auctioning.models.Bid;
import com.alexstrasza.auctioning.models.ItemBase;
import com.alexstrasza.auctioning.models.WinLossObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMessager
{
    @Value("${alexstrasza.rabbit.exchange}")
    private String directExchange;

    @Value("${alexstrasza.routing.inventory.auctionProcessing}")
    private String auctionInventoryProcessing;

    @Value("${alexstrasza.routing.currency.auctionProcessing}")
    private String auctionCurrencyProcessing;

    @Value("${alexstrasza.routing.webapi.bids}")
    private String webapiRouting;

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void SendCurrencyUpdate(WinLossObject currencyData, String creator)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try
        {
            json = objectMapper.writeValueAsString(currencyData);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        MessageProperties properties = new MessageProperties();
        properties.setHeader("creator", creator);
        Message message = new Message(json.getBytes(), properties);
        rabbitTemplate.convertAndSend(directExchange, auctionCurrencyProcessing, message);
    }

    public void SendInventoryUpdate(ItemBase inventoryData, String winner)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try
        {
            json = objectMapper.writeValueAsString(inventoryData);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }

        MessageProperties properties = new MessageProperties();
        properties.setHeader("userId", winner);
//        properties.setHeader("changeType", change);
        Message message = new Message(json.getBytes(), properties);
        rabbitTemplate.convertAndSend(directExchange, auctionInventoryProcessing, message);
    }

    public void NotifyBids(Bid bid, String auctionId)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";

        try
        {
            json = objectMapper.writeValueAsString(bid);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }

        MessageProperties properties = new MessageProperties();
        properties.setHeader("auctionId", auctionId);
        Message message = new Message(json.getBytes(), properties);
        rabbitTemplate.convertAndSend(directExchange, webapiRouting, message);
    }
}
