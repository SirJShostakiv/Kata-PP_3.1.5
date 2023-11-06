package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface UserDAO {
    void create(User user);
    List<User> read();
    void update(User user, Long id);
    void delete(Long id);
    User getByID(Long id);
    public User findByEmail(String username);
    List<Long> getIdList();
    public Set<Role> getAllRoles();
    public void updateRoles(User user);
    public void fixRolesId(User user);
    public void deleteRoles(Long id);
}
