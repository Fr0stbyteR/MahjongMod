package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.Collections;

public class Mountain {
    private ArrayList<MountainTile> mountainTiles = new ArrayList<MountainTile>();
    public boolean add(EnumTile tileIn, EnumPosition positionIn) {
        return mountainTiles.add(new MountainTile(tileIn, positionIn, false));
    }
    public ArrayList<MountainTile> getTilesFromPosition(EnumPosition enumPosition) {
        ArrayList<MountainTile> tiles = new ArrayList<MountainTile>();
        for (MountainTile mountainTile : mountainTiles) {
            if (mountainTile.getPosition() == enumPosition) tiles.add(mountainTile);
        }
        return tiles;
    }
    Mountain(GameType gameType) {
        int playerCount = gameType.getPlayerCount();
        ArrayList<EnumTile> tiles = gameType.getTiles();
        Collections.shuffle(tiles);
        for (int i = 0; i < (playerCount == 3 ? 14 : 17); i++) {
            add(tiles.get(0), EnumPosition.NORTH);
            tiles.remove(0);
            add(tiles.get(0), EnumPosition.NORTH);
            tiles.remove(0);
        }
        for (int i = 0; i < (playerCount == 3 ? 13 : 17); i++) {
            add(tiles.get(0), EnumPosition.WEST);
            tiles.remove(0);
            add(tiles.get(0), EnumPosition.WEST);
            tiles.remove(0);
        }
        for (int i = 0; i < (playerCount == 3 ? 14 : 17); i++) {
            add(tiles.get(0), EnumPosition.SOUTH);
            tiles.remove(0);
            add(tiles.get(0), EnumPosition.SOUTH);
            tiles.remove(0);
        }
        for (int i = 0; i < (playerCount == 3 ? 13 : 17); i++) {
            add(tiles.get(0), EnumPosition.EAST);
            tiles.remove(0);
            add(tiles.get(0), EnumPosition.EAST);
            tiles.remove(0);
        }
    }
}
