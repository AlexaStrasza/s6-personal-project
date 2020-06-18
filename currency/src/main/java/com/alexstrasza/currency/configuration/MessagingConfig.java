package com.alexstrasza.currency.configuration;

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

    @Value("${alexstrasza.queue.currency.auctionProcessing}")
    private String auctionProcessingQueue;

    @Value("${alexstrasza.routing.currency.auctionProcessing}")
    private String auctionProcessingRouting;

    @Value("${alexstrasza.queue.currency.userCreate}")
    private String userCreationQueue;

    @Value("${alexstrasza.routing.currency.userCreate}")
    private String userCreationRouting;

    @Value("${alexstrasza.queue.currency.bids}")
    private String bidQueue;

    @Value("${alexstrasza.routing.currency.bids}")
    private String bidRouting;

    @Value("${alexstrasza.queue.currency.buyout}")
    private String buyoutQueue;

    @Value("${alexstrasza.routing.currency.buyout}")
    private String buyoutRouting;

    @Bean
    public DirectExchange directExchange()
    {
        return new DirectExchange(directExchange, false, false);
    }

    @Bean
    public Queue auctionProcessingQueue()
    {
        return new Queue(auctionProcessingQueue);
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

    @Bean
    public Queue buyoutQueue()
    {
        return new Queue(buyoutQueue);
    }


    @Bean
    public Binding auctionProcessingBinding(Queue auctionProcessingQueue, DirectExchange directExchange)
    {
        return BindingBuilder.bind(auctionProcessingQueue).to(directExchange).with(auctionProcessingRouting);
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

    @Bean
    public Binding buyoutBinding(Queue buyoutQueue, DirectExchange directExchange)
    {
        return BindingBuilder.bind(buyoutQueue).to(directExchange).with(buyoutRouting);
    }
}
