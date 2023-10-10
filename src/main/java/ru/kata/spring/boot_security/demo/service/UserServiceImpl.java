package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.dao.UserDAOImpl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
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
    public void update(User user, Long id) {
        userDAOImpl.update(user, id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userDAOImpl.delete(id);
    }

    @Transactional
    @Override
    public User getByID(Long id) {
        return userDAOImpl.getByID(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAOImpl.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).toList();
    }

    @Override
    public User findByUsername(String username) {
        return userDAOImpl.read().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }
    @Override
    public List<Long> getIdList() {
        return userDAOImpl.getIdList();
    }
}
