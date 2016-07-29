package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.Collections;

public class Mountain {
    private int playerCount;
    private int doorIndex; // first tile get by oya, next is getD doorIndex, next is getU doorIndex - 1
    private int nextIndex;
    private ArrayList<MountainTile> mountainTilesU = new ArrayList<MountainTile>();
    private ArrayList<MountainTile> mountainTilesD = new ArrayList<MountainTile>();

    public boolean addU(EnumTile tileIn, EnumPosition positionIn) {
        return mountainTilesU.add(new MountainTile(tileIn, positionIn, false));
    }

    public boolean addD(EnumTile tileIn, EnumPosition positionIn) {
        return mountainTilesU.add(new MountainTile(tileIn, positionIn, false));
    }

    public ArrayList<MountainTile> getTilesUFromPosition(EnumPosition enumPosition) {
        ArrayList<MountainTile> tiles = new ArrayList<MountainTile>();
        for (MountainTile mountainTile : mountainTilesU) {
            if (mountainTile.getPosition() == enumPosition) tiles.add(mountainTile);
        }
        return tiles;
    }

    public ArrayList<MountainTile> getTilesDFromPosition(EnumPosition enumPosition) {
        ArrayList<MountainTile> tiles = new ArrayList<MountainTile>();
        for (MountainTile mountainTile : mountainTilesD) {
            if (mountainTile.getPosition() == enumPosition) tiles.add(mountainTile);
        }
        return tiles;
    }

    Mountain(GameType gameType) {
        playerCount = gameType.getPlayerCount();
        ArrayList<EnumTile> tiles = gameType.getTiles();
        Collections.shuffle(tiles);
        for (int i = 0; i < (playerCount == 3 ? 14 : 17); i++) { //counter-clockwise
            addU(tiles.get(0), EnumPosition.EAST);
            tiles.remove(0);
            addD(tiles.get(0), EnumPosition.EAST);
            tiles.remove(0);
        }
        for (int i = 0; i < (playerCount == 3 ? 13 : 17); i++) {
            addU(tiles.get(0), EnumPosition.NORTH);
            tiles.remove(0);
            addD(tiles.get(0), EnumPosition.NORTH);
            tiles.remove(0);
        }
        for (int i = 0; i < (playerCount == 3 ? 14 : 17); i++) {
            addU(tiles.get(0), EnumPosition.WEST);
            tiles.remove(0);
            addD(tiles.get(0), EnumPosition.WEST);
            tiles.remove(0);
        }
        for (int i = 0; i < (playerCount == 3 ? 13 : 17); i++) {
            addU(tiles.get(0), EnumPosition.SOUTH);
            tiles.remove(0);
            addD(tiles.get(0), EnumPosition.SOUTH);
            tiles.remove(0);
        }
    }

    public void open(EnumPosition positionIn, int tileStackIn) {
        doorIndex = getIndexFromPosition(positionIn, getStacksCount(positionIn) - tileStackIn - 1);
        nextIndex = doorIndex;
    }

    public int getStacksCount(EnumPosition positionIn) {
        if (playerCount == 4) return 17;
        if (positionIn == EnumPosition.NORTH || positionIn == EnumPosition.SOUTH) return 13;
        return 14;
    }

    public int getIndexFromPosition(EnumPosition positionIn, int tileIndexIn) {
        if (positionIn == EnumPosition.EAST) return tileIndexIn;
        if (positionIn == EnumPosition.NORTH) return playerCount == 3 ? tileIndexIn + 14 : tileIndexIn + 17;
        if (positionIn == EnumPosition.WEST) return playerCount == 3 ? tileIndexIn + 27 : tileIndexIn + 34;
        return playerCount == 3 ? tileIndexIn + 41 : tileIndexIn + 51;
    }

    public EnumPosition getPositionFromIndex(int tileIndexIn) {
        if (tileIndexIn <= (playerCount == 3 ? 14 : 17)) return EnumPosition.EAST;
        if (tileIndexIn <= (playerCount == 3 ? 27 : 34)) return EnumPosition.NORTH;
        if (tileIndexIn <= (playerCount == 3 ? 41 : 51)) return EnumPosition.WEST;
        return EnumPosition.SOUTH;
    }

    public int getSubIndexFromIndex(int tileIndexIn) {
        if (tileIndexIn <= (playerCount == 3 ? 14 : 17)) return tileIndexIn;
        if (tileIndexIn <= (playerCount == 3 ? 27 : 34)) return playerCount == 3 ? tileIndexIn - 14 : tileIndexIn - 17;
        if (tileIndexIn <= (playerCount == 3 ? 41 : 51)) return playerCount == 3 ? tileIndexIn - 27 : tileIndexIn - 34;
        return playerCount == 3 ? tileIndexIn - 41 : tileIndexIn - 51;
    }

    public MountainTile getTile(int tileIndexIn) {
        if (mountainTilesU.get(tileIndexIn) != null) return mountainTilesU.get(tileIndexIn);
        if (mountainTilesD.get(tileIndexIn) != null) return mountainTilesD.get(tileIndexIn);
        return null;
    }

    public MountainTile getNext() {
        return getTile(nextIndex);
    }

    public Mountain removeNext() {
        if (mountainTilesU.get(nextIndex) != null) mountainTilesU.set(nextIndex, null);
        else if (mountainTilesD.get(nextIndex) != null) {
            mountainTilesD.set(nextIndex, null);
            nextIndex--;
        }
        return this;
    }

    public EnumTile getNextThenRemove() {
        EnumTile tile = getNext().getTile();
        removeNext();
        return tile;
    }

    public MountainTile getTileU(int tileIndexIn) {
        if (mountainTilesU.get(tileIndexIn) != null) return mountainTilesU.get(tileIndexIn);
        return null;
    }

    public MountainTile getTileD(int tileIndexIn) {
        if (mountainTilesD.get(tileIndexIn) != null) return mountainTilesD.get(tileIndexIn);
        return null;
    }

    public Mountain removeTile(int tileIndexIn) {
        if (mountainTilesU.get(tileIndexIn) != null) mountainTilesU.set(tileIndexIn, null);
        else if (mountainTilesD.get(tileIndexIn) != null) mountainTilesD.set(tileIndexIn, null);
        return this;
    }

    public Mountain removeTileU(int tileIndexIn) {
        if (mountainTilesU.get(tileIndexIn) != null) mountainTilesU.set(tileIndexIn, null);
        return this;
    }

    public Mountain removeTileD(int tileIndexIn) {
        if (mountainTilesD.get(tileIndexIn) != null) mountainTilesD.set(tileIndexIn, null);
        return this;
    }

    public EnumTile getNextRinshyan() {
        for (int i = 0; i < 4; i++) {
            if (getRinshyan().get(i) != null) return getRinshyan().get(i);
        }
        return null;
    }

    public Mountain removeNextRinshyan() {
        if (getTileU(doorIndex + 1) != null) removeTileU(doorIndex + 1);
        else if (getTileD(doorIndex + 1) != null) removeTileD(doorIndex + 1);
        else if (getTileU(doorIndex + 2) != null) removeTileU(doorIndex + 2);
        else if (getTileD(doorIndex + 2) != null) removeTileD(doorIndex + 2);
        return this;
    }

    public ArrayList<EnumTile> getRinshyan() {
        ArrayList<EnumTile> tiles = new ArrayList<EnumTile>();
        tiles.add(getTileU(doorIndex + 1).getTile());
        tiles.add(getTileD(doorIndex + 1).getTile());
        tiles.add(getTileU(doorIndex + 2).getTile());
        tiles.add(getTileD(doorIndex + 2).getTile());
        return tiles;
    }

    public ArrayList<EnumTile> getDora() {
        ArrayList<EnumTile> tiles = new ArrayList<EnumTile>();
        for (int i = 3; i < 7; i++) {
            tiles.add(getTileU(doorIndex + i).getTile());
        }
        return tiles;
    }

    public ArrayList<EnumTile> getUra() {
        ArrayList<EnumTile> tiles = new ArrayList<EnumTile>();
        for (int i = 3; i < 7; i++) {
            tiles.add(getTileD(doorIndex + i).getTile());
        }
        return tiles;
    }
}
