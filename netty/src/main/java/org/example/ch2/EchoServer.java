package org.example.ch2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * <p>
 *
 * </p>
 *
 * @author Meko
 * @date 2021/10/7 10:31 上午
 */
public class EchoServer {
    private final int port;
    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        int port = 8088;
        new EchoServer(port).start();

    }

    private void start() throws Exception {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            final ServerBootstrap b = new ServerBootstrap();
            b.group(nioEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(echoServerHandler);
                        }
                    });
            final ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        }finally {
            nioEventLoopGroup.shutdownGracefully().sync();
        }

    }
}
