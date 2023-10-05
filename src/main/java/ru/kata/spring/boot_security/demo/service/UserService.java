package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    void create(User user);
    List<User> read();
    void update(String name, String password, int age, Long id);
    void delete(Long id);

    User getByID(Long id);
}
