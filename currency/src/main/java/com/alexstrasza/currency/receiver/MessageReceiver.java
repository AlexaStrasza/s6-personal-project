package com.alexstrasza.currency.receiver;

import com.alexstrasza.currency.components.CurrencyHolder;
import com.alexstrasza.currency.models.WinLossObject;
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
    CurrencyHolder currency;

    @RabbitListener(queues = "${alexstrasza.queue.currency.auctionProcessing}")
    public void currencyDataReceiver(Message message) throws JsonProcessingException // JSON
    {
        String creator = message.getMessageProperties().getHeader("creator").toString();
        ObjectMapper objectMapper = new ObjectMapper();
        WinLossObject item = objectMapper.readValue(new String(message.getBody(), StandardCharsets.UTF_8), WinLossObject.class);

        if (!item.winner.equals(""))
        {
            currency.PayAuction(item.winner, item.auctionId, creator);
        }
        for (String user : item.losers)
        {
            currency.Withdraw(user, item.auctionId);
        }
    }
}