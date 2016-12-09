package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Mountain {
    private Game game;
    private int playerCount;
    private int doorIndex; // first tile get by oya, next is getD doorIndex, next is getU doorIndex - 1
    private int nextIndex;
    private int countDora;
    private int[] rinshyanIndex;
    private int[] doraIndex;
    private ArrayList<MountainTile> mountainTilesU = new ArrayList<MountainTile>();
    private ArrayList<MountainTile> mountainTilesD = new ArrayList<MountainTile>();

    public boolean addU(EnumTile tileIn, EnumPosition positionIn) {
        return mountainTilesU.add(new MountainTile(tileIn, positionIn, false));
    }

    public boolean addD(EnumTile tileIn, EnumPosition positionIn) {
        return mountainTilesD.add(new MountainTile(tileIn, positionIn, false));
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

    Mountain(Game gameIn, long seedIn) {
        game = gameIn;
        countDora = 1;
        rinshyanIndex = new int[2];
        doraIndex = new int[5];
        playerCount = game.getGameType().getPlayerCount();
        ArrayList<EnumTile> tiles = game.getGameType().getTiles();
        Collections.shuffle(tiles, new Random(seedIn));
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

    public int getDoorIndex() {
        return doorIndex;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public void open(EnumPosition positionIn, int tileStackIn) {
        doorIndex = getIndexFromPosition(positionIn, getStacksCount(positionIn) - tileStackIn - 1);
        nextIndex = doorIndex;
        // set Rinshyan
        int openPointer = doorIndex;
        int maxPointer = mountainTilesU.size() - 1;
        if (openPointer == maxPointer) openPointer = 0;
        else openPointer++;
        rinshyanIndex[0] = openPointer;
        getTileU(openPointer).setProp(MountainTile.Prop.RINSHYAN);
        getTileD(openPointer).setProp(MountainTile.Prop.RINSHYAN);

        if (openPointer == maxPointer) openPointer = 0;
        else openPointer++;
        rinshyanIndex[1] = openPointer;
        getTileU(openPointer).setProp(MountainTile.Prop.RINSHYAN);
        getTileD(openPointer).setProp(MountainTile.Prop.RINSHYAN);
        // set Dora Ura
        for (int i = 0; i < 5; i++) {
            if (openPointer == maxPointer) openPointer = 0;
            else openPointer++;
            doraIndex[i] = openPointer;
            getTileU(openPointer).setProp(MountainTile.Prop.DORA);
            if (i == 0) getTileU(openPointer).setShown(true);
            getTileD(openPointer).setProp(MountainTile.Prop.URA);
        }
        // set Haitei
        if (openPointer == maxPointer) openPointer = 0;
        else openPointer++;
        getTileD(openPointer).setProp(MountainTile.Prop.HAITEI);
        getTileU(openPointer).setProp(MountainTile.Prop.HAITEI);
        if (openPointer == maxPointer) openPointer = 0;
        else openPointer++;
        getTileD(openPointer).setProp(MountainTile.Prop.HAITEI);
        if (playerCount == 4) getTileU(openPointer).setProp(MountainTile.Prop.HAITEI);

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
        if (!mountainTilesU.get(tileIndexIn).isNull()) return mountainTilesU.get(tileIndexIn);
        if (!mountainTilesD.get(tileIndexIn).isNull()) return mountainTilesD.get(tileIndexIn);
        return null;
    }

    public MountainTile getNext() {
        return getTile(nextIndex);
    }

    public MountainTile.Prop getNextProp() {
        return getNext().getProp();
    }

    public Mountain removeNext() {
        if (!mountainTilesU.get(nextIndex).isNull()) mountainTilesU.get(nextIndex).setNull();
        else if (!mountainTilesD.get(nextIndex).isNull()) {
            mountainTilesD.get(nextIndex).setNull();
            if (nextIndex == 0) nextIndex = mountainTilesU.size() - 1;
            else nextIndex--;
        }
        return this;
    }

    public EnumTile getNextThenRemove() {
        if (getNextProp() == MountainTile.Prop.HAITEI) game.getGameState().setHaitei(true);
        if (getNextProp() == MountainTile.Prop.DORA) return null;
        EnumTile tile = getNext().getTile();
        removeNext();
        return tile;
    }

    public EnumTile getNextRinshyanThenRemove() {
        EnumTile tile = getNextRinshyan();
        removeNextRinshyan();
        return tile;
    }

    public MountainTile getTileU(int tileIndexIn) {
        if (!mountainTilesU.get(tileIndexIn).isNull()) return mountainTilesU.get(tileIndexIn);
        return null;
    }

    public MountainTile getTileD(int tileIndexIn) {
        if (!mountainTilesD.get(tileIndexIn).isNull()) return mountainTilesD.get(tileIndexIn);
        return null;
    }

    public Mountain removeTile(int tileIndexIn) {
        if (!mountainTilesU.get(tileIndexIn).isNull()) mountainTilesU.get(tileIndexIn).setNull();
        else if (!mountainTilesD.get(tileIndexIn).isNull()) mountainTilesD.get(tileIndexIn).setNull();
        return this;
    }

    public Mountain removeTileU(int tileIndexIn) {
        if (!mountainTilesU.get(tileIndexIn).isNull()) mountainTilesU.get(tileIndexIn).setNull();
        return this;
    }

    public Mountain removeTileD(int tileIndexIn) {
        if (!mountainTilesD.get(tileIndexIn).isNull()) mountainTilesD.get(tileIndexIn).setNull();
        return this;
    }

    public EnumTile getNextRinshyan() {
        if (!getTileU(rinshyanIndex[0]).isNull()) return getTileU(rinshyanIndex[0]).getTile();
        if (!getTileD(rinshyanIndex[0]).isNull()) return getTileD(rinshyanIndex[0]).getTile();
        if (!getTileU(rinshyanIndex[1]).isNull()) return getTileU(rinshyanIndex[1]).getTile();
        if (!getTileD(rinshyanIndex[1]).isNull()) return getTileD(rinshyanIndex[1]).getTile();
        return null;
    }

    public Mountain removeNextRinshyan() {
        if (!getTileU(rinshyanIndex[0]).isNull()) removeTileU(rinshyanIndex[0]);
        else if (!getTileD(rinshyanIndex[0]).isNull()) removeTileD(rinshyanIndex[0]);
        else if (!getTileU(rinshyanIndex[1]).isNull()) removeTileU(rinshyanIndex[1]);
        else if (!getTileD(rinshyanIndex[1]).isNull()) removeTileD(rinshyanIndex[1]);
        return this;
    }

    public int getCountDora() {
        return countDora;
    }

    public ArrayList<EnumTile> getDora() {
        ArrayList<EnumTile> tiles = new ArrayList<EnumTile>();
        for (int i = 0; i < countDora; i++) {
            tiles.add(getTileU(doraIndex[i]).getTile().getDora(playerCount));
        }
        return tiles;
    }

    public ArrayList<EnumTile> getUra() {
        ArrayList<EnumTile> tiles = new ArrayList<EnumTile>();
        for (int i = 0; i < countDora; i++) {
            tiles.add(getTileD(doraIndex[i]).getTile().getDora(playerCount));
        }
        return tiles;
    }

    public Mountain extraDora() {
        countDora++;
        getTileU(doraIndex[countDora - 1]).setShown(true);
        game.getUi().newDora();
        return this;
    }

    public boolean isDora(EnumTile enumtile) {
        return getDora().contains(enumtile);
    }

    public int[] getAvailableCount() {
        int count[] = {0, 0, 0, 0, 0};
        for (MountainTile mountainTile : mountainTilesU) {
            if (mountainTile.isNull() || mountainTile.getProp() != MountainTile.Prop.NORMAL) continue;
            if (mountainTile.getPosition() == EnumPosition.EAST) count[1]++;
            if (mountainTile.getPosition() == EnumPosition.SOUTH) count[2]++;
            if (mountainTile.getPosition() == EnumPosition.WEST) count[3]++;
            if (mountainTile.getPosition() == EnumPosition.NORTH) count[4]++;
        }
        for (MountainTile mountainTile : mountainTilesD) {
            if (mountainTile.isNull() || mountainTile.getProp() != MountainTile.Prop.NORMAL) continue;
            if (mountainTile.getPosition() == EnumPosition.EAST) count[1]++;
            if (mountainTile.getPosition() == EnumPosition.SOUTH) count[2]++;
            if (mountainTile.getPosition() == EnumPosition.WEST) count[3]++;
            if (mountainTile.getPosition() == EnumPosition.NORTH) count[4]++;
        }
        count[0] = count[1] + count[2] + count[3] + count[4];
        return count;
    }
}
