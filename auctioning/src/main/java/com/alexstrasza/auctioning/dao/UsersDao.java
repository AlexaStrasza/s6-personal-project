package com.alexstrasza.auctioning.dao;

import com.alexstrasza.auctioning.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersDao extends JpaRepository<UsersEntity,Long> {

    UsersEntity findByUsername(String username);

}
