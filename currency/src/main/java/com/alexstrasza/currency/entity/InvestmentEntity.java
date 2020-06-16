package com.alexstrasza.currency.entity;

import javax.persistence.*;

@Entity
@Table (name = "investments")
public class InvestmentEntity
{
    public InvestmentEntity()
    {
    }

    public InvestmentEntity(String auctionId, int investment)
    {
        this.auctionId = auctionId;
        invested = investment;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String auctionId;
    public int invested;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getAuctionId()
    {
        return auctionId;
    }

    public void setAuctionId(String auctionId)
    {
        this.auctionId = auctionId;
    }

    public int getInvested()
    {
        return invested;
    }

    public void setInvested(int invested)
    {
        this.invested = invested;
    }
}
