package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalyzeResult {
    WinningHand.Status status;
    HashMap<ArrayList<EnumTile>, EnumTile> waitDrop;
    EnumWinningHand winningHand;
    int fan;

    public AnalyzeResult(WinningHand.Status statusIn, HashMap<ArrayList<EnumTile>, EnumTile> waitDropIn, EnumWinningHand winningHandIn, int fanIn) {
        status = statusIn;
        waitDrop = waitDropIn;
        winningHand = winningHandIn;
        fan = fanIn;
    }

    public WinningHand.Status getStatus() {
        return status;
    }

    public HashMap<ArrayList<EnumTile>, EnumTile> getWaitDrop() {
        return waitDrop;
    }

    public EnumWinningHand getWinningHand() {
        return winningHand;
    }

    public int getFan() {
        return fan;
    }
}
