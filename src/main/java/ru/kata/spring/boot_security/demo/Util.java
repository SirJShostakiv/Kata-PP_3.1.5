package ru.kata.spring.boot_security.demo;

import javax.transaction.Transactional;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Transactional
public class Util {
    public static void initiateUsers() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud_app_bootstrap?" +
                "verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&" +
                "serverTimezone=UTC&allowPublicKeyRetrieval=true", "root", "wGgfwfyg672");
             CallableStatement callable = conn.prepareCall("{call CreateInitialUsers()}")) {

            callable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
