package org.choongang;

import org.choongang.configs.DbConfig2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes= DbConfig2.class)
public class DataSourceTests2 {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() {
        try (Connection con = dataSource.getConnection()) {
            System.out.println(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
