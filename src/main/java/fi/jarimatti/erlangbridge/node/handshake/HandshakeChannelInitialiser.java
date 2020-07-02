package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class HandshakeChannelInitialiser extends ChannelInitializer<SocketChannel> {

    private final String name;
    private final Challenger challenger;

    public HandshakeChannelInitialiser(String name, Challenger challenger) {
        this.name = name;
        this.challenger = challenger;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new LengthEncoder())
                .addLast(new LengthDecoder())
                .addLast("send_name_encoder", new SendNameV5Encoder())
                .addLast("client_handshake_decoder", new ClientHandshakeMessageDecoder())
                .addLast("challenge_reply_codec", new ChallengeReplyCodec())
                .addLast("handshake_handler", new HandshakeHandler(name, challenger));
    }
}
