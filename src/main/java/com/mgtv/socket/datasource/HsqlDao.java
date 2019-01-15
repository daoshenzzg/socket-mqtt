package com.mgtv.socket.datasource;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * HsqlDataSource 简单操作封装
 *
 * @author zhiguang@mgtv.com
 * @date 2019/1/14 15:06
 */
public class HsqlDao {
    private static final Logger logger = LoggerFactory.getLogger(HsqlDao.class);

    private HsqlDataSource dataSource;

    public HsqlDao(HsqlDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 执行一条SQL
     *
     * @param sql
     * @param args
     * @return
     */
    public int execute(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement statement = null;
        int effect = 0;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    statement.setObject(i + 1, args[i]);
                }
            }
            effect = statement.executeUpdate();
        } catch (Exception ex) {
            logger.error("Failed to execute SQL '{}' with parameters '{}'.",
                    sql, JSON.toJSONString(args), ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    logger.error("Failed to close JDBC statement.", ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Failed to close JDBC connection.", ex);
                }
            }
        }
        return effect;
    }

    /**
     *
     * @param sql
     * @param args
     * @return
     */
    public ResultSet query(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    statement.setObject(i + 1, args[i]);
                }
            }
            resultSet = statement.executeQuery();
        } catch (Exception ex) {
            logger.error("Failed to query result set using SQL '{}' with parameters '{}'.",
                    sql, JSON.toJSONString(args), ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    logger.error("Failed to close JDBC statement.", ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Failed to close JDBC connection.", ex);
                }
            }
        }
        return resultSet;
    }

    public ResultSet query(String sql) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
        } catch (Exception e) {
            logger.error("Failed to query result set using SQL '{}'.", sql, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error("Failed to close JDBC statement.", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Failed to close JDBC connection.", e);
                }
            }
        }

        return resultSet;
    }
}
