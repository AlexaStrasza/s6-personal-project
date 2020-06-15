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
    public Binding auctionCreationBinding(Queue auctionCreationQueue, DirectExchange directExchange)
    {
        return BindingBuilder.bind(auctionCreationQueue).to(directExchange).with(auctionCreationRouting);
    }
}
