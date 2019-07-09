package com.isssr.ticketing_system.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Implementation of custom dao for executing custom queries
@Repository
@Transactional(readOnly = true)
public class CustomRepositoryImp implements CustomRepository {

    @Override
    public <T> T customQuery(String query, JdbcTemplate jdbcTemplate, Class<T> returnType) throws DataAccessException {

        return jdbcTemplate.queryForObject(query, returnType);

    }

    @Override
    public List<String> getTablesMetadata(Connection connection, boolean isDefault) throws SQLException {

        DatabaseMetaData md = connection.getMetaData();

        ResultSet rs;
        if (isDefault) {

            rs = md.getTables(null, null, "ts_%", new String[]{"TABLE"});

        } else {

            rs = md.getTables(null, null, null, new String[]{"TABLE"});

        }

        List<String> tableNames = new ArrayList<>();

        while (rs.next()) {

            tableNames.add(rs.getString("TABLE_NAME"));

        }

        return tableNames;

    }

    @Override
    public List<String> getTableColumnsMetadata(Connection connection, String tableName) throws SQLException {

        DatabaseMetaData md = connection.getMetaData();

        ResultSet rs = md.getColumns(null, null, tableName, null);

        List<String> columns = new ArrayList<>();

        while (rs.next()) {

            columns.add(rs.getString("COLUMN_NAME"));

        }

        return columns;
    }
}
