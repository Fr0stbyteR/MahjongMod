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
    public static final HashMap<EnumTile, Integer> TILESJ3R = GameType.getTilesCount(GameType.GameRegion.JAPAN, 3, 2);
    public static final HashMap<EnumTile, Integer> TILESJ3 = GameType.getTilesCount(GameType.GameRegion.JAPAN, 3, 0);
    public static final HashMap<EnumTile, Integer> TILESJ4R = GameType.getTilesCount(GameType.GameRegion.JAPAN, 4, 3);
    public static final HashMap<EnumTile, Integer> TILESJ4 = GameType.getTilesCount(GameType.GameRegion.JAPAN, 4, 0);

    public GameType(GameRegion regionIn, int playerCountIn, int lengthIn, int redDoraCountIn) {
        region = regionIn;
        playerCount = playerCountIn;
        length = lengthIn;
        redDoraCount = redDoraCountIn;
    }

    public HashMap<EnumTile, Integer> getTilesCount() {
        return getTilesCount(region, playerCount, redDoraCount);
    }

    public static HashMap<EnumTile, Integer> getTilesCount(GameRegion regionIn, int playerCountIn, int redDoraCountIn) {
        HashMap<EnumTile, Integer> tiles = new HashMap<EnumTile, Integer>();
        switch (regionIn) {
            case JAPAN: {
                if (playerCountIn == 3) {
                    tiles.put(M1, 4);
                    tiles.put(M9, 4);
                    for (EnumTile tile : EnumTile.values()) {
                        if (tile.getGroup() == PIN && !tile.isRed()) tiles.put(tile, 4);
                        if (tile.getGroup() == SOU && !tile.isRed()) tiles.put(tile, 4);
                        if (tile.getGroup() == WIND && !tile.isRed()) tiles.put(tile, 4);
                        if (tile.getGroup() == DRAGON && !tile.isRed()) tiles.put(tile, 4);
                    }
                    if ((redDoraCountIn > 0) && (redDoraCountIn <= 8)) {
                        int p5rCount = redDoraCountIn / 2 + (redDoraCountIn % 2);
                        int s5rCount = redDoraCountIn / 2;
                        if (p5rCount > 0) {
                            tiles.put(P5, 4 - p5rCount);
                            tiles.put(P5R, p5rCount);
                        }
                        if (s5rCount > 0) {
                            tiles.put(S5, 4 - s5rCount);
                            tiles.put(S5R, s5rCount);
                        }
                    }
                    return tiles;
                }
                if (playerCountIn == 4) {
                    for (EnumTile tile : EnumTile.values()) {
                        if (tile.getGroup() == MAN && !tile.isRed()) tiles.put(tile, 4);
                        if (tile.getGroup() == PIN && !tile.isRed()) tiles.put(tile, 4);
                        if (tile.getGroup() == SOU && !tile.isRed()) tiles.put(tile, 4);
                        if (tile.getGroup() == WIND && !tile.isRed()) tiles.put(tile, 4);
                        if (tile.getGroup() == DRAGON && !tile.isRed()) tiles.put(tile, 4);
                    }
                    if (redDoraCountIn > 0 && redDoraCountIn <= 12) {
                        int m5rCount = redDoraCountIn / 3 + (redDoraCountIn % 3 == 1 ? 1 : 0);
                        int p5rCount = redDoraCountIn / 3 + (redDoraCountIn % 3 == 2 ? 1 : 0);
                        int s5rCount = redDoraCountIn / 3;
                        if (m5rCount > 0) {
                            tiles.put(M5, 4 - m5rCount);
                            tiles.put(M5R, m5rCount);
                        }
                        if (p5rCount > 0) {
                            tiles.put(P5, 4 - p5rCount);
                            tiles.put(P5R, p5rCount);
                        }
                        if (s5rCount > 0) {
                            tiles.put(S5, 4 - s5rCount);
                            tiles.put(S5R, s5rCount);
                        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameType gameType = (GameType) o;

        if (playerCount != gameType.playerCount) return false;
        if (length != gameType.length) return false;
        if (redDoraCount != gameType.redDoraCount) return false;
        return region == gameType.region;

    }

    @Override
    public int hashCode() {
        int result = region != null ? region.hashCode() : 0;
        result = 31 * result + playerCount;
        result = 31 * result + length;
        result = 31 * result + redDoraCount;
        return result;
    }

}
