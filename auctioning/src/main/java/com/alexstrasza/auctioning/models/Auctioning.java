package com.alexstrasza.auctioning.models;
import org.apache.tomcat.util.digester.ArrayStack;

import java.util.List;

public class Auctioning
{
    private static Auctioning instance;

    public List<Auction> indexedAuctions = new ArrayStack<>();

    private Auctioning()
    {

    }

    public static Auctioning GetInstance()
    {
        if (instance == null)
        {
            instance = new Auctioning();
        }

        return instance;
    }

    public void AddAuction(Auction auction)
    {
        indexedAuctions.add(auction);
    }
}
