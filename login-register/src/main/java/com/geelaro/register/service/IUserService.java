package com.geelaro.register.service;

import com.geelaro.register.domain.entity.User;

public interface IUserService {

    User save(User user);

    User findByEmail(String email);

    User findByName(String name);

    boolean checkUserByName(String name);

    boolean checkUserByEmail(String email);

}
