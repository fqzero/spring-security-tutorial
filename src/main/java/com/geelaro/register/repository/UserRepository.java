package com.geelaro.register.repository;

import com.geelaro.register.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByNameOrEmail(String name,String email);

    User findByEmail(String email);

    User findByName(String name);
}
