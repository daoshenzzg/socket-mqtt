package com.mgtv.socket.push.database;

import com.mgtv.socket.datasource.HsqlDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;

import java.sql.SQLException;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/14 15:47
 */
public class DatabaseTest {

    public static void main(String[] args) throws Exception {
        HsqlDataSource dataSource = new HsqlDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:mem:student");
        dataSource.setUsername("admin");
        dataSource.setPassword("");
        dataSource.setTableScript("classpath:createTable.sql");
        dataSource.init();

        QueryRunner qr = new QueryRunner(dataSource);

        long t1 = System.currentTimeMillis();
        System.out.println("开始插入数据");
        insert(qr);
        long t2 = System.currentTimeMillis();
        System.out.println("插入数据耗时:" + (t2 - t1) + "ms");

        System.out.println("开始查询数据");
        query(qr);
        long t3 = System.currentTimeMillis();
        System.out.println("查询数据耗时:" + (t3 - t2) + "ms");

    }

    private static void insert(QueryRunner qr) throws SQLException {
        String sql = "insert into classes(id, name, remark) values(?, ?, ?)";

        for (int i = 0; i < 10000; i++) {
            qr.execute(sql, i, "name" + i, "remark" + i);
        }
    }

    private static void query(QueryRunner qr) throws SQLException {
        String sql = "select * from classes where id=?";
        for (int i = 0; i < 10000; i++) {
            Object[] arr = qr.query(sql, new ArrayHandler(), i);
        }
    }
}
