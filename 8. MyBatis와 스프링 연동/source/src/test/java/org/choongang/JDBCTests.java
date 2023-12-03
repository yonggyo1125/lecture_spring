package org.choongang;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCTests {
    @BeforeAll
    static void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnection() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/teamb2", "teamb2", "_aA123456")) {
            System.out.println(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
