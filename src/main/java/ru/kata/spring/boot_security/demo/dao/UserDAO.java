package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDAO {
    void create(User user);
    List<User> read();
    void update(User user);
    void delete(Long id);
    User getByID(Long id);
    public User findByEmail(String username);
    List<Long> getIdList();
    public String[] getAllRoles();
    public void updateRoles(User user);
    public void fixRolesId(User user);
    public void deleteRoles(Long id);
}
