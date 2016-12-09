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
    private boolean isChiitoitsu;

    public Hand() {
        hasGet = false;
        hasEye = false;
        tileEye = null;
        countKita = 0;
        isChiitoitsu = false;
        tiles = new ArrayList<HandTiles>(Collections.singletonList(new Handing()));
        tileGang = new ArrayList<EnumTile>();
        tilePeng = new ArrayList<EnumTile>();
        tileChi = new ArrayList<EnumTile>();
        tileAnGang = new ArrayList<EnumTile>();
        tileKe = new ArrayList<EnumTile>();
        tileShun = new ArrayList<EnumTile>();
    }

    public boolean isChiitoitsu() {
        return isChiitoitsu;
    }

    public void setChiitoitsu(boolean isChiitoitsuIn) {
        isChiitoitsu = isChiitoitsuIn;
    }

    public ArrayList<EnumTile> getHanding() {
        return tiles.get(0).getTiles();
    }

    public Hand setHanding(ArrayList<EnumTile> tilesIn) {
        ((Handing) tiles.get(0)).setTiles(tilesIn);
        return this;
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

    public Hand addToHanding(ArrayList<EnumTile> tilesIn) {
        ((Handing) tiles.get(0)).addAll(tilesIn).sort();
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

    public Hand handingToNormal() {
        ((Handing) tiles.get(0)).toNormal();
        return this;
    }

    public ArrayList<EnumTile> getHandingToNormal() {
        ArrayList<EnumTile> handing = (ArrayList<EnumTile>) tiles.get(0).getTiles().clone();
        for (int i = 0; i < handing.size(); i++) {
            handing.set(i, handing.get(i).getNormal());
        }
        return handing;
    }

    public boolean findGang(EnumTile tileIn) {
        int count = 0;
        for (EnumTile tile1 : getHanding()) {
            if (tileIn.getNormal() == tile1.getNormal()) count++;
        }
        return count == 3;
    }

    public Hand gang(EnumTile tileGot, int orientationIn, boolean plusGangIn) {
        EnumTile tile2 = tileGot.getNormal();
        EnumTile tile3 = tileGot.getNormal();
        EnumTile tile4 = tileGot.getNormal();
        if (!tileGot.isRed() && (tileGot.getRed() != null)) if (getHanding().contains(tileGot.getRed())) tile4 = tileGot.getRed();
        removeFromHanding(tile2).removeFromHanding(tile3).removeFromHanding(tile4);
        tiles.add(new Gang(tileGot, tile2, tile3, tile4, orientationIn, plusGangIn));
        tileGang.add(tileGot.getNormal());
        return this;
    }

    public boolean findPeng(EnumTile tileIn) {
        int count = 0;
        for (EnumTile tile1 : getHanding()) {
            if (tileIn.getNormal() == tile1.getNormal()) count++;
        }
        return count >= 2;
    }

    public Hand peng(EnumTile tileGot, int orientationIn) {
        EnumTile tile2 = tileGot.getNormal();
        EnumTile tile3 = tileGot.getNormal();
        if (!tileGot.isRed() && tileGot.getRed() != null) if (getHanding().contains(tileGot.getRed())) tile3 = tileGot.getRed();
        removeFromHanding(tile2).removeFromHanding(tile3);
        tiles.add(new Peng(tileGot, tile2, tile3, orientationIn));
        tilePeng.add(tileGot.getNormal());
        return this;
    }

    public ArrayList<EnumTile> findChi(EnumTile tileIn) {
        ArrayList<EnumTile> found = new ArrayList<EnumTile>();
        ArrayList<EnumTile> handing = getHandingToNormal();
        if (tileIn.getPrev() != null && tileIn.getPrev().getPrev() != null
                && handing.contains(tileIn.getPrev()) && handing.contains(tileIn.getPrev().getPrev()))
            found.add(tileIn.getPrev().getPrev());
        if (tileIn.getPrev() != null && tileIn.getNext() != null
                && handing.contains(tileIn.getPrev()) && handing.contains(tileIn.getNext()))
            found.add(tileIn.getPrev());
        if (tileIn.getNext() != null && tileIn.getNext().getNext()  != null
                && handing.contains(tileIn.getNext()) && handing.contains(tileIn.getNext().getNext() ))
            found.add(tileIn);
        for (int i = 0; i < found.size(); i++) {
            if (getHanding().contains(found.get(i).getRed())) found.set(i, found.get(i).getRed());
        }
        return found;
    }

    public Hand chi(EnumTile tile1, int orientationIn, EnumTile tileGotIn) {
        EnumTile tile2 = tile1.getNext();
        if (tile2 == null) return null;
        EnumTile tile3 = tile1.getNext().getNext();
        if (tile3 == null) return null;
        if (getHanding().contains(tile2.getRed())) tile2 = tile2.getRed();
        if (getHanding().contains(tile3.getRed())) tile3 = tile3.getRed();
        if (tileGotIn != tile1) removeFromHanding(tile1);
        if (tileGotIn != tile2) removeFromHanding(tile2);
        if (tileGotIn != tile3) removeFromHanding(tile3);
        tiles.add(new Chi(tile1, tile2, tile3, orientationIn, tileGotIn));
        tileChi.add(tile1.getNormal());
        return this;
    }

    public ArrayList<EnumTile> findAnGang() {
        ArrayList<EnumTile> found = new ArrayList<EnumTile>();
        ArrayList<EnumTile> hand = getHandingToNormal();
        EnumTile last = null;
        if (hasGet) hand.add(getGet().getNormal());
        for (EnumTile tile : hand) {
            if (last == tile || found.contains(tile)) continue;
            else last = tile;
            int count = 0;
            for (EnumTile tile1 : hand) {
                if (tile == tile1) count++;
            }
            if (count == 4) found.add(tile);
        }
        return found;
    }

    public Hand anGang(EnumTile tile1) {
        if (hasGet) addToHandingFromGet();
        EnumTile tile2 = tile1.getNormal();
        EnumTile tile3 = tile1.getNormal();
        EnumTile tile4 = tile1.getNormal();
        if (!tile1.isRed() && tile1.getRed() != null) if (getHanding().contains(tile1.getRed())) tile4 = tile1.getRed();
        removeFromHanding(tile1).removeFromHanding(tile2).removeFromHanding(tile3).removeFromHanding(tile4);
        tiles.add(new AnGang(tile1, tile2, tile3, tile4));
        tileAnGang.add(tile1.getNormal());
        return this;
    }

    public ArrayList<EnumTile> findPlusGang() {
        ArrayList<EnumTile> found = new ArrayList<EnumTile>();
        for (EnumTile tile : tilePeng) {
            if (getHandingToNormal().contains(tile)) found.add(tile);
            if (getGet() == tile) found.add(tile);
        }
        return found;
    }

    public Hand plusGang(EnumTile tileIn) {
        for (HandTiles handTiles : tiles) {
            if (handTiles instanceof Peng) {
                if (getGet() == tileIn || getHandingToNormal().contains(tileIn)) {
                    addToHandingFromGet();
                    tiles.remove(handTiles);
                    tilePeng.remove(tileIn.getNormal());
                    gang(tileIn, 0, true);
                    break;
                }
            }
        }
        return this;
    }

    public Hand ke(EnumTile tile1) {
        if (hasGet) addToHandingFromGet();
        EnumTile tile2 = tile1.getNormal();
        EnumTile tile3 = tile1.getNormal();
        if (!tile1.isRed() && tile1.getRed() != null) if (getHanding().contains(tile1.getRed())) tile3 = tile1.getRed();
        removeFromHanding(tile1).removeFromHanding(tile2).removeFromHanding(tile3);
        tiles.add(new Ke(tile1, tile2, tile3));
        tileKe.add(tile1.getNormal());
        return this;
    }

    public Hand shun(EnumTile tile1) {
        if (hasGet) addToHandingFromGet();
        EnumTile tile2 = tile1.getNext();
        if (tile2 == null) return null;
        EnumTile tile3 = tile1.getNext().getNext();
        if (tile3 == null) return null;
        if (getHanding().contains(tile2.getRed())) tile2 = tile2.getRed();
        if (getHanding().contains(tile3.getRed())) tile3 = tile3.getRed();
        removeFromHanding(tile1).removeFromHanding(tile2).removeFromHanding(tile3);
        tiles.add(new Shun(tile1, tile2, tile3));
        tileShun.add(tile1.getNormal());
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
                hasGet = false;
                return this;
            }
        }
        return this;
    }

    public boolean isMenzen() {
        return getCountGang() + getCountPeng() + getCountChi() == 0;
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

    public void setCountKita(int countKitaIn) {
        countKita = countKitaIn;
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

    public void setTileGang(ArrayList<EnumTile> tileGangIn) {
        tileGang = tileGangIn;
    }

    public ArrayList<EnumTile> getTilePeng() {
        return tilePeng;
    }

    public void setTilePeng(ArrayList<EnumTile> tilePengIn) {
        tilePeng = tilePengIn;
    }

    public ArrayList<EnumTile> getTileChi() {
        return tileChi;
    }

    public void setTileChi(ArrayList<EnumTile> tileChiIn) {
        tileChi = tileChiIn;
    }

    public ArrayList<EnumTile> getTileAnGang() {
        return tileAnGang;
    }

    public void setTileAnGang(ArrayList<EnumTile> tileAnGangIn) {
        tileAnGang = tileAnGangIn;
    }

    public ArrayList<EnumTile> getTileKe() {
        return tileKe;
    }

    public void setTileKe(ArrayList<EnumTile> tileKeIn) {
        tileKe = tileKeIn;
    }

    public ArrayList<EnumTile> getTileShun() {
        return tileShun;
    }

    public void setTileShun(ArrayList<EnumTile> tileShunIn) {
        tileShun = tileShunIn;
    }

    public EnumTile getTileEye() {
        return tileEye;
    }

    public void setTileEye(EnumTile tileEyeIn) {
        tileEye = tileEyeIn;
    }

    public ArrayList<HandTiles> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<HandTiles> tilesIn) {
        tiles = tilesIn;
    }

    public void setHasGet(boolean hasGetIn) {
        hasGet = hasGetIn;
    }

    public void setHasEye(boolean hasEyeIn) {
        hasEye = hasEyeIn;
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

    public String toString() { // without kita
        String all = "";
        for (HandTiles handTiles : tiles) {
            if (handTiles instanceof Handing) all += tilesToString(handTiles.getTiles());
            else if (handTiles instanceof Chi || handTiles instanceof Peng || handTiles instanceof Gang) {
                String direction = handTiles.getOrientation() == 1 ? "<" : handTiles.getOrientation() == 2 ? "^" : ">";
                all += " [" + direction + tilesToString(handTiles.getTiles()) + "]";
            }
            else if (handTiles instanceof Get) {
                all += " +" + tilesToString(handTiles.getTiles());
            }
            else all += " [" + tilesToString(handTiles.getTiles()) + "]";
        }
        return all;
    }

    public static String tilesToString(ArrayList<EnumTile> tiles) {
        String string = "";
        String sLastGroup = "";
        Collections.sort(tiles, EnumTile.tilesComparator);
        for (EnumTile tile : tiles) {
            if (!sLastGroup.equals(tile.name().substring(0, 1))) {
                if (!string.isEmpty()) string += sLastGroup.toLowerCase() + " ";
                sLastGroup = tile.name().substring(0, 1);
            }
            string += tile.name().substring(1);
        }
        string += sLastGroup.toLowerCase();
        return string;
    }
}
