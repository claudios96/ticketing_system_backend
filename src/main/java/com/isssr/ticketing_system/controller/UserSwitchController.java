package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.logEnabler.LogEnabler;
import com.isssr.ticketing_system.logger.aspect.LogOperation;
import com.isssr.ticketing_system.entity.db_connection.DBConnectionModeEnum;
import com.isssr.ticketing_system.dao.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@Component
@Service
public class UserSwitchController {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${root.mode.username}")
    private String ROOT_USERNAME;

    @Value("${root.mode.password}")
    private String ROOT_PASSWORD;

    @Value("${readOnly.mode.username}")
    private String USER_USERNAME;

    @Value("${readOnly.mode.password}")
    private String USER_PASSWORD;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CustomRepository customRepository;

    @Autowired
    private LogEnabler logEnabler;

    public <T> T doNotLog(String query, Class<T> returnType, String dbURL, String dbUsername, String dbPassword, String dbDriver) throws SQLException, DataAccessException {

        setLogOption(false);

        return doQueryReadOnlyMode(query, returnType, dbURL, dbUsername, dbPassword, dbDriver);

    }

    //Setting log option by changing param - Enable/Disable
    public void setLogOption(boolean flag) {
        Method method;
        try {
            method = UserSwitchController.class.getMethod("doQueryReadOnlyMode", String.class, Class.class, String.class, String.class, String.class, String.class);
            LogOperation methodAnnotation = method.getAnnotation(LogOperation.class);
            logEnabler.changeAnnotationValue(methodAnnotation, "isEnabled", flag);
            System.out.println(methodAnnotation.isEnabled());
            System.out.println(flag);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    //Use a read only user to do job
    @LogOperation(tag = "QUERY_EXECUTE", inputArgs = {"query"})
    public synchronized <T> T doQueryReadOnlyMode(String query, Class<T> returnType, String dbURL, String dbUsername, String dbPassword, String dbDriver) throws SQLException, DataAccessException {
        //Go with the connection of a new user
        this.jdbcTemplate.setDataSource(getDataSource(dbURL, dbUsername, dbPassword, dbDriver, DBConnectionModeEnum.READ_ONLY_MODE));
        Connection connection;
        T result = null;

        connection = this.jdbcTemplate.getDataSource().getConnection();
        try {
            result = this.customRepository.customQuery(query, jdbcTemplate, returnType);
        } catch (Exception e) {
            connection.close();
            throw e;
        }
        connection.close();

        return result;
    }

    public Connection getReadOnlyConnection(@Nullable String dbURL, @Nullable String dbUsername, @Nullable String dbPassword, @Nullable String dbDriver) throws SQLException {

        this.jdbcTemplate.setDataSource(getDataSource(dbURL, dbUsername, dbPassword, dbDriver, DBConnectionModeEnum.READ_ONLY_MODE));

        return this.jdbcTemplate.getDataSource().getConnection();
    }

    //Get Connection with a 'read only' user to db
    private DataSource getDataSource(@Nullable String dbURL, @Nullable String dbUsername, @Nullable String dbPassword, @Nullable String dbDriver, DBConnectionModeEnum mode) {

        if (dbURL == null)
            dbURL = this.url; //set default url

        if (dbUsername == null)
            switch (mode) { //set default username
                case READ_ONLY_MODE:
                    dbUsername = this.USER_USERNAME;
                    break;
                case ROOT_MODE:
                    dbUsername = this.ROOT_USERNAME;
            }

        if (dbPassword == null)
            switch (mode) { //set default password
                case READ_ONLY_MODE:
                    dbPassword = this.USER_PASSWORD;
                    break;
                case ROOT_MODE:
                    dbPassword = this.ROOT_PASSWORD;
            }


        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        if (dbDriver != null) {

            dataSource.setDriverClassName(dbDriver);

        } else {

            dataSource.setDriverClassName(driver);

        }

        dataSource.setUrl(dbURL);

        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }
}
