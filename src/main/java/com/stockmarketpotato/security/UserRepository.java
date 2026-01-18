package com.stockmarketpotato.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    
    List<User> findAll();

    @Override
    void delete(User user);
}
