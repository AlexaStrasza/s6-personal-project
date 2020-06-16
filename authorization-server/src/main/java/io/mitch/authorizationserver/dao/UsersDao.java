package io.mitch.authorizationserver.dao;

import io.mitch.authorizationserver.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersDao extends JpaRepository<UsersEntity,Long> {

    UsersEntity findByUsername(String username);

}
