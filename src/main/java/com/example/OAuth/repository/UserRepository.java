package com.example.OAuth.repository;

import com.example.OAuth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public abstract Optional<User> findByEmail(String email);

}
