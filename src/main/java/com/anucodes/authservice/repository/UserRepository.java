package com.anucodes.authservice.repository;

import com.anucodes.authservice.entity.UserInfo;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {

    public UserInfo findByUsername(String username);
}
