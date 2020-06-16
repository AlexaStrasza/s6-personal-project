package io.mitch.authorizationserver.components;

import Messages.UserModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMessager
{
    @Value("${alexstrasza.rabbit.exchange}")
    private String directExchange;

    @Value("${alexstrasza.routing.auction.userCreate}")
    private String auctionUserCreate;

    @Value("${alexstrasza.routing.currency.userCreate}")
    private String currencyUserCreate;

    @Value("${alexstrasza.routing.inventory.userCreate}")
    private String inventoryUserCreate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void NotifyUserCreation(UserModel item)
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
//        properties.setHeader("userId", user);
//        properties.setHeader("changeType", change);
//        properties.setHeader("floating", floating);
        Message message = new Message(json.getBytes(), properties);
//        rabbitTemplate.convertAndSend(directExchange, auctionUserCreate, message);
        rabbitTemplate.convertAndSend(directExchange, currencyUserCreate, message);
//        rabbitTemplate.convertAndSend(directExchange, inventoryUserCreate, message);
    }
}
