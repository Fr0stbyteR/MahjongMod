package net.fr0stbyter.mahjong.network.message;

import io.netty.buffer.ByteBuf;
import net.fr0stbyter.mahjong.Mahjong;
import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.Game;
import net.fr0stbyter.mahjong.util.MahjongLogic.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMjPlayer implements IMessage, IMessageHandler<MessageMjPlayer, IMessage> {
    private int indexOption, indexEnumTile; //-1 = clear

    public MessageMjPlayer() {
    }

    public MessageMjPlayer(int indexOption, int indexEnumTile) {
        this.indexOption = indexOption;
        this.indexEnumTile = indexEnumTile;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.indexOption = buf.readInt();
        this.indexEnumTile = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(indexOption);
        buf.writeInt(indexEnumTile);
    }

    @Override
    public IMessage onMessage(MessageMjPlayer message, MessageContext ctx) {
        //EntityPlayer player = ctx.getServerHandler().playerEntity;
        if (message.indexOption == -1) Mahjong.mjPlayerHandler.clear();
        else Mahjong.mjPlayerHandler.add(message.indexOption, message.indexEnumTile);
        return null;
    }
}
