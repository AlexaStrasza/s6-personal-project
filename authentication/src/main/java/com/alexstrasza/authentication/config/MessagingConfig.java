package com.alexstrasza.authentication.config;

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

    @Value("${alexstrasza.queue.currency.userCreate}")
    private String currencyUserCreationQueue;

    @Value("${alexstrasza.routing.currency.userCreate}")
    private String currencyUserCreationRouting;

    @Value("${alexstrasza.queue.auction.userCreate}")
    private String auctionUserCreationQueue;

    @Value("${alexstrasza.routing.auction.userCreate}")
    private String auctionUserCreationRouting;

    @Value("${alexstrasza.queue.inventory.userCreate}")
    private String inventoryUserCreationQueue;

    @Value("${alexstrasza.routing.inventory.userCreate}")
    private String inventoryUserCreationRouting;

    @Bean
    public DirectExchange directExchange()
    {
        return new DirectExchange(directExchange, false, false);
    }

    @Bean
    public Queue currencyUserCreationQueue()
    {
        return new Queue(currencyUserCreationQueue);
    }

    @Bean
    public Binding currencyUserCreationBinding(Queue currencyUserCreationQueue, DirectExchange directExchange)
    {
        return BindingBuilder.bind(currencyUserCreationQueue).to(directExchange).with(currencyUserCreationRouting);
    }

    @Bean
    public Queue auctionUserCreationQueue()
    {
        return new Queue(auctionUserCreationQueue);
    }

    @Bean
    public Binding auctionUserCreationBinding(Queue auctionUserCreationQueue, DirectExchange directExchange)
    {
        return BindingBuilder.bind(auctionUserCreationQueue).to(directExchange).with(auctionUserCreationRouting);
    }

    @Bean
    public Queue inventoryUserCreationQueue()
    {
        return new Queue(inventoryUserCreationQueue);
    }

    @Bean
    public Binding inventoryUserCreationBinding(Queue inventoryUserCreationQueue, DirectExchange directExchange)
    {
        return BindingBuilder.bind(inventoryUserCreationQueue).to(directExchange).with(inventoryUserCreationRouting);
    }
}
