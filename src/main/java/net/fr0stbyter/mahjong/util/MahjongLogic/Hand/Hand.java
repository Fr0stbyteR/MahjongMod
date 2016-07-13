package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTileGroup;
import net.fr0stbyter.mahjong.util.MahjongLogic.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Hand implements Cloneable {
    private boolean hasGet = false;
    private int countGang = 0;
    private ArrayList<EnumTile> tileGang = new ArrayList<EnumTile>();
    private int countPeng = 0;
    private ArrayList<EnumTile> tilePeng = new ArrayList<EnumTile>();
    private int countChi = 0;
    private ArrayList<EnumTile> tileChi = new ArrayList<EnumTile>();
    private int countKita = 0;
    private int countAnGang = 0;
    private ArrayList<EnumTile> tileAnGang = new ArrayList<EnumTile>();
    private int countKe = 0;
    private ArrayList<EnumTile> tileKe = new ArrayList<EnumTile>();
    private int countShun = 0;
    private ArrayList<EnumTile> tileShun = new ArrayList<EnumTile>();
    private boolean hasEye = false;
    private EnumTile tileEye = null;
    private ArrayList<HandTiles> tiles = new ArrayList<HandTiles>(Collections.singleton(new Handing()));

    public ArrayList<EnumTile> getHanding() {
        return ((Handing) tiles.get(0)).getTiles();
    }

    public int getHandingCount() {
        return getHanding().size();
    }

    public ArrayList<EnumTile> getAll() { // without kita
        ArrayList<EnumTile> all = new ArrayList<EnumTile>();
        all.addAll(getHanding());
        for (HandTiles handTiles : tiles) {
            if (handTiles instanceof Gang) all.addAll(((Gang) handTiles).getTiles());
            else if (handTiles instanceof Peng) all.addAll(((Peng) handTiles).getTiles());
            else if (handTiles instanceof Chi) all.addAll(((Chi) handTiles).getTiles());
            else if (handTiles instanceof AnGang) all.addAll(((AnGang) handTiles).getTiles());
            else if (handTiles instanceof Ke) all.addAll(((Ke) handTiles).getTiles());
            else if (handTiles instanceof Shun) all.addAll(((Shun) handTiles).getTiles());
            else if (handTiles instanceof Eye) all.addAll(((Eye) handTiles).getTiles());
            else if (handTiles instanceof Get) all.add(((Get) handTiles).getTile());
        }
        return all;
    }

    public ArrayList<EnumTile> getHandingByGroup(EnumTileGroup group) {
        ArrayList<EnumTile> all = getHanding();
        ArrayList<EnumTile> result = new ArrayList<EnumTile>();
        for (EnumTile tile : all) {
            if (tile.getGroup() == group) result.add(tile);
        }
        return result;
    }

    public ArrayList<HandTiles> addToHanding(EnumTile tileIn) {
        ((Handing) tiles.get(0)).add(tileIn).sort();
        return tiles;
    }

    public Hand addToHandingFromGet() {
        for (HandTiles handTiles : tiles) {
            if (handTiles instanceof Get) {
                addToHanding(((Get) handTiles).getTile());
                tiles.remove(handTiles);
                break;
            }
        }
        hasGet = false;
        return this;
    }

    public Hand removeFromHanding(EnumTile tileIn) {
        getHanding().remove(tileIn);
        return this;
    }

    public Hand setHanding(ArrayList<EnumTile> tilesIn) {
        ((Handing) tiles.get(0)).setTiles(tilesIn);
        return this;
    }

    public Hand gang(EnumTile tileIn1, Player playerIn, boolean plusGangIn) {
        EnumTile tileIn2 = tileIn1.getNormal();
        EnumTile tileIn3 = tileIn1.getNormal();
        EnumTile tileIn4 = tileIn1.getNormal();
        if (!tileIn1.isRed() && (tileIn1.getRed() != null)) if (getHanding().contains(tileIn1.getRed())) tileIn4 = tileIn1.getRed();
        removeFromHanding(tileIn2).removeFromHanding(tileIn3).removeFromHanding(tileIn4);
        tiles.add(new Gang(tileIn1, tileIn2, tileIn3, tileIn4, playerIn, plusGangIn));
        countGang++;
        tileGang.add(tileIn1.getNormal());
        return this;
    }

    public Hand peng(EnumTile tileIn1, Player playerIn) {
        EnumTile tileIn2 = tileIn1.getNormal();
        EnumTile tileIn3 = tileIn1.getNormal();
        if (!tileIn1.isRed() && (tileIn1.getRed() != null)) if (getHanding().contains(tileIn1.getRed())) tileIn3 = tileIn1.getRed();
        removeFromHanding(tileIn2).removeFromHanding(tileIn3);
        tiles.add(new Peng(tileIn1, tileIn2, tileIn3, playerIn));
        countPeng++;
        tilePeng.add(tileIn1.getNormal());
        return this;
    }

    public Hand chi(EnumTile tileIn2, Player playerIn) {
        EnumTile tileIn1 = tileIn2.getPrev();
        EnumTile tileIn3 = tileIn2.getNext();
        if ((tileIn1.getRed() != null)) if (getHanding().contains(tileIn1.getRed())) tileIn1 = tileIn1.getRed();
        if ((tileIn3.getRed() != null)) if (getHanding().contains(tileIn3.getRed())) tileIn3 = tileIn3.getRed();
        removeFromHanding(tileIn1).removeFromHanding(tileIn3);
        tiles.add(new Chi(tileIn1, tileIn2, tileIn3, playerIn));
        countChi++;
        tileChi.add(tileIn1.getNormal());
        return this;
    }

    public Hand anGang(EnumTile tileIn1) {
        if (hasGet) addToHandingFromGet();
        EnumTile tileIn2 = tileIn1.getNormal();
        EnumTile tileIn3 = tileIn1.getNormal();
        EnumTile tileIn4 = tileIn1.getNormal();
        if (!tileIn1.isRed() && tileIn1.getRed() != null) if (getHanding().contains(tileIn1.getRed())) tileIn4 = tileIn1.getRed();
        removeFromHanding(tileIn1).removeFromHanding(tileIn2).removeFromHanding(tileIn3).removeFromHanding(tileIn4);
        tiles.add(new AnGang(tileIn1, tileIn2, tileIn3, tileIn4));
        countAnGang++;
        tileAnGang.add(tileIn1.getNormal());
        return this;
    }

    public Hand ke(EnumTile tileIn1) {
        if (hasGet) addToHandingFromGet();
        EnumTile tileIn2 = tileIn1.getNormal();
        EnumTile tileIn3 = tileIn1.getNormal();
        if (!tileIn1.isRed() && tileIn1.getRed() != null) if (getHanding().contains(tileIn1.getRed())) tileIn3 = tileIn1.getRed();
        removeFromHanding(tileIn1).removeFromHanding(tileIn2).removeFromHanding(tileIn3);
        tiles.add(new Ke(tileIn1, tileIn2, tileIn3));
        countKe++;
        tileKe.add(tileIn1.getNormal());
        return this;
    }

    public Hand shun(EnumTile tileIn1) {
        if (hasGet) addToHandingFromGet();
        EnumTile tileIn2 = tileIn1.getNext().getNext();
        EnumTile tileIn3 = tileIn1.getNext();
        if ((tileIn2.getRed() != null)) if (getHanding().contains(tileIn2.getRed())) tileIn2 = tileIn2.getRed();
        if ((tileIn3.getRed() != null)) if (getHanding().contains(tileIn3.getRed())) tileIn3 = tileIn3.getRed();
        removeFromHanding(tileIn1).removeFromHanding(tileIn2).removeFromHanding(tileIn3);
        tiles.add(new Shun(tileIn1, tileIn2, tileIn3));
        countShun++;
        tileShun.add(tileIn1.getNormal());
        return this;
    }

    public Hand kita() {
        if (getGet() == EnumTile.F4) addToHandingFromGet();
        removeFromHanding(EnumTile.F4);
        tiles.add(new Kita());
        countKita++;
        return this;
    }

    public Hand eye(EnumTile tileIn1) {
        if (hasGet) addToHandingFromGet();
        EnumTile tileIn2 = tileIn1.getNormal();
        if (!tileIn1.isRed() && tileIn1.getRed() != null) if (getHanding().contains(tileIn1.getRed())) tileIn2 = tileIn1.getRed();
        removeFromHanding(tileIn1).removeFromHanding(tileIn2);
        tiles.add(new Eye(tileIn1, tileIn2));
        hasEye = true;
        tileEye = tileIn1.getNormal();
        return this;
    }

    public Hand get(EnumTile tileIn) {
        if (hasGet) addToHandingFromGet();
        tiles.add(new Get(tileIn));
        hasGet = true;
        return this;
    }

    public EnumTile getGet() {
        for (HandTiles handTiles : tiles) {
            if (handTiles instanceof Get) return ((Get) handTiles).getTile();
        }
        return null;
    }

    public Hand removeGet() {
        for (HandTiles handTiles : tiles) {
            if (handTiles instanceof Get) {
                tiles.remove(handTiles);
                return this;
            }
        }
        return this;
    }

    public boolean isMenzen() {
        if (countGang + countPeng + countChi == 0) return true;
        else return false;
    }

    public boolean hasGet() {
        return hasGet;
    }

    public int getCountGang() {
        return countGang;
    }

    public int getCountPeng() {
        return countPeng;
    }

    public int getCountChi() {
        return countChi;
    }

    public int getCountKita() {
        return countKita;
    }

    public int getCountAnGang() {
        return countAnGang;
    }

    public int getCountKe() {
        return countKe;
    }

    public int getCountShun() {
        return countShun;
    }

    public boolean hasEye() {
        return hasEye;
    }

    public ArrayList<EnumTile> getTileGang() {
        return tileGang;
    }

    public ArrayList<EnumTile> getTilePeng() {
        return tilePeng;
    }

    public ArrayList<EnumTile> getTileChi() {
        return tileChi;
    }

    public ArrayList<EnumTile> getTileAnGang() {
        return tileAnGang;
    }

    public ArrayList<EnumTile> getTileKe() {
        return tileKe;
    }

    public ArrayList<EnumTile> getTileShun() {
        return tileShun;
    }

    public EnumTile getTileEye() {
        return tileEye;
    }

    public ArrayList<HandTiles> getTiles() {
        return tiles;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
