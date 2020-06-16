package com.alexstrasza.currency.dao;

import com.alexstrasza.currency.entity.CurrencyEntity;
import com.alexstrasza.currency.entity.InvestmentEntity;
import com.alexstrasza.currency.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CurrencyDao extends JpaRepository<CurrencyEntity, Long>
{
//    public final static String getCurrencyByUser = "SELECT * FROM currency k where k.username in (SELECT u.follows_username FROM Users_follows u where u.users_entity_username = :username) order by date desc";
    public final static String getCurrencyByUser = "SELECT * FROM currency WHERE username = :username";

    @Query(value = getCurrencyByUser, nativeQuery = true)
    CurrencyEntity getCurrencyObjByUser(String username);

//    public final static String getInvestedAuctionByUser = "SELECT ie.* FROM currency.investment_entity ie LEFT JOIN currency.currency_invested_auctions ia ON ie.id = ia.invested_auctions_id LEFT JOIN currency.currency c ON c.id = ia.currency_entity_id WHERE c.username = :username AND ie.auction_id = :auctionId";
//
//    @Query(value = getInvestedAuctionByUser, nativeQuery = true)
//    InvestmentEntity getInvestedAuctionByUser(String username, String auctionId);


//    public final static String createNewUser = "INSERT INTO currency.currency (owned_currency, username) VALUES (:amount, :username)";
//
//    @Query(value = createNewUser, nativeQuery = true)
//    void createUser(int amount, String username);
}
