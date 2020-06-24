package com.alexstrasza.inventory.dao;

import com.alexstrasza.inventory.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersDao extends JpaRepository<UsersEntity, Long>
{
    UsersEntity findByUsername(String username);
}
