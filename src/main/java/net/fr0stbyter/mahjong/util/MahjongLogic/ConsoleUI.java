package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.minecraft.util.EnumFacing;

import java.util.*;

public class ConsoleUI implements UI {
    private Game game;
    private Scanner scanner;

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setPositions(HashMap<String, EnumFacing> playersIn) {

    }

    @Override
    public void newDora() {

    }

    @Override
    public void chooseInt(Player playerIn, int optionIn, EnumTile tileIn) {

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

    @Override
    public void options() {
        for (Player player : game.getPlayers()) {
            if (!player.getOptions().isEmpty()) {
                for (Player.Option option : player.getOptions().keySet()) {
                    System.out.print(player.getId() + " option: " + option.name().toLowerCase() + (player.getOptions().get(option) != null ? player.getOptions().get(option) : "") + "\n");
                }
                scanOption(player);
            }
        }
    }

    @Override
    public void choose(Player playerIn, Player.Option option, EnumTile enumTile) {

    }

    @Override
    public void discard(Player playerIn, EnumTile enumTile) {

    }

    @Override
    public void choosed(Player playerIn, Player.Option optionIn) {
        Player player = game.getPlayer(game.getGameState().getCurPlayer());
        printTable(player);
        discard(player);
    }

    @Override
    public void requestConfirm() {
        options();
    }

    @Override
    public void showReport(Player playerIn, HashMap<Player, Integer> scoreChangeIn) {
        WinningHand winningHand = playerIn.getWinningHand();
        System.out.print("AGARI: ");
        System.out.print(playerIn.getId() + " " + playerIn.getCurWind() + " " + playerIn.getHand().toString() + (winningHand.getIsTsumo() ? "\n" : " +" + game.getRiver().getLast().getTile().name().toLowerCase() + "\n"));
        for (AnalyzeResult analyzeResult : winningHand.getyakuList()) {
            if (analyzeResult.getHandStatus() == WinningHand.HandStatus.WIN) System.out.print(analyzeResult.getWinningHand() + " " + analyzeResult.getFan() + "fan\n");
        }
        System.out.print("DORA:" + game.getMountain().getDora() + "URA:" + game.getMountain().getUra() + "\n");
        System.out.print(winningHand.getFu().getCount() + " fu, " + winningHand.getFan() + " fan\n");
        System.out.print(winningHand.getScoreLevel() + " " + winningHand.getScore() + " \n");
        for (Player player : game.getPlayers()) {
            System.out.print(player.getId() + " " + player.getCurWind() + " " + (player.getScore() - scoreChangeIn.get(player)) + " " + scoreChangeIn.get(player) + "\n");
        }
    }

    @Override
    public void showReport(Game.Ryuukyoku ryuukyokuIn, HashMap<Player, Integer> scoreChangeIn) {
        System.out.print("RYUKYOKU: " + ryuukyokuIn.name());
        for (Player player : game.getPlayers()) {
            System.out.print(player.getId() + " " + player.getCurWind() + " " + player.getScore() + scoreChangeIn.get(player) + " " + scoreChangeIn.get(player) + "\n");
        }
    }

    @Override
    public void showReport() {
        for (Player player : game.getPlayers()) {
            System.out.print(player.getId() + " " + player.getCurWind() + " " + player.getScore() + "\n");
        }
    }

    @Override
    public void riichi(Player playerIn) {

    }

    @Override
    public void gameOver() {

    }

    public void printTable() {
        for (Player player : game.getPlayers()) {
            printTable(player);
        }
    }

    public void printTable(Player player) {
        System.out.print(player.getId() + " " + player.getCurWind() + " " + player.getScore() + (player.isRiichi() ? "riichi\n" : "\n") + player.getHand().toString() + "\n");
    }

    public void printGameState() {
        System.out.print(game.getGameState().getCurRound().getName() + game.getGameState().getCurHand() + " " + game.getGameState().getCurExtra() + "\n");
    }

    public void discard(Player playerIn) {
        EnumTile tileDiscard = null;
        HashMap<Player.Option, ArrayList<EnumTile>> options = game.getPlayer(game.getGameState().getCurPlayer()).getOptions();
        if (!options.isEmpty()) {
            for (Player.Option option : options.keySet()) {
                System.out.print("Option: " + option.name().toLowerCase() + (options.get(option) != null ? options.get(option) : "") + "\n");
            }
        }
        while (tileDiscard == null || !(playerIn.getHand().getHanding().contains(tileDiscard) || (playerIn.getHand().hasGet() && tileDiscard == playerIn.getHand().getGet()))) {
            tileDiscard = scan();
            if (tileDiscard == null) return;
        }
        System.out.print(playerIn.getId() + " discards " + tileDiscard + "\n");
        playerIn.discard(tileDiscard);
    }

    public EnumTile scan() {
        String scan = "";
        Player player = game.getPlayer(game.getGameState().getCurPlayer());
        while (EnumTile.getTile(scan) == null) {
            System.out.print(player.getId() + ": ");
            scan = scanner.nextLine();
            if (Player.Option.getOption(scan.split("\\s")[0]) != null) {
                Player.Option option = Player.Option.getOption(scan.split("\\s")[0]);
                EnumTile tile = null;
                if (scan.split("\\s").length > 1) tile = EnumTile.getTile(scan.split("\\s")[1]);
                player.selectOption(option, tile, player.canChyankan());
                if (option == Player.Option.KITA || option == Player.Option.ANGANG || option == Player.Option.PLUSGANG) printTable(player);
                else return null;
            }
        }
        return EnumTile.getTile(scan);
    }

    private void scanOption(Player playerIn) {
        String scan;
        while (true) {
            System.out.print(playerIn.getId() + ": ");
            scan = scanner.nextLine();
            if (Player.Option.getOption(scan.split("\\s")[0]) != null) {
                Player.Option option = Player.Option.getOption(scan.split("\\s")[0]);
                EnumTile tile = null;
                if (scan.split("\\s").length > 1) tile = EnumTile.getTile(scan.split("\\s")[1]);
                if (playerIn.selectOption(option, tile, playerIn.canChyankan()) != null) break;
            }
        }
    }

}
