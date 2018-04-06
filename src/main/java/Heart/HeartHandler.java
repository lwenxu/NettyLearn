package Heart;

import org.jboss.netty.channel.*;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class HeartHandler extends SimpleChannelHandler {
    // 这个方法就会处理心跳事件的，也就是看事件是不是 Idle事件
    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof IdleStateEvent) {
            if (((IdleStateEvent) e).getState() == IdleState.ALL_IDLE) {
                ChannelFuture future = ctx.getChannel().write("服务端将关闭此链接");
                future.addListener(channelFuture -> {
                    ctx.getChannel().close();
                });
            }
        }else {
            super.handleUpstream(ctx, e);
        }

    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ctx.getChannel().write("Hi");
        System.out.println(e.getMessage());
    }
}
