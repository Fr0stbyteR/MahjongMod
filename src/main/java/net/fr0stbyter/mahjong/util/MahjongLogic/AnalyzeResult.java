package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalyzeResult {
    WinningHand.HandStatus handStatus;
    HashMap<ArrayList<EnumTile>, EnumTile> waitDrop;
    EnumWinningHand winningHand;
    int fan;

    public AnalyzeResult(WinningHand.HandStatus handStatusIn, HashMap<ArrayList<EnumTile>, EnumTile> waitDropIn, EnumWinningHand winningHandIn, int fanIn) {
        handStatus = handStatusIn;
        waitDrop = waitDropIn;
        winningHand = winningHandIn;
        fan = fanIn;
    }

    public WinningHand.HandStatus getHandStatus() {
        return handStatus;
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
