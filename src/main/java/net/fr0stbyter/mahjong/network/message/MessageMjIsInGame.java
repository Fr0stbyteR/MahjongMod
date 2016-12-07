package net.fr0stbyter.mahjong.network.message;

import io.netty.buffer.ByteBuf;
import net.fr0stbyter.mahjong.Mahjong;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.ByteBuffer;

public class MessageMjIsInGame implements IMessage, IMessageHandler<MessageMjIsInGame, IMessage> {
    private int isInGame; // 0no 1waiting, 2gaming
    private long gameId;

    public MessageMjIsInGame() {
    }

    public MessageMjIsInGame(int isInGame, long gameId) {
        this.isInGame = isInGame;
        this.gameId = gameId;
    }

    public MessageMjIsInGame(int isInGame) {
        this.isInGame = isInGame;
    }

    public void fromBytes(ByteBuf buf) {
            this.isInGame = buf.readInt();
            this.gameId = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(isInGame);
        buf.writeLong(gameId);
    }

    @Override
    public IMessage onMessage(MessageMjIsInGame message, MessageContext ctx) {
        if (message.isInGame == 0) {
            Mahjong.mjPlayerHandler.setGame(message.isInGame, "");
        } else {
            Mahjong.mjPlayerHandler.setGame(message.isInGame, Long.toHexString(message.gameId));
        }
        return null;
    }
}
