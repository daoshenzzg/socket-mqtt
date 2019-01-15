package com.mgtv.socket.push.server;

import com.mgtv.socket.datasource.HsqlDao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/14 18:45
 */
public class ChannelDao {

    private HsqlDao hsqlDao;

    public void setHsqlDao(HsqlDao hsqlDao) {
        this.hsqlDao = hsqlDao;
    }

    public int connect(String channelId, String uuid) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO channel_user (channel_id, uuid, create_time)\n");
        sql.append("VALUES (?, ?, ?)\n");

        return hsqlDao.execute(sql.toString(), channelId, uuid, new Timestamp(System.currentTimeMillis()));
    }

    public int disConnect(String channelId) {
        String sql = "DELETE FROM channel_user WHERE channel_id = ?";
        return hsqlDao.execute(sql.toString(), channelId);
    }

    public List<ActivityChannelDO> getAll() {
        ResultSet resultSet = hsqlDao.query("SELECT * FROM channel_user", null);
        try {
            List<ActivityChannelDO> list = new ArrayList<>();
            while (resultSet.next()) {
                ActivityChannelDO item = new ActivityChannelDO();
                item.setChannelId(resultSet.getString("channel_id"));
                item.setUuid(resultSet.getString("uuid"));
            }
            return list;
        }catch (Exception ex) {

        }

        return null;
    }
}
