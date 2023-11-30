package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    void create(User user);
    List<User> read();
    void update(User user);
    String delete(Long id);
    User getByID(Long id);
    public User findByEmail(String username);
    public String[] getAllRoles();
}
