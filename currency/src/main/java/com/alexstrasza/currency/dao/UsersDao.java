package com.alexstrasza.currency.dao;

import com.alexstrasza.currency.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersDao extends JpaRepository<UsersEntity, Long>
{
    UsersEntity findByUsername(String username);
}
