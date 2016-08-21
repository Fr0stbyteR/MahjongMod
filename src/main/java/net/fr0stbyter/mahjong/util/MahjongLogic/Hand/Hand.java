package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTileGroup;
import net.fr0stbyter.mahjong.util.MahjongLogic.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Hand implements Cloneable {
    private boolean hasGet;
    private ArrayList<EnumTile> tileGang;
    private ArrayList<EnumTile> tilePeng;
    private ArrayList<EnumTile> tileChi;
    private int countKita;
    private ArrayList<EnumTile> tileAnGang;
    private ArrayList<EnumTile> tileKe;
    private ArrayList<EnumTile> tileShun;
    private boolean hasEye;
    private EnumTile tileEye;
    private ArrayList<HandTiles> tiles;

    public Hand() {
        hasGet = false;
        hasEye = false;
        tileEye = null;
        countKita = 0;
        tiles = new ArrayList<HandTiles>(Collections.singletonList(new Handing()));
        tileGang = new ArrayList<EnumTile>();
        tilePeng = new ArrayList<EnumTile>();
        tileChi = new ArrayList<EnumTile>();
        tileAnGang = new ArrayList<EnumTile>();
        tileKe = new ArrayList<EnumTile>();
        tileShun = new ArrayList<EnumTile>();
    }

    public ArrayList<EnumTile> getHanding() {
        return tiles.get(0).getTiles();
    }

    public int getHandingCount() {
        return getHanding().size();
    }

    public ArrayList<EnumTile> getAll() { // without kita
        ArrayList<EnumTile> all = new ArrayList<EnumTile>();
        for (HandTiles handTiles : tiles) {
            if (!(handTiles instanceof Kita)) all.addAll(handTiles.getTiles());
        }
        Collections.sort(all, EnumTile.tilesComparator);
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

    public Hand addToHanding(EnumTile tileIn) {
        ((Handing) tiles.get(0)).add(tileIn).sort();
        return this;
    }

    public Hand addToHandingFromGet() {
        for (HandTiles handTiles : tiles) {
            if (handTiles instanceof Get) {
                addToHanding(handTiles.getTile());
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

    public Hand handingToNormal() {
        ((Handing) tiles.get(0)).toNormal();
        return this;
    }

    public Hand gang(EnumTile tileIn1, Player playerIn, boolean plusGangIn) {
        EnumTile tileIn2 = tileIn1.getNormal();
        EnumTile tileIn3 = tileIn1.getNormal();
        EnumTile tileIn4 = tileIn1.getNormal();
        if (!tileIn1.isRed() && (tileIn1.getRed() != null)) if (getHanding().contains(tileIn1.getRed())) tileIn4 = tileIn1.getRed();
        removeFromHanding(tileIn2).removeFromHanding(tileIn3).removeFromHanding(tileIn4);
        tiles.add(new Gang(tileIn1, tileIn2, tileIn3, tileIn4, playerIn, plusGangIn));
        tileGang.add(tileIn1.getNormal());
        return this;
    }

    public Hand peng(EnumTile tileIn1, Player playerIn) {
        EnumTile tileIn2 = tileIn1.getNormal();
        EnumTile tileIn3 = tileIn1.getNormal();
        if (!tileIn1.isRed() && (tileIn1.getRed() != null)) if (getHanding().contains(tileIn1.getRed())) tileIn3 = tileIn1.getRed();
        removeFromHanding(tileIn2).removeFromHanding(tileIn3);
        tiles.add(new Peng(tileIn1, tileIn2, tileIn3, playerIn));
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
            if (handTiles instanceof Get) return handTiles.getTile();
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
        if (getCountGang() + getCountPeng() + getCountChi() == 0) return true;
        else return false;
    }

    public boolean hasGet() {
        return hasGet;
    }

    public int getCountGang() {
        return getTileGang().size();
    }

    public int getCountPeng() {
        return getTilePeng().size();
    }

    public int getCountChi() {
        return getTileChi().size();
    }

    public int getCountKita() {
        return countKita;
    }

    public int getCountAnGang() {
        return getTileAnGang().size();
    }

    public int getCountKe() {
        return getTileKe().size();
    }

    public int getCountShun() {
        return getTileShun().size();
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

    public void setHasGet(boolean hasGetIn) {
        hasGet = hasGetIn;
    }

    public void setTileGang(ArrayList<EnumTile> tileGangIn) {
        tileGang = tileGangIn;
    }

    public void setTilePeng(ArrayList<EnumTile> tilePengIn) {
        tilePeng = tilePengIn;
    }

    public void setTileChi(ArrayList<EnumTile> tileChiIn) {
        tileChi = tileChiIn;
    }

    public void setCountKita(int countKitaIn) {
        countKita = countKitaIn;
    }

    public void setTileAnGang(ArrayList<EnumTile> tileAnGangIn) {
        tileAnGang = tileAnGangIn;
    }

    public void setTileKe(ArrayList<EnumTile> tileKeIn) {
        tileKe = tileKeIn;
    }

    public void setTileShun(ArrayList<EnumTile> tileShunIn) {
        tileShun = tileShunIn;
    }

    public void setHasEye(boolean hasEyeIn) {
        hasEye = hasEyeIn;
    }

    public void setTileEye(EnumTile tileEyeIn) {
        tileEye = tileEyeIn;
    }

    public void setTiles(ArrayList<HandTiles> tilesIn) {
        tiles = tilesIn;
    }

    public boolean equals(Hand hand) {
        return hand != null
                && getAll().equals(hand.getAll())
                && tileAnGang.equals(hand.getTileAnGang())
                && tileGang.equals(hand.getTileGang())
                && tileKe.equals(hand.getTileKe())
                && tilePeng.equals(hand.getTilePeng())
                && tileShun.equals(hand.getTileShun())
                && tileChi.equals(hand.getTileChi())
                && tileEye == hand.getTileEye()
                && getGet() == hand.getGet();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Hand hand = (Hand) super.clone();
        hand.setTileGang((ArrayList<EnumTile>) tileGang.clone());
        hand.setTilePeng((ArrayList<EnumTile>) tilePeng.clone());
        hand.setTileChi((ArrayList<EnumTile>) tileChi.clone());
        hand.setTileAnGang((ArrayList<EnumTile>) tileAnGang.clone());
        hand.setTileKe((ArrayList<EnumTile>) tileKe.clone());
        hand.setTileShun((ArrayList<EnumTile>) tileShun.clone());
        ArrayList<HandTiles> newTiles = new ArrayList<HandTiles>();
        for (HandTiles handTiles : tiles) {
            newTiles.add((HandTiles) handTiles.clone());
        }
        hand.setTiles(newTiles);
        return hand;
    }

}
