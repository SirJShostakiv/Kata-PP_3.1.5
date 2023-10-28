package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static ru.kata.spring.boot_security.demo.Util.initiateUsers;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EntityScan("ru.kata.spring.boot_security.demo.model")
public class SpringBootSecurityDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
        initiateUsers();
    }
}
