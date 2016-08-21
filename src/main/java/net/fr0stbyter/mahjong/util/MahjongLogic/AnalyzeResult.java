package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalyzeResult {
    WinningHand.HandStatus handStatus;
    HashMap<EnumTile, ArrayList<EnumTile>> dropWait;
    EnumWinningHand winningHand;
    int fan;

    public AnalyzeResult(WinningHand.HandStatus handStatusIn, HashMap<EnumTile, ArrayList<EnumTile>> dropWaitIn, EnumWinningHand winningHandIn, int fanIn) {
        handStatus = handStatusIn;
        dropWait = dropWaitIn;
        winningHand = winningHandIn;
        fan = fanIn;
    }

    public WinningHand.HandStatus getHandStatus() {
        return handStatus;
    }

    public HashMap<EnumTile, ArrayList<EnumTile>> getDropWait() {
        return dropWait;
    }

    public EnumWinningHand getWinningHand() {
        return winningHand;
    }

    public int getFan() {
        return fan;
    }
}
