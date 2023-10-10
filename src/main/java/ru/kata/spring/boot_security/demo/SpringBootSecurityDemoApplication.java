package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EntityScan("ru.kata.spring.boot_security.demo.model")
@EnableJpaRepositories(basePackages = "ru.kata.spring.boot_security.demo.repository")
public class SpringBootSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud_app_security?" +
                "verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&" +
                "serverTimezone=UTC&allowPublicKeyRetrieval=true", "root", "wGgfwfyg672");
             CallableStatement callable = conn.prepareCall("{call CreateInitialUsers()}")) {

            callable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
