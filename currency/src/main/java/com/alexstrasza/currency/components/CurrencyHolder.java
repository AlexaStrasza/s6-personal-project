package com.alexstrasza.currency.components;

import com.alexstrasza.currency.models.PlayerCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CurrencyHolder
{
    @Autowired
    RabbitMessager messager;

    List<PlayerCurrency> players = new ArrayList<PlayerCurrency>();

    public void CreateNewPlayer(String user, int amount)
    {
        players.add(new PlayerCurrency(user, amount));
    }

    public void ChangeCurrency(String user, int amount)
    {
        for (PlayerCurrency player: players)
        {
            if (player.userId.equals(user))
            {
                player.ChangeCurrency(amount);
                if (amount > 0)
                messager.SendCurrencyUpdate(amount, player.userId, "add", "false");
                else
                messager.SendCurrencyUpdate(amount, player.userId, "remove", "false");
            }
        }
    }

    public String Withdraw(String user, String auctionId)
    {
        for (PlayerCurrency player: players)
        {
            if (player.userId.equals(user))
            {
                if (player.investedAuctions.containsKey(auctionId))
                {
                    int regainedCurrency = player.investedAuctions.get(auctionId);
                    player.ownedCurrency += regainedCurrency;
                    player.investedAuctions.remove(auctionId);

                    messager.SendCurrencyUpdate(regainedCurrency, player.userId, "add", "false");
                    messager.SendCurrencyUpdate(regainedCurrency, player.userId, "remove", "true");

                    return "Offer withdrawn";
                }
                return "Error withdrawing offer. User has not bid on given auction.";
            }
        }
        return "User not found.";
    }

    public String PayAuction(String user, String auctionId, String payTo)
    {
        for (PlayerCurrency player: players)
        {
            if (player.userId.equals(user))
            {
                if (player.investedAuctions.containsKey(auctionId))
                {
                    int spentCurrency = player.investedAuctions.get(auctionId);
                    player.investedAuctions.remove(auctionId);
                    messager.SendCurrencyUpdate(spentCurrency, player.userId, "remove", "true");
                    for (PlayerCurrency playerToPayTo : players)
                    {
                        if (playerToPayTo.userId.equals(payTo))
                        {
                            playerToPayTo.ChangeCurrency(spentCurrency);
                            messager.SendCurrencyUpdate(spentCurrency, playerToPayTo.userId, "add", "false");
                        }
                    }
                    return "Auction paid";
                }
                return "Error paying auction. User has not bid on given auction.";
            }
        }
        return "User not found.";
    }



    public int Bid(String user, String auctionId, int amount)
    {
        for (PlayerCurrency player: players)
        {
            if (player.userId.equals(user))
            {
                // When raising an existing bid
                if (player.investedAuctions.containsKey(auctionId))
                {
                    if (amount > player.investedAuctions.get(auctionId))
                    {
                        if (player.ownedCurrency >= (amount - player.investedAuctions.get(auctionId)))
                        {
                            int spending = amount - player.investedAuctions.get(auctionId);
                            player.ownedCurrency -= (amount - player.investedAuctions.get(auctionId));
                            player.investedAuctions.replace(auctionId, amount);

                            messager.SendCurrencyUpdate(spending, player.userId, "remove", "false");
                            messager.SendCurrencyUpdate(spending, player.userId, "add", "true");

                            return 1; // Successfully placed bid
                        }
                        else return 0; // Not enough currency
                    }
                    else return 2; // Already higher bid placed
                }
                // When placing a new bid
                else
                {
                    if (player.ownedCurrency >= amount)
                    {
                        player.ownedCurrency -= amount;
                        player.investedAuctions.put(auctionId, amount);
                        messager.SendCurrencyUpdate(amount, player.userId, "remove", "false");
                        messager.SendCurrencyUpdate(amount, player.userId, "add", "true");

                        return 1; // Successfully placed bid
                    }
                    else return 0; // Not enough currency
                }
            }
        }
        return -1;
    }

    public int GetCurrency(String user)
    {
        for (PlayerCurrency player: players)
        {
            if (player.userId.equals(user))
            {
                return player.ownedCurrency;
            }
        }
        return -1;
    }

    public int GetFloatingCurrency(String user)
    {
        for (PlayerCurrency player: players)
        {
            if (player.userId.equals(user))
            {
                int i = 0;
                for (int value : player.investedAuctions.values())
                {
                    i += value;
                }
                return i;
            }
        }
        return -1;
    }

    public void CreatePlayerForTesting()
    {
        System.out.println("Creating some player data");
        CreateNewPlayer("User 1", 10000);
        CreateNewPlayer("User 2", 9000);
        CreateNewPlayer("User 3", 8000);
    }
}
