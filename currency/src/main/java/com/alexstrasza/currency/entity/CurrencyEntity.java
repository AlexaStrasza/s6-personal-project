package com.alexstrasza.currency.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "currency", uniqueConstraints=@UniqueConstraint(columnNames="user"))
public class CurrencyEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user")
    public UsersEntity user;

    public int ownedCurrency;
    public int floatingCurrency;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<InvestmentEntity> investedAuctions = new ArrayList<>();

    public CurrencyEntity()
    {
    }

    public CurrencyEntity(UsersEntity user)
    {
        this.user = user;
        ownedCurrency = 0;
    }

    public CurrencyEntity(UsersEntity user, int amount)
    {
        this.user = user;
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
