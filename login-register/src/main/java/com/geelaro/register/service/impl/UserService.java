package com.geelaro.register.service.impl;

import com.geelaro.register.domain.entity.User;
import com.geelaro.register.domain.dto.UserDto;
import com.geelaro.register.repository.RoleRepository;
import com.geelaro.register.repository.UserRepository;
import com.geelaro.register.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;


@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public boolean checkUserByName(String name) {
        User user = findByName(name);
        return user != null;
    }

    @Override
    public boolean checkUserByEmail(String email) {
        User user = findByEmail(email);
        return user != null;
    }


    /**
     * 创建一个新的user,角色为ROLE_USER
     * @param userDto
     * @return
     */
    public User registerNewAccount(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassWd(passwordEncoder.encode(userDto.getPassWd()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        user.setCreateTime(new Date().getTime());
        user.setLastModifyTime(new Date().getTime());
        return save(user);
    }
}
