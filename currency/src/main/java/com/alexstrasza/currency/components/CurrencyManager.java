package com.alexstrasza.currency.components;

import com.alexstrasza.currency.dao.CurrencyDao;
import com.alexstrasza.currency.dao.UsersDao;
import com.alexstrasza.currency.entity.CurrencyEntity;
import com.alexstrasza.currency.entity.InvestmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CurrencyManager
{
    @Autowired
    RabbitMessenger messenger;

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private UsersDao userDao;

    public void ChangeCurrency(String user, int amount)
    {
        CurrencyEntity entity = GetCurrencyObj(user);
        System.out.println(entity);
        if (entity != null)
        {
            entity.ownedCurrency += amount;
            currencyDao.save(entity);
            if (amount > 0)
            messenger.SendCurrencyUpdate(amount, user, "add", "false");
            else
            messenger.SendCurrencyUpdate(amount, user, "remove", "false");
        }
    }

    private CurrencyEntity GetCurrencyObj(String user)
    {
         return currencyDao.findByUser(userDao.findByUsername(user));
    }

    public String Withdraw(String user, String auctionId)
    {
        CurrencyEntity entity = GetCurrencyObj(user);
        if (entity == null) return "User not found.";
        InvestmentEntity investment = entity.getInvestedAuction(auctionId);
        if (investment == null) return "Error withdrawing offer. User has not bid on given auction.";

        int regainedCurrency = investment.invested;
        entity.ownedCurrency += regainedCurrency;
        entity.floatingCurrency -= regainedCurrency;
        entity.investedAuctions.remove(investment);

        currencyDao.save(entity);

        messenger.SendCurrencyUpdate(regainedCurrency, user, "add", "false");
        messenger.SendCurrencyUpdate(regainedCurrency, user, "remove", "true");

        return "Offer withdrawn";
    }

    public String PayAuction(String user, String auctionId, String payTo)
    {
        CurrencyEntity entity = GetCurrencyObj(user);
        CurrencyEntity entityPayTo = GetCurrencyObj(payTo);
        if (entity == null || entityPayTo == null) return "User not found.";
        InvestmentEntity investment = entity.getInvestedAuction(auctionId);
        if (investment == null) return "Error paying auction. User has not bid on given auction.";

        int currencyChange = investment.invested;
        entity.floatingCurrency -= currencyChange;
        entityPayTo.ownedCurrency += currencyChange;
        entity.investedAuctions.remove(investment);

        currencyDao.save(entity);
        currencyDao.save(entityPayTo);

        messenger.SendCurrencyUpdate(currencyChange, user, "remove", "true");
        messenger.SendCurrencyUpdate(currencyChange, payTo, "add", "false");
        return "Auction paid";
    }

    public int Bid(int amount, String user, String auctionId)
    {

        CurrencyEntity entity = GetCurrencyObj(user);
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

                messenger.SendCurrencyUpdate(amount, user, "remove", "false");
                messenger.SendCurrencyUpdate(amount, user, "add", "true");

                return 1; // Successfully placed bid
            }
            else return 0; // Not enough currency
        }
        else
        {
            if (investment.invested >= amount) return 2; // Already bidding more
            int spending = amount - investment.invested;
            if (entity.ownedCurrency >= spending)
            {
                investment.invested = amount;

                entity.ownedCurrency -= spending;
                entity.floatingCurrency += spending;

                currencyDao.save(entity);

                messenger.SendCurrencyUpdate(spending, user, "remove", "false");
                messenger.SendCurrencyUpdate(spending, user, "add", "true");
                return 1; // Successfully placed bid
            }
            else return 0; // Not enough currency
        }
    }

//    public int Buyout(int amount, String user, String auctionId)
//    {
//        CurrencyEntity entity = GetCurrencyObj(user);
//        if (entity == null) return -1; // User not found
//
//
//
//        // When raising an existing bid
//        InvestmentEntity investment = entity.getInvestedAuction(auctionId);
//        if (investment == null) // If null user has not bid on auction before
//        {
//            if (entity.ownedCurrency >= amount)
//            {
//                investment = new InvestmentEntity(auctionId, amount);
//                entity.investedAuctions.add(investment);
//                entity.ownedCurrency -= amount;
//                entity.floatingCurrency += amount;
//                currencyDao.save(entity);
//
//                messager.SendCurrencyUpdate(amount, user, "remove", "false");
//                messager.SendCurrencyUpdate(amount, user, "add", "true");
//
//                return 1; // Successfully placed bid
//            }
//            else return 0; // Not enough currency
//        }
//        else
//        {
//            if (investment.invested > amount) return 2; // Already bidding more
//            int spending = amount - investment.invested;
//            if (entity.ownedCurrency >= spending)
//            {
//                investment.invested = amount;
//
//                entity.ownedCurrency -= spending;
//                entity.floatingCurrency += spending;
//
//                currencyDao.save(entity);
//
//                messager.SendCurrencyUpdate(spending, user, "remove", "false");
//                messager.SendCurrencyUpdate(spending, user, "add", "true");
//                return 1; // Successfully placed bid
//            }
//            else return 0; // Not enough currency
//        }
//    }

    public int GetCurrency(String user)
    {
        CurrencyEntity entity = GetCurrencyObj(user);
        return entity.ownedCurrency;
    }

    public int GetFloatingCurrency(String user)
    {
        CurrencyEntity entity = GetCurrencyObj(user);
        return entity.floatingCurrency;
    }
}
