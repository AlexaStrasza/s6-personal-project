package com.alexstrasza.currency.receiver;

import com.alexstrasza.currency.components.CurrencyHolder;
import com.alexstrasza.currency.dao.CurrencyDao;
import com.alexstrasza.currency.dao.UsersDao;
import com.alexstrasza.currency.entity.CurrencyEntity;
import com.alexstrasza.currency.entity.UsersEntity;
import com.alexstrasza.currency.models.UserModel;
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

    @Autowired
    private UsersDao userDao;

    @Autowired
    private CurrencyDao currencyDao;

    @RabbitListener(queues = "${alexstrasza.queue.currency.userCreate}")
    public void createUser(Message message) // JSON
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
        UsersEntity entity = new UsersEntity(user.getUsername());
        userDao.save(entity);
        CurrencyEntity currencyEntity = new CurrencyEntity(entity, 10000);
        currencyDao.save(currencyEntity);
//        currency.CreateNewPlayer(entity.getUsername(), 10000); // Giving new players 10k for testing
    }

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