package net.fr0stbyter.mahjong.network.message;

import io.netty.buffer.ByteBuf;
import net.fr0stbyter.mahjong.Mahjong;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMjStatus implements IMessage, IMessageHandler<MessageMjStatus, IMessage> {
    private int messageType; //-1 clear options 0 state, 1 curPlayer, 2 options
    private int playersCount, round, hand, extra, gameLength, riichibou;
    private String[] playersName;
    private int[] playersScore;
    private int curPos;
    private int option, tilesCount;
    private int[] tiles;

    public MessageMjStatus() {
    }

    public MessageMjStatus(int messageType) {
        this.messageType = messageType;
    }

    public MessageMjStatus(int messageType, int playersCount, int round, int hand, int extra, int gameLength, int riichibou, String[] playersName, int[] playersScore) {
        this.messageType = messageType;
        this.playersCount = playersCount;
        this.round = round;
        this.hand = hand;
        this.extra = extra;
        this.riichibou = riichibou;
        this.gameLength = gameLength;
        this.playersName = playersName;
        this.playersScore = playersScore;
    }

    public MessageMjStatus(int messageType, int curPos) {
        this.messageType = messageType;
        this.curPos = curPos;
    }

    public MessageMjStatus(int messageType, int option, int tilesCount, int[] tiles) {
        this.messageType = messageType;
        this.option = option;
        this.tilesCount = tilesCount;
        this.tiles = tiles;
    }

    public void fromBytes(ByteBuf buf) {
        this.messageType =  buf.readInt();
        if (messageType == 0) {
            this.playersCount = buf.readInt();
            this.round = buf.readInt();
            this.hand = buf.readInt();
            this.extra = buf.readInt();
            this.gameLength = buf.readInt();
            this.riichibou = buf.readInt();
            this.playersName = new String[playersCount];
            this.playersScore = new int[playersCount];
            for (int i = 0; i < playersCount; i++) {
                playersName[i] = ByteBufUtils.readUTF8String(buf);
                playersScore[i] = buf.readInt();
            }
            return;
        }
        if (messageType == 1) {
            this.curPos = buf.readInt();
            return;
        }
        if (messageType == 2) {
            this.option = buf.readInt();
            this.tilesCount = buf.readInt();
            this.tiles = new int[tilesCount];
            for (int i = 0; i < tilesCount; i++) {
                this.tiles[i] = buf.readInt();
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(messageType);
        if (messageType == 0) {
            buf.writeInt(playersCount);
            buf.writeInt(round);
            buf.writeInt(hand);
            buf.writeInt(extra);
            buf.writeInt(gameLength);
            buf.writeInt(riichibou);
            for (int i = 0; i < playersCount; i++) {
                ByteBufUtils.writeUTF8String(buf, playersName[i]);
                buf.writeInt(playersScore[i]);
            }
            return;
        }
        if (messageType == 1) {
            buf.writeInt(curPos);
            return;
        }
        if (messageType == 2) {
            buf.writeInt(option);
            buf.writeInt(tilesCount);
            for (int i = 0; i < tilesCount; i++) {
                buf.writeInt(tiles[i]);
            }
        }
    }

    @Override
    public IMessage onMessage(MessageMjStatus message, MessageContext ctx) {
        if (message.messageType == -1) {
            Mahjong.mjPlayerHandler.clearOptions();
            return null;
        }
        if (message.messageType == 0) {
            Mahjong.mjPlayerHandler.updateState(new int[]{message.playersCount, message.round, message.hand, message.extra, message.gameLength, message.riichibou}, message.playersName, message.playersScore);
            return null;
        }
        if (message.messageType == 1) {
            Mahjong.mjPlayerHandler.updateCurPos(message.curPos);
            return null;
        }
        if (message.messageType == 2) {
            Mahjong.mjPlayerHandler.addOption(message.option, message.tiles);
            return null;
        }
        return null;
    }
}
