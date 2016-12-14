package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.minecraft.util.EnumFacing;

import java.util.HashMap;

public interface UI {
    Game getGame();
    void setGame(Game gameIn);
    void dealOver(); //between each hand
    void nextDealOver(); //when player get a tile
    void options();
    void discard(Player playerIn); //discard phase
    void choose(Player playerIn, Player.Option option, EnumTile tileIn);
    void discard(Player playerIn, EnumTile tileIn); //discard act
    void choosed(Player playerIn, Player.Option optionIn);
    void requestConfirm();
    void showReport(Player playerIn, HashMap<Player, Integer> scoreChangeIn);
    void showReport(Game.Ryuukyoku ryuukyokuIn, HashMap<Player, Integer> scoreChangeIn);
    void showReport();
    void riichi(Player playerIn);
    void gameOver();
    void setPositions(HashMap<EnumFacing, String> playersIn);
    void newDora();
    void chooseInt(Player playerIn, int optionIn, EnumTile tileIn);

    void agari(Player playerIn);

    void hosted(boolean hostIn, Player playerIn);
}
