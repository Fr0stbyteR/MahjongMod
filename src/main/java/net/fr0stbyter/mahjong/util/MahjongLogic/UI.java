package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.HashMap;

public interface UI {
    Game getGame();
    void setGame(Game gameIn);
    void dealOver();
    void nextDealOver();
    void options();
    void melded();
    void requestConfirm();
    void showReport(Player playerIn, HashMap<Player, Integer> scoreChangeIn);
    void showReport(Game.Ryuukyoku ryuukyokuIn, HashMap<Player, Integer> scoreChangeIn);
    void showReport();
}
