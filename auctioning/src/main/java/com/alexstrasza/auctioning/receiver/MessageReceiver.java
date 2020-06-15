package com.alexstrasza.auctioning.receiver;

import com.alexstrasza.auctioning.components.Auctioning;
import com.alexstrasza.auctioning.models.AuctionCreationObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MessageReceiver {

    @Autowired
    private Auctioning auctioning;

    @RabbitListener(queues = "${alexstrasza.queue.auction.auctionCreation}")
    public void auctionCreationDataReceiver(Message message) throws JsonProcessingException // JSON
    {
        String userId = message.getMessageProperties().getHeader("userId").toString();
        ObjectMapper objectMapper = new ObjectMapper();
        AuctionCreationObject obj = objectMapper.readValue(new String(message.getBody(), StandardCharsets.UTF_8), AuctionCreationObject.class);

        auctioning.StartAuction(obj, userId);
    }
}