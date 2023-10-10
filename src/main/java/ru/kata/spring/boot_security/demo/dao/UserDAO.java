package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDAO {
    void create(User user);
    List<User> read();
    void update(User user, Long id);
    void delete(Long id);
    User getByID(Long id);
    public User findByUsername(String username);
    List<Long> getIdList();
}
