package com.alexstrasza.currency.models;

import com.alexstrasza.currency.components.RabbitMessager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class PlayerCurrency
{
    public String userId;
    public int ownedCurrency;
    public Map<String, Integer> investedAuctions = new HashMap<String, Integer>();

    public PlayerCurrency()
    {

    }

    public PlayerCurrency(String user, int amount)
    {
        userId = user;
        ownedCurrency = amount;
    }

    public void ChangeCurrency(int amount)
    {
        ownedCurrency += amount;
    }
}
