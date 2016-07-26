package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.HashMap;

import static net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile.*;
import static net.fr0stbyter.mahjong.util.MahjongLogic.EnumTileGroup.*;


public class GameType {
    public enum GameRegion {JAPAN}
    private GameRegion region;
    private int playerCount; // 3 4
    private int length; // 1 2 4
    private int redDoraCount; // 1-4
    public GameType(GameRegion regionIn, int playerCountIn, int lengthIn, int redDoraCountIn) {
        region = regionIn;
        playerCount = playerCountIn;
        length = lengthIn;
        redDoraCount = redDoraCountIn;
    }
    public HashMap<EnumTile, Integer> getTilesCount() {
        GameRegion region = this.region;
        int playerCount = this.playerCount;
        int redDoraCount = this.redDoraCount;
        HashMap<EnumTile, Integer> tiles = new HashMap<EnumTile, Integer>();
        switch (region) {
            case JAPAN: {
                if (playerCount == 3) {
                    tiles.put(M1, 4);
                    tiles.put(M9, 4);
                    for (EnumTile tile : EnumTile.values()) {
                        if ((tile.getGroup() == PIN) && (!tile.isRed())) tiles.put(tile, 4);
                        if ((tile.getGroup() == SOU) && (!tile.isRed())) tiles.put(tile, 4);
                        if ((tile.getGroup() == WIND) && (!tile.isRed())) tiles.put(tile, 4);
                        if ((tile.getGroup() == DRAGON) && (!tile.isRed())) tiles.put(tile, 4);
                    }
                    if ((redDoraCount > 0) && (redDoraCount <= 8)) {
                        int p5rCount = (redDoraCount / 2) - (redDoraCount % 2);
                        int s5rCount = redDoraCount / 2;
                        tiles.put(P5, 4 - p5rCount);
                        tiles.put(P5R, p5rCount);
                        tiles.put(S5, 4 - s5rCount);
                        tiles.put(S5R, s5rCount);
                    }
                    return tiles;
                }
                if (playerCount == 4) {
                    for (EnumTile tile : EnumTile.values()) {
                        if (tile.getGroup() == MAN) tiles.put(tile, 4);
                        if (tile.getGroup() == PIN) tiles.put(tile, 4);
                        if (tile.getGroup() == SOU) tiles.put(tile, 4);
                        if (tile.getGroup() == WIND) tiles.put(tile, 4);
                        if (tile.getGroup() == DRAGON) tiles.put(tile, 4);
                    }
                    if ((redDoraCount > 0) && (redDoraCount <= 12)) {
                        int m5rCount = (redDoraCount / 2) - (redDoraCount % 2 == 1 ? 1 : 0);
                        int p5rCount = (redDoraCount / 2) - (redDoraCount % 2 == 2 ? 1 : 0);
                        int s5rCount = redDoraCount / 2;
                        tiles.put(M5, 4 - m5rCount);
                        tiles.put(M5R, m5rCount);
                        tiles.put(P5, 4 - p5rCount);
                        tiles.put(P5R, p5rCount);
                        tiles.put(S5, 4 - s5rCount);
                        tiles.put(S5R, s5rCount);
                    }
                    return tiles;
                }
            }
            default: return null;
        }
    }

    public ArrayList<EnumTile> getTiles() {
        HashMap<EnumTile, Integer> getTilesCount = getTilesCount();
        ArrayList<EnumTile> tiles = new ArrayList<EnumTile>();
        for (EnumTile tile : getTilesCount.keySet()) {
            for (int i = 0; i < getTilesCount.get(tile); i++) {
                tiles.add(tile);
            }
        }
        return tiles;
    }

    public GameRegion getRegion() {
        return region;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getLength() {
        return length;
    }

    public int getRedDoraCount() {
        return redDoraCount;
    }
}
