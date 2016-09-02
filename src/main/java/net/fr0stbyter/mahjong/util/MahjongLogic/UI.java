package net.fr0stbyter.mahjong.util.MahjongLogic;

public interface UI {
    Game getGame();
    void setGame(Game gameIn);
    void dealOver();
    void nextDealOver();
}
