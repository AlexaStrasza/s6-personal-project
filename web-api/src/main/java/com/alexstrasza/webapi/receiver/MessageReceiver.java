package com.alexstrasza.webapi.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageReceiver {

    @Autowired
    private SimpMessagingTemplate simp;

    @RabbitListener(queues = "${alexstrasza.queue.webapi.inventoryChange}")
    public void notifyInventoryChange(Message message) throws JsonProcessingException // JSON
    {
        String userId = message.getMessageProperties().getHeader("userId").toString();
        String changeType = message.getMessageProperties().getHeader("changeType").toString();
        Map<String, Object> headers = new HashMap<>();
        headers.put("MessageType", "inventoryChange");
        headers.put("changeType", changeType);

        SendMessage(userId, new String(message.getBody(), StandardCharsets.UTF_8), headers);
    }

    @RabbitListener(queues = "${alexstrasza.queue.webapi.currencyChange}")
    public void notifyCurrencyChange(Message message) // JSON
    {
        String userId = message.getMessageProperties().getHeader("userId").toString();
        String changeType = message.getMessageProperties().getHeader("changeType").toString();
        String floating = message.getMessageProperties().getHeader("floating").toString();
        Map<String, Object> headers = new HashMap<>();
        headers.put("MessageType", "currencyChange");
        headers.put("changeType", changeType);
        headers.put("floating", floating);

        SendMessage(userId, new String(message.getBody(), StandardCharsets.UTF_8), headers);
    }

    @RabbitListener(queues = "${alexstrasza.queue.webapi.bids}")
    public void notifyBidUpdate(Message message) // JSON
    {
        Map<String, Object> headers = new HashMap<>();
        headers.put("MessageType", "bidPlacement");
        SendMessage(message.getMessageProperties().getHeader("auctionId").toString(),
                    new String(message.getBody(), StandardCharsets.UTF_8), headers);
    }

    public void SendMessage(String destination, String jsonData, Map<String, Object> headers)
    {
        simp.convertAndSend("/topic/" + destination, jsonData, headers);
    }
}