package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ConsoleUI implements UI {
    private Game game;
    private Scanner scanner;

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game gameIn) {
        game = gameIn;
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ArrayList<String> playersID = new ArrayList<String>();
        playersID.add("ID1");
        playersID.add("ID2");
        playersID.add("ID3");
        GameType gameType = new GameType(GameType.GameRegion.JAPAN, 3, 1, 3);
        Game game = new Game(playersID, gameType, new ConsoleUI());
        /*ArrayList<EnumTile> doraIn = game.getMountain().getDora();
        ArrayList<EnumTile> uraIn = game.getMountain().getUra();
        GameState gameState = game.getGameState();
        Player player = game.getPlayers().get(1);
        Hand hand = new Hand();
        hand.addToHanding(EnumTile.S1)
                .addToHanding(EnumTile.S2)
                .addToHanding(EnumTile.S3)
                .addToHanding(EnumTile.M1)
                .addToHanding(EnumTile.M2)
                .addToHanding(EnumTile.M3)
                .addToHanding(EnumTile.P1)
                .addToHanding(EnumTile.P2)
                .addToHanding(EnumTile.P3)
                .addToHanding(EnumTile.F1)
                .addToHanding(EnumTile.F1)
                .addToHanding(EnumTile.F1)
                .addToHanding(EnumTile.D3);
        //HashMap<EnumTile, ArrayList<EnumTile>> ten = baseAnalyzeTen(hand, EnumTile.S1);
        //hand.peng(EnumTile.F2, game.getPlayers().get(0));
        hand.addToHanding(EnumTile.F4).kita();
        //player.setRiichi(true);
        //EnumTile extraTile = EnumTile.D3;
        hand.get(EnumTile.D3);
        EnumTile extraTile = null;
        System.out.print("\nhanding:" + hand.getAll());
        gameState.setCurPlayer(EnumPosition.SOUTH);
        gameState.setCurDeal(3);
        WinningHand winningHand = Analyze.analyzeWin(gameType, gameState, player, doraIn, uraIn, hand, extraTile);
        System.out.print("\nRES:" + winningHand);
        for (AnalyzeResult analyzeResult : winningHand.getyakuList()) {
            if (analyzeResult.getHandStatus() == WinningHand.HandStatus.WIN) System.out.print("\nRES:" + analyzeResult.getWinningHand() + " " + analyzeResult.getFan());
        }
        System.out.print("\nFU:" + winningHand.getFu().getCount());
        System.out.print("\nFAN:" + winningHand.getFan());
        System.out.print("\nSCORE:" + winningHand.getScoreLevel() + " " + winningHand.getScore());
        System.out.print("\nPLAYER1:" + player.getId() + " " + player.isOya() + " " + player.getCurWind());
        System.out.print("\nDORA:" + doraIn);*/

    }

    @Override
    public void dealOver() {
        printGameState();
        printTable();
        discard(game.getPlayer(game.getGameState().getCurPlayer()));
    }

    @Override
    public void nextDealOver() {
        dealOver();
    }

    public void printTable() {
        for (Player player : game.getPlayers()) {
            System.out.print(player.getId() + " " + player.getCurWind() + " " + player.getScore() + "\n" + player.getHand().toString() + "\n");
        }
    }

    public void printGameState() {
        System.out.print(game.getGameState().getCurRound().getName() + game.getGameState().getCurHand() + " " + game.getGameState().getCurExtra() + "\n");
    }

    public void discard(Player playerIn) {
        EnumTile tileDiscard = null;
        HashMap<Player.Options, ArrayList<EnumTile>> options = game.getPlayer(game.getGameState().getCurPlayer()).getOptions();
        if (!options.isEmpty()) {
            for (Player.Options option : options.keySet()) {
                System.out.print("Option: " + option.name().toLowerCase() + (options.get(option) != null ? options.get(option) : "") + "\n");
            }
        }
        //todo option
        while (!playerIn.getHand().getHanding().contains(tileDiscard) && tileDiscard != playerIn.getHand().getGet()) {
            tileDiscard = scanTile();
        }
        System.out.print(playerIn.getId() + " discards " + tileDiscard + "\n");
        playerIn.discard(tileDiscard);
    }

    public EnumTile scanTile() {
        String sTile = "";
        while (EnumTile.getTile(sTile) == null) {
            System.out.print(game.getGameState().getCurPlayer() + ": ");
            sTile = scanner.nextLine();
            System.out.print("input " + EnumTile.getTile(sTile) + "\n");
        }
        return EnumTile.getTile(sTile);
    }
}
