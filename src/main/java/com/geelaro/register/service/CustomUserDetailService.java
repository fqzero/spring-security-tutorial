package com.geelaro.register.service;

import com.geelaro.register.domain.entity.Role;
import com.geelaro.register.domain.entity.User;
import com.geelaro.register.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final static Logger logger = LogManager.getLogger(CustomUserDetailService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);

        if (user==null){
            logger.error("Not Found name "+ name);
            throw new UsernameNotFoundException("Not Found name "+ name);

        }

        return new org.springframework.security.core.userdetails.User(
            user.getName(),
                user.getPassWd(),
                getAuthorities(user.getRoles()));
    }
    private static List<GrantedAuthority> getAuthorities (List<Role> roles) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role:roles){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
