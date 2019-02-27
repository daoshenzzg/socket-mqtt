package com.yb.socket.count;

import com.yb.socket.service.server.Server;
import com.yb.socket.service.server.ServerContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Server统计信息Handle
 * 放在pipeline的最后，因为channelInactive和channelActive事件处理中用到了Server里的最新channels size
 *
 * @author daoshenzzg@163.com
 * @date 2019/1/11 16:53
 */
@ChannelHandler.Sharable
public class CountHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server server = ServerContext.getContext().getServer();
        server.getCountInfo().setCurChannelNum(server.getChannels().size());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Server server = ServerContext.getContext().getServer();
        server.getCountInfo().setCurChannelNum(server.getChannels().size());
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CountInfo countInfo = ServerContext.getContext().getServer().getCountInfo();
        countInfo.getReceiveNum().incrementAndGet();
        countInfo.setLastReceive(System.currentTimeMillis());
        super.channelRead(ctx, msg);
    }

}