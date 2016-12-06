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

public class MessageMj implements IMessage, IMessageHandler<MessageMj, IMessage> {
    private int indexOption, indexEnumTile; //-1 = discard, -2 check in game

    public MessageMj() {
    }

    public MessageMj(int indexOption, int indexEnumTile) {
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
    public IMessage onMessage(MessageMj message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        if (Mahjong.mjGameHandler.isInGame(player)) {
            Game game = Mahjong.mjGameHandler.getGame(player);
            //if (indexOption == -1) game.getUi().discard(game.getPlayer(player.getDisplayNameString()), EnumTile.getTile(indexEnumTile));
            EnumTile enumTile = message.indexEnumTile == -1 ? null : EnumTile.getTile(message.indexEnumTile);
            game.getUi().chooseInt(game.getPlayer(player.getDisplayNameString()), message.indexOption, enumTile);
        }
        return null;
    }
}
