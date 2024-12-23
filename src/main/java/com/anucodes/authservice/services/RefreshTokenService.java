package com.anucodes.authservice.services;


import com.anucodes.authservice.entity.RefreshToken;
import com.anucodes.authservice.entity.UserInfo;
import com.anucodes.authservice.repository.RefreshTokenRepository;
import com.anucodes.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(String username){

        UserInfo userInfo = userRepository.findByUsername(username);

        RefreshToken refreshToken = RefreshToken
                .builder()
                .userInfo(userInfo)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(6000))
                .build();

        RefreshToken tokenToReturn = refreshTokenRepository.save(refreshToken);

        return tokenToReturn;
    }

    public Optional<RefreshToken> findUserByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken){
        if (refreshToken.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken()+ "The token is expired. Login Again!!");
        }

        return refreshToken;
    }
}
