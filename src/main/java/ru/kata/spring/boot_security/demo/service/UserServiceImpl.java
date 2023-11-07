package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDAOImpl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    public List<Long> getIdList() {
        return userDAOImpl.getIdList();
    }
    @Transactional
    @Override
    public Set<Role> getAllRoles() {
        return userDAOImpl.getAllRoles();
    }
    public UserDAOImpl getDAO() {
        return userDAOImpl;
    }
}
