package com.yb.socket.datasource;

import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * 针对hsqldb的简单datasource实现
 *
 * @author daoshenzzg@163.com
 * @date 2019/1/14 11:19
 */
public class HsqlDataSource implements DataSource {
    private final String CLASSPATH_PREFIX = "classpath:";
    /**
     * 连接池大小
     */
    private int poolSize = 3;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    /**
     * 获取连接的超时时间
     */
    private int timeout = 1000;
    /**
     * 建表脚本
     */
    private String tableScript;
    private boolean inited = false;

    private LinkedList<Connection> connectionPool = new LinkedList<Connection>();

    public HsqlDataSource() {
    }

    public HsqlDataSource(String driverClassName, String url, String username, String password,
                          String tableScript, int poolSize, int timeout) {
        super();
        this.poolSize = poolSize;
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableScript = tableScript;
        this.timeout = timeout;
    }


    /**
     * 初始化datasource，datasource使用前必须初始化
     */
    public void init() {
        InputStream is = null;
        try {
            Class.forName(driverClassName);
            if (poolSize <= 0) {
                throw new IllegalArgumentException("Invalid pool size " + poolSize);
            }

            if (StringUtils.isBlank(tableScript)) {
                throw new IllegalArgumentException("tableScript cannot be null");
            }

            if (tableScript.startsWith(CLASSPATH_PREFIX)) {
                is = this.getClass().getClassLoader().getResourceAsStream(tableScript.substring(CLASSPATH_PREFIX.length()));
            } else {
                is = new FileInputStream(tableScript.substring(CLASSPATH_PREFIX.length()));
            }
            if (is == null) {
                throw new FileNotFoundException(tableScript + " cannot be opened because it does not exist");
            }

            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(url, username, password);
                // 获取被代理的对象
                connection = ConnectionProxy.getProxyedConnection(connection, connectionPool);
                // 添加被代理的对象
                connectionPool.add(connection);
            }

            //开始建表
            createTable(is);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ex) {
                }
            }
        }

        inited = true;
    }

    private void createTable(InputStream is) {
        Connection connection = null;
        Statement statement = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String sql = null;

            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            while ((sql = br.readLine()) != null) {
                statement.execute(sql.trim());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception ex) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new RuntimeException("Nonsupport Operation.");
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new RuntimeException("Nonsupport Operation.");
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new RuntimeException("Nonsupport Operation.");
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> clz) throws SQLException {
        return (T) this;
    }

    @Override
    public boolean isWrapperFor(Class<?> clz) throws SQLException {
        return DataSource.class.equals(clz);
    }

    @Override
    public Connection getConnection() throws SQLException {
        synchronized (connectionPool) {
            if (!inited) {
                init();
            }

            if (connectionPool.size() == 0) {
                try {
                    connectionPool.wait(timeout);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                return getConnection();
            } else {
                Connection connection = connectionPool.removeFirst();
                return connection;
            }
        }
    }

    static class ConnectionProxy implements InvocationHandler {
        private Object obj;
        private LinkedList<Connection> connectionPool;

        private ConnectionProxy(Object obj, LinkedList<Connection> connectionPool) {
            this.obj = obj;
            this.connectionPool = connectionPool;
        }

        public static Connection getProxyedConnection(Object obj, LinkedList<Connection> connectionPool) {

            Object proxed = Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                    new Class[]{Connection.class},
                    new ConnectionProxy(obj, connectionPool));

            return (Connection) proxed;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = "close";
            if (methodName.equals(method.getName())) {
                synchronized (connectionPool) {
                    // 将被代理的对象放回池中
                    connectionPool.add((Connection) proxy);
                    // 通知等待线程去获取一个连接吧
                    connectionPool.notify();
                }
                return null;
            } else {
                return method.invoke(obj, args);
            }
        }
    }

    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        return getConnection();
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setTableScript(String tableScript) {
        this.tableScript = tableScript;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
