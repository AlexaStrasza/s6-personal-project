package com.alexstrasza.auctioning.dao;

import com.alexstrasza.auctioning.models.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AuctionDao extends JpaRepository<Auction,Long>
{
    Auction findByAuctionId(String id);

    List<Auction> findAllByAuctionEndedIsFalse();
}
