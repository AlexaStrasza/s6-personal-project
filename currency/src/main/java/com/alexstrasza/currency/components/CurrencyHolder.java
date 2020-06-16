package com.alexstrasza.currency.components;

import com.alexstrasza.currency.dao.CurrencyDao;
import com.alexstrasza.currency.entity.CurrencyEntity;
import com.alexstrasza.currency.entity.InvestmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyHolder
{
    @Autowired
    RabbitMessager messager;

    @Autowired
    private CurrencyDao currencyDao;

    public void ChangeCurrency(String user, int amount)
    {
        CurrencyEntity entity = currencyDao.getCurrencyObjByUser(user);
        System.out.println(entity);
        if (entity != null)
        {
            entity.ownedCurrency += amount;
            currencyDao.save(entity);
            if (amount > 0)
            messager.SendCurrencyUpdate(amount, user, "add", "false");
            else
            messager.SendCurrencyUpdate(amount, user, "remove", "false");
        }
    }

    public String Withdraw(String user, String auctionId)
    {
        CurrencyEntity entity = currencyDao.getCurrencyObjByUser(user);
        if (entity == null) return "User not found.";
        InvestmentEntity investment = entity.getInvestedAuction(auctionId);
        if (investment == null) return "Error withdrawing offer. User has not bid on given auction.";

        int regainedCurrency = investment.invested;
        entity.ownedCurrency += regainedCurrency;
        entity.floatingCurrency -= regainedCurrency;
        entity.investedAuctions.remove(investment);

        currencyDao.save(entity);

        messager.SendCurrencyUpdate(regainedCurrency, user, "add", "false");
        messager.SendCurrencyUpdate(regainedCurrency, user, "remove", "true");

        return "Offer withdrawn";
    }

    public String PayAuction(String user, String auctionId, String payTo)
    {
        CurrencyEntity entity = currencyDao.getCurrencyObjByUser(user);
        CurrencyEntity entityPayTo = currencyDao.getCurrencyObjByUser(payTo);
        if (entity == null || entityPayTo == null) return "User not found.";
        InvestmentEntity investment = entity.getInvestedAuction(auctionId);
        if (investment == null) return "Error paying auction. User has not bid on given auction.";

        int currencyChange = investment.invested;
        entity.floatingCurrency -= currencyChange;
        entityPayTo.ownedCurrency += currencyChange;
        entity.investedAuctions.remove(investment);

        currencyDao.save(entity);
        currencyDao.save(entityPayTo);

        messager.SendCurrencyUpdate(currencyChange, user, "remove", "true");
        messager.SendCurrencyUpdate(currencyChange, payTo, "add", "false");
        return "Auction paid";
    }

    public int Bid(String user, String auctionId, int amount)
    {

        CurrencyEntity entity = currencyDao.getCurrencyObjByUser(user);
        if (entity == null) return -1; // User not found
        // When raising an existing bid
        InvestmentEntity investment = entity.getInvestedAuction(auctionId);
        if (investment == null) // If null user has not bid on auction before
        {
            if (entity.ownedCurrency >= amount)
            {
                investment = new InvestmentEntity(auctionId, amount);
                entity.investedAuctions.add(investment);
                entity.ownedCurrency -= amount;
                entity.floatingCurrency += amount;
                currencyDao.save(entity);

                messager.SendCurrencyUpdate(amount, user, "remove", "false");
                messager.SendCurrencyUpdate(amount, user, "add", "true");

                return 1; // Successfully placed bid
            }
            else return 0; // Not enough currency
        }
        else
        {
            if (investment.invested > amount) return 2; // Already bidding more
            int spending = amount - investment.invested;
            if (entity.ownedCurrency >= spending)
            {
                investment.invested = amount;

                entity.ownedCurrency -= spending;
                entity.floatingCurrency += spending;

                currencyDao.save(entity);

                messager.SendCurrencyUpdate(spending, user, "remove", "false");
                messager.SendCurrencyUpdate(spending, user, "add", "true");
                return 1; // Successfully placed bid
            }
            else return 0; // Not enough currency
        }
    }

    public int GetCurrency(String user)
    {
        CurrencyEntity entity = currencyDao.getCurrencyObjByUser(user);
        return entity.ownedCurrency;
    }

    public int GetFloatingCurrency(String user)
    {
        CurrencyEntity entity = currencyDao.getCurrencyObjByUser(user);
        return entity.floatingCurrency;
    }

    public void CreatePlayerForTesting()
    {
        System.out.println("Creating some player data");
//        CreateNewPlayer("User 1", 10000);
//        CreateNewPlayer("User 2", 9000);
//        CreateNewPlayer("User 3", 8000);
    }
}
