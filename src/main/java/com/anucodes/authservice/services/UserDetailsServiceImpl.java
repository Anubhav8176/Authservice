package com.anucodes.authservice.services;

import com.anucodes.authservice.entity.RefreshToken;
import com.anucodes.authservice.entity.UserInfo;
import com.anucodes.authservice.model.UserInfoDto;
import com.anucodes.authservice.producer.AuthProducer;
import com.anucodes.authservice.repository.RefreshTokenRepository;
import com.anucodes.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@Data
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthProducer authProducer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);

        if (user==null){
            throw new UsernameNotFoundException("Invalid username!!");
        }
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserExist(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public boolean signUp(UserInfoDto userInfoDto){
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if (Objects.nonNull(checkIfUserExist(userInfoDto))){
            return false;
        }
        String userId = UUID.randomUUID().toString();
        UserInfo newUser = new UserInfo(
                userId,
                userInfoDto.getUsername(),
                userInfoDto.getPassword(),
                new HashSet<>()
        );

        userRepository.save(newUser);
        authProducer.sendEventToKafka(userInfoDto);

        return true;
    }
}
