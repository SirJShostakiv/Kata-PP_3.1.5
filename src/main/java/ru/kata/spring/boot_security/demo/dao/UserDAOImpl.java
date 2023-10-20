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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
@Transactional
@ComponentScan("app")
public class UserDAOImpl implements UserDAO {

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
        User updatedUser = em.find(User.class, id);

        updatedUser.setUsername(user.getUsername());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setAge(user.getAge());

        em.merge(updatedUser);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.find(User.class, id));
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public User getByID(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        User user = em.find(User.class, id);
        em.close();
        return user;
    }

    @Override
    public User findByUsername(String email) {
        return read().stream().filter(u -> u.getUsername().equals(email)).findFirst().orElse(null);
    }

    @Override
    public List<Long> getIdList() {
        return read().stream().map(User::getId).toList();
    }

    @Override
    public String getRoles(String email) throws SQLException {
        List<String> rolesId = new ArrayList<>();
        List<String> rolesList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud_app_bootstrap?" +
                "verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&" +
                "serverTimezone=UTC&allowPublicKeyRetrieval=true", "root", "wGgfwfyg672");
             Statement statement1 = conn.createStatement();
             Statement statement2 = conn.createStatement()) {

            String query = String.format("SELECT * FROM users_roles WHERE users_id='%d'", findByUsername(email).getId());
            ResultSet rs1 = statement1.executeQuery(query);
            while (rs1.next()) {
                rolesId.add(rs1.getString(2));
            }
            String[] roles_id = new String[rolesId.size()];
            roles_id = rolesId.toArray(roles_id);

            for (String roles : roles_id) {
                String query2 = String.format("SELECT * FROM roles WHERE id='%s'", roles);
                ResultSet rs2 = statement2.executeQuery(query2);
                rs2.next();
                rolesList.add(rs2.getString(2).substring(5));
            }
            String[] roles = new String[rolesList.size()];
            roles = rolesList.toArray(roles);
            return String.join(" ", roles);

        }
    }

    public void setRoles(String roles) {
        String countQuery = "SELECT COUNT(*) FROM users";
        long count = 0L;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud_app_bootstrap?" +
                "verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&" +
                "serverTimezone=UTC&allowPublicKeyRetrieval=true", "root", "wGgfwfyg672");
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(countQuery);
            while (rs.next()) {
                count++;
            }
            switch (roles) {
                case "ADMIN":
                    String query1 = String.format("INSERT IGNORE INTO users_roles (users_id, roles_id) VALUES (%d, 1)", count + 2);
                    String query2 = String.format("INSERT IGNORE INTO users_roles (users_id, roles_id) VALUES (%d, 2)", count + 2);
                    statement.execute(query1);
                    statement.execute(query2);
                    break;
                case "USER":
                    String query = String.format("INSERT IGNORE INTO users_roles (users_id, roles_id) VALUES (%d, 2)", count + 2);
                    statement.execute(query);
                    break;
                default:
                    throw new RuntimeException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
