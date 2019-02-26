package com.mgtv.socket.push.server;

import com.mgtv.socket.datasource.HsqlDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/2/26 14:31
 */

public class SubscribeServer {
    private QueryRunner qr;

    public SubscribeServer(HsqlDataSource dataSource) {
        this.qr = new QueryRunner(dataSource);
    }

    /**
     * 订阅主题
     *
     * @param clientId
     * @param channelId
     * @param topic
     * @throws SQLException
     */
    public void subscribe(String clientId, String channelId, String topic) throws SQLException {
        String sql = "INSERT INTO subscribe_user(channel_id, client_id, topic, create_time) VALUES(?, ?, ?, ?)";
        qr.execute(sql, channelId, clientId, topic, System.currentTimeMillis() / 1000);
    }

    /**
     * 取消订阅
     *
     * @param clientId
     * @param topic
     */
    public void unSubscribe(String clientId, String topic) throws SQLException {
        String sql = "DELETE FROM subscribe_user WHERE client_id = ? AND topic = ?";
        qr.execute(sql, clientId, topic);
    }

    /**
     * 取消订阅
     *
     * @param channelId
     */
    public void unSubscribe(String channelId) throws SQLException {
        String sql = "DELETE FROM subscribe_user WHERE client_id = ? AND topic = ?";
        qr.execute(sql, channelId);
    }

    /**
     * 根据主题查询订阅者
     *
     * @param topic
     * @return
     * @throws SQLException
     */
    public List<String> getChannelByTopic(String topic) throws SQLException {
        String sql = "SELECT channel_id FROM subscribe_user WHERE topic = ?";
        return qr.query(sql, new ColumnListHandler<>("channel_id"), topic);
    }

    public QueryRunner getQr() {
        return qr;
    }

    public void setQr(QueryRunner qr) {
        this.qr = qr;
    }
}
