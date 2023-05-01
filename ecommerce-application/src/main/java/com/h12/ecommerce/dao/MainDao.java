package com.h12.ecommerce.dao;

import com.h12.ecommerce.configurations.model.DBConfig;
import com.h12.ecommerce.exceptions.InternalServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Repository
public class MainDao {
    private Connection connection;
    @Autowired
    private DBConfig dbConfig;

    /**
     * Default constructor.
     */
    protected MainDao()  {
    }

    /**
     * To create connection.
     * @return connection object.
     * @throws InternalServerException when sql statement execution didn't go well.
     */
    protected Connection getConnection() throws InternalServerException {
        this.createConnection();
        return this.connection;
    }

    /**
     *
     * @throws InternalServerException when sql statement execution didn't go well.
     */
    private void createConnection() throws  InternalServerException {
//        Properties properties = new Properties();
//        String cwd = System.getProperty("user.dir");
//        String fileName = cwd + "/src/main/resources/dbconfig.properties";
//        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
//            properties.load(fileInputStream);
//        } catch (Exception e) {
//            throw new DBConfigFileMissingException("Properties File not found.");
//        }
//        String jdbcUrl = properties.getProperty("db.url");
//        String dataBaseName = properties.getProperty("db.name");
//        String userName = properties.getProperty("db.username");
//        String userPassword = properties.getProperty("db.password");
        String url = dbConfig.getUrl();
        String userName = dbConfig.getUserName();
        String userPassword = dbConfig.getPassword();
//        String url = jdbcUrl + dataBaseName;
        try {
            connection = DriverManager.getConnection(url, userName, userPassword);
        } catch (SQLException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

}
