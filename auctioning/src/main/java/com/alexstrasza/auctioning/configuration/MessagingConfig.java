package com.alexstrasza.auctioning.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig
{
    @Value("${alexstrasza.rabbit.exchange}")
    private String directExchange;

    @Value("${alexstrasza.queue.auction.auctionCreation}")
    private String auctionCreationQueue;

    @Value("${alexstrasza.routing.auction.auctionCreation}")
    private String auctionCreationRouting;

    @Value("${alexstrasza.queue.auction.userCreate}")
    private String userCreationQueue;

    @Value("${alexstrasza.routing.auction.userCreate}")
    private String userCreationRouting;

    @Value("${alexstrasza.queue.auction.bids}")
    private String bidQueue;

    @Value("${alexstrasza.routing.auction.bids}")
    private String bidRouting;

//    @Value("${alexstrasza.queue.auction.buyout}")
//    private String buyoutQueue;
//
//    @Value("${alexstrasza.routing.auction.buyout}")
//    private String buyoutRouting;

    @Bean
    public DirectExchange directExchange()
    {
        return new DirectExchange(directExchange, false, false);
    }

    @Bean
    public Queue auctionCreationQueue()
    {
        return new Queue(auctionCreationQueue);
    }

    @Bean
    public Queue userCreationQueue()
    {
        return new Queue(userCreationQueue);
    }

    @Bean
    public Queue bidQueue()
    {
        return new Queue(bidQueue);
    }

//    @Bean
//    public Queue buyoutQueue()
//    {
//        return new Queue(buyoutQueue);
//    }

    @Bean
    public Binding auctionCreationBinding(Queue auctionCreationQueue, DirectExchange directExchange)
    {
        return BindingBuilder.bind(auctionCreationQueue).to(directExchange).with(auctionCreationRouting);
    }

    @Bean
    public Binding userCreationBinding(Queue userCreationQueue, DirectExchange directExchange)
    {
        return BindingBuilder.bind(userCreationQueue).to(directExchange).with(userCreationRouting);
    }

    @Bean
    public Binding bidBinding(Queue bidQueue, DirectExchange directExchange)
    {
        return BindingBuilder.bind(bidQueue).to(directExchange).with(bidRouting);
    }

//    @Bean
//    public Binding buyoutBinding(Queue buyoutQueue, DirectExchange directExchange)
//    {
//        return BindingBuilder.bind(buyoutQueue).to(directExchange).with(buyoutRouting);
//    }
}
