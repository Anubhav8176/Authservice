package com.anucodes.authservice.services;

import com.anucodes.authservice.entity.UserInfo;
import com.anucodes.authservice.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


//CustomUserDetails class simply takes the userInfo object and takes the information from the table.
public class CustomUserDetails extends UserInfo implements UserDetails {

    private String username;
    private String password;
    Collection<? extends GrantedAuthority> authorities;


    public CustomUserDetails(UserInfo userInfo){
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();

        for(UserRole roles: userInfo.getRoles()){
            auths.add(new SimpleGrantedAuthority(roles.getName().toUpperCase()));
        }

        this.authorities = auths;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public String getPassword(){
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}