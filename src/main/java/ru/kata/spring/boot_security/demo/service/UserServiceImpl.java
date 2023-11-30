package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDAOImpl;
import ru.kata.spring.boot_security.demo.exception_handling.NoSuchUserException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserDAOImpl userDAOImpl;

    @Autowired
    public UserServiceImpl(UserDAOImpl userDAOImpl) {
        this.userDAOImpl = userDAOImpl;
    }

    @Transactional
    @Override
    public void create(User user) {
        userDAOImpl.create(user);
    }

    @Transactional
    @Override
    public List<User> read() {
        return userDAOImpl.read();
    }

    @Transactional
    @Override
    public void update(User user) {
        userDAOImpl.update(user);
    }

    @Transactional
    @Override
    public String delete(Long id) {
        if (userDAOImpl.getByID(id) == null) {
            throw new NoSuchUserException("There is no such user with id = "
                    + id + " in the database.");
        } else {
            userDAOImpl.delete(id);
            return "User with ID = " + id + " was deleted.";
        }
    }

    @Transactional
    @Override
    public User getByID(Long id) {
        if (userDAOImpl.getByID(id) == null) {
            throw new NoSuchUserException("There is no such user with id " + id + " in the database.");
        }
        return userDAOImpl.getByID(id);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDAOImpl.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", email));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    @Transactional
    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).toList();
    }

    @Transactional
    @Override
    public User findByEmail(String username) {
        return userDAOImpl.read().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Transactional
    @Override
    public String[] getAllRoles() {
        return userDAOImpl.getAllRoles();
    }
}
