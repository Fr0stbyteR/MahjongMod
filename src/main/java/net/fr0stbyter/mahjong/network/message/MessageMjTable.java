package net.fr0stbyter.mahjong.network.message;

import io.netty.buffer.ByteBuf;
import net.fr0stbyter.mahjong.Mahjong;
import net.fr0stbyter.mahjong.util.MahjongLogic.GameType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMjTable implements IMessage, IMessageHandler<MessageMjTable, IMessage> {
    private boolean isAdd; //false:kick
    private int x, y, z;
    private int region, playerCount, length, redDoraCount;

    public MessageMjTable() {
    }

    public MessageMjTable(boolean isAdd, int x, int y, int z, int region, int playerCount, int length, int redDoraCount) {
        this.isAdd = isAdd;
        this.x = x;
        this.y = y;
        this.z = z;
        this.region = region;
        this.playerCount = playerCount;
        this.length = length;
        this.redDoraCount = redDoraCount;
    }

    public MessageMjTable(boolean isAdd) {
        this.isAdd = isAdd;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isAdd = buf.readBoolean();
        if (!this.isAdd) return;
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.region = buf.readInt();
        this.playerCount = buf.readInt();
        this.length = buf.readInt();
        this.redDoraCount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isAdd);
        if (!isAdd) return;
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(region);
        buf.writeInt(playerCount);
        buf.writeInt(length);
        buf.writeInt(redDoraCount);
    }

    @Override
    public IMessage onMessage(MessageMjTable message, MessageContext ctx) {
        //World world = ctx.getServerHandler().playerEntity.worldObj;
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        if (!message.isAdd) {
            Mahjong.mjGameHandler.kickPlayer(player);
            return null;
        }
        BlockPos pos = new BlockPos(message.x, message.y, message.z);
        EnumFacing facing = EnumFacing.EAST;
        int diffX = player.getPosition().getX() - message.x;
        int diffZ = player.getPosition().getZ() - message.z;
        if (diffX >= Math.abs(diffZ)) facing = EnumFacing.EAST; //player is east from block
        else if (diffX <= Math.abs(diffZ) * -1) facing = EnumFacing.WEST;
        else if (diffZ >= Math.abs(diffX)) facing = EnumFacing.SOUTH;
        else if (diffZ <= Math.abs(diffX) * -1) facing = EnumFacing.NORTH;
        GameType gameType = new GameType(GameType.GameRegion.JAPAN, message.playerCount, message.length, message.redDoraCount);
        Mahjong.mjGameHandler.addWaitingPlayer(player, facing, pos, gameType);
        return null;
    }
}
