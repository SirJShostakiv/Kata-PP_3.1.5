package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.*;
import java.util.*;

@Repository
@Transactional
@ComponentScan("app")
public class UserDAOImpl implements UserDAO {

    private final String URL = "jdbc:mysql://localhost:3306/crud_app_bootstrap?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String DATABASE_USERNAME = "root";
    private final String DATABASE_PASSWORD = "wGgfwfyg672";
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public UserDAOImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void create(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        fixRolesId(user);
    }

    @Override
    public List<User> read() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.find(User.class, 1L);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);

        TypedQuery<User> query = em.createQuery(cq);
        List<User> userList = query.getResultList();
        em.close();
        return userList;
    }

    @Override
    public void update(User user, Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        User updatedUser = em.find(User.class, user.getId());

        updatedUser.setId(user.getId());
        updatedUser.setFirstname(user.getFirstname());
        updatedUser.setLastname(user.getLastname());
        updatedUser.setAge(user.getAge());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setEmail(user.getEmail());
        updateRoles(user);

        em.merge(updatedUser);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(Long id) {
        deleteRoles(id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.find(User.class, id));
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public User findByEmail(String email) {
        return read().stream().filter(u -> u.getUsername().equals(email)).findFirst().orElse(null);
    }

    @Override
    public Set<Role> getAllRoles() {
        Set<Role> rolesList = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             Statement statement = conn.createStatement()) {

            String query = "SELECT * FROM roles";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Long id = rs.getLong(1);
                String role = rs.getString(2);
                Role userRole = new Role(id, role);
                rolesList.add(userRole);
            }
            return rolesList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRoles(User user) {
        try (Connection conn = DriverManager.getConnection(URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             Statement statement = conn.createStatement()) {
            String userRoles = String.format("SELECT * FROM users_roles WHERE users_id=%d", user.getId());
            ResultSet rs = statement.executeQuery(userRoles);

            List<String> userPastRolesList = new ArrayList<>();
            List<String> userFutureRolesList = user.getRoles().stream().map(Role::getName).toList();
            while (rs.next()) {
                switch (rs.getString(2)) {
                    case "1":
                        userPastRolesList.add("ROLE_ADMIN");
                        break;
                    case "2":
                        userPastRolesList.add("ROLE_USER");
                        break;
                    default:
                        throw new RuntimeException("Switch exception");
                }
            }
            if (userPastRolesList.contains("ROLE_ADMIN") && userFutureRolesList.contains("ROLE_USER")) {
                String query = String.format("DELETE FROM users_roles WHERE users_id=%d AND roles_id=1", user.getId());
                statement.execute(query);
            } else if (userPastRolesList.contains("ROLE_USER") && userPastRolesList.size() == 1 && userFutureRolesList.contains("ROLE_ADMIN")) {
                String query = String.format("INSERT IGNORE INTO users_roles (users_id, roles_id) VALUES (%d, 1)", user.getId());
                statement.execute(query);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fixRolesId(User user) {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             Statement statement = conn.createStatement();
             Statement statement1 = conn.createStatement();
             Statement statement2 = conn.createStatement()) {
            String query1 = String.format("SELECT * FROM users_roles WHERE users_id=%d", user.getId());
            ResultSet rs = statement.executeQuery(query1);

            while (rs.next()) {
                count++;
            }

            if (count == 2) {
                String query2 = String.format("UPDATE IGNORE users_roles SET roles_id=1 WHERE users_id=%d AND roles_id=3", user.getId());
                statement1.execute(query2);
                String query3 = String.format("UPDATE IGNORE users_roles SET roles_id=2 WHERE users_id=%d AND roles_id=4", user.getId());
                statement2.execute(query3);
            } else if (count == 1) {
                String query3 = String.format("UPDATE IGNORE users_roles SET roles_id=2 WHERE users_id=%d", user.getId());
                statement2.execute(query3);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRoles(Long id) {
        try (Connection conn = DriverManager.getConnection(URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             Statement statement = conn.createStatement()) {
            String query = String.format("DELETE IGNORE FROM users_roles WHERE users_id=%d", id);
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
