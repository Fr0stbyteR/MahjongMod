package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.HandTiles;

import java.util.ArrayList;
import java.util.HashMap;

public class WinningHand {
    private Status status;
    private Player player;
    private ArrayList<HashMap<EnumWinningHand, Integer>> hands;
    private EnumTile extraTile;
    private int fan;
    private int fu;
    private int score;
    private ArrayList<HandTiles<HandTiles>> tiles;
    WinningHand(Status statusIn, Player playerIn, ArrayList<HashMap<EnumWinningHand, Integer>> handsIn, int fanIn, int fuIn, int scoreIn, ArrayList<HandTiles<HandTiles>> tilesIn, EnumTile extraTileIn) {
        status = statusIn;
        player = playerIn;
        hands = handsIn;
        fan = fanIn;
        fu = fuIn;
        score = scoreIn;
        tiles = tilesIn;
        extraTile = extraTileIn;
    }

    public WinningHand analyze(GameState state, Player playerIn, ArrayList<HandTiles<HandTiles>> tilesIn, EnumTile extraTileIn) {
        Status status = Status.NOTEN;
        ArrayList<HashMap<EnumWinningHand, Integer>> hands = new ArrayList<HashMap<EnumWinningHand, Integer>>();
        int fan = 0;
        int fu = 0;
        int score = 0;
        // YakuMan
        // TODO
        return new WinningHand(status, playerIn, hands, fan, fu, score, tilesIn, extraTileIn);
    }


    public Status getStatus() {
        return status;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<HashMap<EnumWinningHand, Integer>> getHands() {
        return hands;
    }

    public int getFan() {
        return fan;
    }

    public int getFu() {
        return fu;
    }

    public ArrayList<HandTiles<HandTiles>> getTiles() {
        return tiles;
    }

    public EnumTile getExtraTile() {
        return extraTile;
    }

    public int getScore() {
        return score;
    }

    public enum Status {WIN, TEN, NOTEN}
}
