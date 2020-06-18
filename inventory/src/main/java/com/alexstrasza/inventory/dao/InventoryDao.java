package com.alexstrasza.inventory.dao;

import com.alexstrasza.inventory.entity.InventoryEntity;
import com.alexstrasza.inventory.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryDao extends JpaRepository<InventoryEntity, Long>
{
//    public final static String getInventoryByUser = "SELECT * FROM inventory WHERE user = :username";
////
//    @Query(value = getInventoryByUser, nativeQuery = true)
    InventoryEntity findByUser(UsersEntity user);
}
