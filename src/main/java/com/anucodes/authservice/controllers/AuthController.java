package com.anucodes.authservice.controllers;

import com.anucodes.authservice.entity.RefreshToken;
import com.anucodes.authservice.model.UserInfoDto;
import com.anucodes.authservice.response.JwtResponse;
import com.anucodes.authservice.services.JwtService;
import com.anucodes.authservice.services.RefreshTokenService;
import com.anucodes.authservice.services.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("auth/signup")
    public ResponseEntity SignUp(UserInfoDto userInfoDto){

        try{
            Boolean isSignedIn = userDetailsService.signUp(userInfoDto);
            if (Boolean.FALSE.equals(isSignedIn)){
                return new ResponseEntity("Already Exist", HttpStatus.BAD_REQUEST);
            }

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.generateToken(userInfoDto.getUsername());
            return new ResponseEntity(
                    JwtResponse
                            .builder()
                            .accessToken(jwtToken)
                            .token(refreshToken.getToken().toString())
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
