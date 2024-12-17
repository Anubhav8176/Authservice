package com.anucodes.authservice.controllers;


import com.anucodes.authservice.entity.RefreshToken;
import com.anucodes.authservice.model.UserInfoDto;
import com.anucodes.authservice.request.AuthResponseDTO;
import com.anucodes.authservice.request.RefreshTokenRequestDTO;
import com.anucodes.authservice.response.JwtResponse;
import com.anucodes.authservice.services.JwtService;
import com.anucodes.authservice.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("auth/login/")
    public ResponseEntity Login(@RequestBody AuthResponseDTO authResponseDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authResponseDTO.getUsername(), authResponseDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authResponseDTO.getUsername());
            String token = jwtService.generateToken(authResponseDTO.getUsername());
            return new ResponseEntity(
                    JwtResponse.builder()
                            .accessToken(token)
                            .token(refreshToken.getToken())
                            .build(), HttpStatus.OK
            );
        }else{
            return new ResponseEntity("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("auth/refreshToken")
    public JwtResponse refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findUserByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    return JwtResponse.builder()
                            .accessToken(jwtService.generateToken(userInfo.getUsername()))
                            .token(refreshTokenRequestDTO.getToken())
                            .build();
                })
                .orElseThrow(()->new RuntimeException("RefreshToken not found in database!!"));
    }
}
