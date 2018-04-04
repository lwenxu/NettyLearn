package ServierDemo;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.ServerSocketChannel;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xpf19 on 2018/4/4.
 */
public class ServiceTest {
    public static void main(String[] args) {
        // 创建一个启动器
        ServerBootstrap bootstrap = new ServerBootstrap();
//         两个线程池
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
//        设置channel工厂
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));
//        设置管道工厂
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("hiHandler", new HiHandler());
                return pipeline;
            }
        });
//        监听
        bootstrap.bind(new InetSocketAddress("127.0.0.1", 8000));
    }
}
