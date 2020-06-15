package com.alexstrasza.webapi.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig
{
    @Value("${alexstrasza.rabbit.exchange}")
    private String directExchange;

    @Value("${alexstrasza.queue.webapi.inventoryChange}")
    private String webapiQueueInventory;

    @Value("${alexstrasza.routing.webapi.inventoryChange}")
    private String webapiRoutingInventory;

    @Value("${alexstrasza.queue.webapi.currencyChange}")
    private String webapiQueueCurrency;

    @Value("${alexstrasza.routing.webapi.currencyChange}")
    private String webapiRoutingCurrency;

    @Value("${alexstrasza.queue.webapi.bids}")
    private String webapiQueueBids;

    @Value("${alexstrasza.routing.webapi.bids}")
    private String webapiRoutingBids;

    @Bean
    public DirectExchange directExchange()
    {
        return new DirectExchange(directExchange, false, false);
    }

    @Bean
    public Queue webapiQueueInventory()
    {
        return new Queue(webapiQueueInventory);
    }

    @Bean
    public Binding webapiBindingInventory(Queue webapiQueueInventory, DirectExchange directExchange)
    {
        return BindingBuilder.bind(webapiQueueInventory).to(directExchange).with(webapiRoutingInventory);
    }

    @Bean
    public Queue webapiQueueCurrency()
    {
        return new Queue(webapiQueueCurrency);
    }

    @Bean
    public Binding webapiBindingCurrency(Queue webapiQueueCurrency, DirectExchange directExchange)
    {
        return BindingBuilder.bind(webapiQueueCurrency).to(directExchange).with(webapiRoutingCurrency);
    }

    @Bean
    public Queue webapiQueueBids()
    {
        return new Queue(webapiQueueBids);
    }

    @Bean
    public Binding webapiBindingBids(Queue webapiQueueBids, DirectExchange directExchange)
    {
        return BindingBuilder.bind(webapiQueueBids).to(directExchange).with(webapiRoutingBids);
    }
}