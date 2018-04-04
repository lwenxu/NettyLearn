package ClientDemo;

import ServierDemo.HiHandler;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xpf19 on 2018/4/4.
 */
public class ClientTest {
    public static void main(String[] args) {
        ClientBootstrap bootstrap = new ClientBootstrap();
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        bootstrap.setFactory(new NioClientSocketChannelFactory());
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("hiHandler", new HiHandler());
                return pipeline;
            }
        });
        ChannelFuture connect = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8000));
        Scanner scanner = new Scanner(System.in);
        Channel channel = connect.getChannel();
        while (true) {
            channel.write(scanner.nextLine());
        }
    }
}
