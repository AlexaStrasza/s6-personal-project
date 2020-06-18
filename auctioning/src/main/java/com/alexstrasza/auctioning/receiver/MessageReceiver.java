package com.alexstrasza.auctioning.receiver;

import com.alexstrasza.auctioning.components.AuctionManager;
import com.alexstrasza.auctioning.components.RabbitMessager;
import com.alexstrasza.auctioning.dao.UsersDao;
import com.alexstrasza.auctioning.entity.UsersEntity;
import com.alexstrasza.auctioning.models.*;
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
    private AuctionManager auctioning;

    @Autowired
    RabbitMessager rabbitMessager;

    @Autowired
    private UsersDao userDao;

    @RabbitListener(queues = "${alexstrasza.queue.auction.userCreate}")
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
        UsersEntity test = userDao.findByUsername(user.getUsername());
        if (test == null)
        {
            UsersEntity entity = new UsersEntity(user.getUsername());
            userDao.save(entity);


            AuctionCreationObject obj = new AuctionCreationObject();
            obj.amount = 10;
            obj.pricePerUnit = 10;
            obj.allowBuyout = false;
            obj.itemId = 0;
            FilterOptions options = new FilterOptions();
            options.buyout = false;
            options.name = "Charm of the berserker";
            options.rarity = "Rare";
            options.type = "Trinket";
            obj.filterOptions = options;
            auctioning.StartAuction(obj, entity.getUsername());
        }
    }

    @RabbitListener(queues = "${alexstrasza.queue.auction.auctionCreation}")
    public void auctionCreationDataReceiver(Message message) throws JsonProcessingException // JSON
    {
        String userId = message.getMessageProperties().getHeader("userId").toString();
        ObjectMapper objectMapper = new ObjectMapper();
        AuctionCreationObject obj = objectMapper.readValue(new String(message.getBody(), StandardCharsets.UTF_8), AuctionCreationObject.class);

        auctioning.StartAuction(obj, userId);
    }

    @RabbitListener(queues = "${alexstrasza.queue.auction.bids}")
    public void bidPlacement(Message message) throws JsonProcessingException
    {
        String auctionId = message.getMessageProperties().getHeader("auctionId").toString();
        String user = message.getMessageProperties().getHeader("user").toString();

        ObjectMapper objectMapper = new ObjectMapper();
        DataContainer data = null;
        data = objectMapper.readValue(new String(message.getBody(), StandardCharsets.UTF_8), DataContainer.class);

        Bid bid = auctioning.PlaceBid(data.heldInt, auctionId, user);

        // Notify other subscribed users of new bid
        rabbitMessager.NotifyBids(bid, auctionId, "false");
    }

//    @RabbitListener(queues = "${alexstrasza.queue.auction.buyout}")
//    public void buyoutPlacement(Message message) throws JsonProcessingException
//    {
//        String auctionId = message.getMessageProperties().getHeader("auctionId").toString();
//        String user = message.getMessageProperties().getHeader("user").toString();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        DataContainer data = null;
//        data = objectMapper.readValue(new String(message.getBody(), StandardCharsets.UTF_8), DataContainer.class);
//
//        Bid bid = auctioning.PlaceBuyout(auctionId, user);
//
//        // Notify other subscribed users of new bid
//        rabbitMessager.NotifyBids(bid, auctionId, "true");
//    }
}