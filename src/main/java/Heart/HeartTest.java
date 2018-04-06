package Heart;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HeartTest {
    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("en", new StringEncoder());
                pipeline.addLast("de", new StringDecoder());
                pipeline.addLast("handler",new HeartHandler());
                // 添加一个 Idle 的监听对象
                /**
                 * 心跳包说白了也是一些消息，只不过这些消息是非常简单的消息，报文简单
                 *
                 * 对于服务器来说心跳包主要就是为了断开那些无用的连接，比如说客户端断电但是 tcp 不会释放链接
                 * 需要服务端来判断客户端是不是已经掉线了，然后把他踢掉。一般服务端回复的消息都是一个系统时间
                 *
                 * 对客户端来说就是用来判断当前连接是不是已经丢失了，也就是用来判断是否需要重连
                 * 以及延时测量等等
                 */
                pipeline.addLast("idle",new IdleStateHandler(new Timer() {
                    @Override
                    public Timeout newTimeout(TimerTask timerTask, long l, TimeUnit timeUnit) {
                        return null;
                    }

                    @Override
                    public Set<Timeout> stop() {
                        return null;
                    }
                }, 5, 5, 10));
                return pipeline;
            }
        });
        bootstrap.bind(new InetSocketAddress("127.0.0.1", 8000));
    }
}
