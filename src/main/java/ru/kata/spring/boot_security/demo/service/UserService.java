package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService extends UserDetailsService {
    void create(User user);
    List<User> read();
    void update(User user, Long id);
    void delete(Long id);
    User getByID(Long id);
    List<Long> getIdList();
    public User findByUsername(String username);
    public String[] getRoles(String username) throws SQLException;
}
