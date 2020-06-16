package com.alexstrasza.currency.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "currency", uniqueConstraints=@UniqueConstraint(columnNames="username"))
public class CurrencyEntity
{
    public UsersEntity getUsername()
    {
        return username;
    }

    public void setUsername(UsersEntity user)
    {
        this.username = user;
    }

    public int getOwnedCurrency()
    {
        return ownedCurrency;
    }

    public void setOwnedCurrency(int ownedCurrency)
    {
        this.ownedCurrency = ownedCurrency;
    }

    public int getFloatingCurrency()
    {
        return floatingCurrency;
    }

    public void setFloatingCurrency(int floatingCurrency)
    {
        this.floatingCurrency = floatingCurrency;
    }

    public List<InvestmentEntity> getInvestedAuctions()
    {
        return investedAuctions;
    }

    public void setInvestedAuctions(List<InvestmentEntity> investedAuctions)
    {
        this.investedAuctions = investedAuctions;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @OneToOne
    @JoinColumn(name = "username")
    public UsersEntity username;

    public int ownedCurrency;
    public int floatingCurrency;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true )
    public List<InvestmentEntity> investedAuctions = new ArrayList<>();

    public CurrencyEntity()
    {
    }

    public CurrencyEntity(UsersEntity user)
    {
        this.username = user;
        ownedCurrency = 0;
    }

    public CurrencyEntity(UsersEntity user, int amount)
    {
        this.username = user;
        ownedCurrency = amount;
    }

    public InvestmentEntity getInvestedAuction(String auctionId)
    {
        for (InvestmentEntity entity : investedAuctions)
        {
            if (entity.auctionId.equals(auctionId)) return entity;
        }
        return null;
    }
}
