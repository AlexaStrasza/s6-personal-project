package com.alexstrasza.authentication.dao;

import com.alexstrasza.authentication.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersDao extends JpaRepository<UsersEntity,Long> {

    UsersEntity findByUsername(String username);

}
