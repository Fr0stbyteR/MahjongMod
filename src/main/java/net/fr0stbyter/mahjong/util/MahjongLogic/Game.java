package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game {
    private long seed;
    private UI ui;
    private GameState gameState;
    private GameType gameType;
    private ArrayList<Player> players;
    private River river;
    private Mountain mountain;
    private Dices dices;
    private ArrayList<Player> playersHasOptions;
    private HashMap<Player, HashMap<Player.Option, EnumTile>> optionsSelected;
    private boolean isRenchyan;

    public Game(GameType gameTypeIn, UI uiIn) {
        ui = uiIn;
        ui.setGame(this);
        gameType = gameTypeIn;
    }

    public Game(HashMap<String, EnumFacing> playersIn, GameType gameTypeIn, UI uiIn) {
        ui = uiIn;
        ui.setGame(this);
        gameType = gameTypeIn;
        initGame(playersIn);
    }

    public Game(ArrayList<String> playersIdIn, GameType gameTypeIn, UI uiIn) {
        ui = uiIn;
        ui.setGame(this);
        gameType = gameTypeIn;
        initGame(playersIdIn);
    }

    public void initGame(HashMap<String, EnumFacing> playersIn) {
        gameState = new GameState(this);
        players = new ArrayList<Player>();
        river = new River(this);
        optionsSelected = new HashMap<Player, HashMap<Player.Option, EnumTile>>();
        Random random = new Random();
        seed = random.nextInt();
        dices = new Dices(2, seed);
        playersHasOptions = new ArrayList<Player>();
        // sitting down
        // decide oya with dices
        //Collections.shuffle(playersIdIn);
        ui.setPositions(playersIn);
        ArrayList<String> playersIdIn = new ArrayList<String>(playersIn.keySet());
        int oyaIndex = (dices.roll().getSum() - 1) % gameType.getPlayerCount(); // roll temp Oya
        oyaIndex = (oyaIndex + dices.roll().getSum() - 1) % gameType.getPlayerCount(); // Oya roll
        for (int i = 0; i < gameType.getPlayerCount(); i++) {
            players.add(new Player(this, playersIdIn.get(i), EnumPosition.getPosition(i)));
        }
        for (Player player : players) {
            player.setScore(gameType.getPlayerCount() == 4 ? 25000 : 35000);
        }
        while (players.get(oyaIndex).getCurWind() != EnumPosition.EAST) {
            nextOya();
        }
        deal();
    }

    public void initGame(ArrayList<String> playersIdIn) {
        gameState = new GameState(this);
        players = new ArrayList<Player>();
        river = new River(this);
        optionsSelected = new HashMap<Player, HashMap<Player.Option, EnumTile>>();
        seed = 101;
        dices = new Dices(2, seed);
        playersHasOptions = new ArrayList<Player>();
        // sitting down
        // decide oya with dices
        //Collections.shuffle(playersIdIn);
        int oyaIndex = (dices.roll().getSum() - 1) % gameType.getPlayerCount(); // roll temp Oya
        oyaIndex = (oyaIndex + dices.roll().getSum() - 1) % gameType.getPlayerCount(); // Oya roll
        for (int i = 0; i < gameType.getPlayerCount(); i++) {
            players.add(new Player(this, playersIdIn.get(i), EnumPosition.getPosition(i)));
        }
        for (Player player : players) {
            player.setScore(gameType.getPlayerCount() == 4 ? 25000 : 35000);
        }
        while (players.get(oyaIndex).getCurWind() != EnumPosition.EAST) {
            nextOya();
        }
        deal();
    }


    public Game deal() {
        // create mountain
        mountain = new Mountain(this, seed);
        int openPositionIndex = (dices.roll().getSum() - 1) % gameType.getPlayerCount();
        mountain.open(EnumPosition.getPosition(openPositionIndex), dices.getSum() - 1);
        // first deal
        gameState.setPhase(GameState.Phase.DEAL);
        for (int i = 0; i < 3; i++) {
            for (Player player : players) {
                for (int j = 0; j < 4; j++) {
                    player.addToHanding(mountain.getNextThenRemove());
                }
            }
        }
        for (Player player : players) {
            player.addToHanding(mountain.getNextThenRemove());
            player.analyzeWaiting();
        }
        getPlayer(gameState.getCurPlayer()).getTileFromMountain();
        ui.dealOver();
        // wait discard
        return this;
    }

    public Game nextDeal() {
        river.cancelWaiting();
        if (mountain.getNextProp() == MountainTile.Prop.DORA) ryuukyoku(getPlayer(gameState.getCurPlayer()), Ryuukyoku.KOUHAIHEIKYOKU);
        while (getMountain().getCountDora() < getCountGang() + 1) {
            getMountain().extraDora();
        }
        if ((getCountGang() == 4 && !isOnePersonGang()) || getCountGang() > 4) {
            ryuukyoku(getPlayer(getGameState().getCurPlayer()), Game.Ryuukyoku.SUUKANSANRA);
            return this;
        }
        gameState.nextPlayer();
        for (Player player : players) {
            player.setCanChyankan(false);
        }
        gameState.setPhase(GameState.Phase.DEAL);
        getPlayer(gameState.getCurPlayer()).getTileFromMountain();
        ui.nextDealOver();
        return this;
    }

    public ArrayList<Player> checkOptions(Player playerIn, EnumTile tileIn, boolean isChyankanIn) {
        optionsSelected.clear();
        playersHasOptions.clear();
        for (Player player : players) {
            if (player == playerIn) continue;
            if (!player.getOptions(playerIn, tileIn, isChyankanIn).isEmpty()) playersHasOptions.add(player);
        }
        if (playersHasOptions.isEmpty() && !isChyankanIn) nextDeal();
        else ui.options();
        return playersHasOptions;
    }

    public Game selectOption(Player playerIn, HashMap<Player.Option, EnumTile> optionIn, boolean isChyankanIn) {
        if (!optionIn.containsKey(Player.Option.CANCEL)) optionsSelected.put(playerIn, optionIn);
        playersHasOptions.remove(playerIn);
        for (Player player : players) {
            player.setCanChyankan(isChyankanIn);
        }
        if (playersHasOptions.isEmpty()) {
            ArrayList<Player> playerAgari = new ArrayList<Player>();
            EnumTile tileAgari = null;
            isRenchyan = false;
            for (Player player : optionsSelected.keySet()) {
                if (optionsSelected.get(player).containsKey(Player.Option.RON)) {
                    playerAgari.add(player);
                    tileAgari = optionsSelected.get(player).get(Player.Option.RON);
                    if (player.getCurWind() == EnumPosition.EAST) isRenchyan = true;
                }
            }
            if (playerAgari.size() == 3) {
                ryuukyoku(null, Ryuukyoku.SANCHYAHOU);
                return this;
            }
            if (!playerAgari.isEmpty()) {
                for (Player player : optionsSelected.keySet()) {
                    if (!optionsSelected.get(player).containsKey(Player.Option.RON)) {
                        optionsSelected.remove(player);
                    }
                }
                playerAgari.get(0).ron(gameState.getCurPlayer(), tileAgari);
                return this;
            }
            if (isChyankanIn) {
                nextDeal();
                return this;
            }
            for (Player player : optionsSelected.keySet()) {
                if (optionsSelected.get(player).containsKey(Player.Option.GANG)) {
                    breakIbbatsu();
                    player.gang(gameState.getCurPlayer(), optionsSelected.get(player).get(Player.Option.GANG));
                    ui.choosed(player, Player.Option.GANG);
                    return this;
                }
            }
            for (Player player : optionsSelected.keySet()) {
                if (optionsSelected.get(player).containsKey(Player.Option.PENG)) {
                    breakIbbatsu();
                    player.peng(gameState.getCurPlayer(), optionsSelected.get(player).get(Player.Option.PENG));
                    ui.choosed(player, Player.Option.PENG);
                    return this;
                }
            }
            for (Player player : optionsSelected.keySet()) {
                if (optionsSelected.get(player).containsKey(Player.Option.CHI)) {
                    breakIbbatsu();
                    player.chi(gameState.getCurPlayer(), optionsSelected.get(player).get(Player.Option.CHI));
                    ui.choosed(player, Player.Option.CHI);
                    return this;
                }
            }
        }
        nextDeal();
        return this;
    }

    public Game requestConfirm() {
        playersHasOptions.clear();
        for (Player player : players) {
            player.requestConfirm();
            playersHasOptions.add(player);
        }
        ui.requestConfirm();
        return this;
    }

    public Game confirm(Player playerIn) {
        playerIn.clearOptions();
        playersHasOptions.remove(playerIn);
        if (!playersHasOptions.isEmpty()) return this;
        if (gameState.getPhase() == GameState.Phase.GAME_OVER) gameOver();
        if (gameState.getPhase() == GameState.Phase.AGARI) {
            if (!optionsSelected.isEmpty()) {
                for (Player player : optionsSelected.keySet()) {
                    if (!optionsSelected.get(player).containsKey(Player.Option.RON)) {
                        player.ron(gameState.getCurPlayer(), optionsSelected.get(player).get(Player.Option.RON));
                        return this;
                    }
                }
            } else {
                for (Player player : players) {
                    if (player.getScore() < 0) {
                        gameState.setPhase(GameState.Phase.GAME_OVER);
                        showGameOver();
                        return this;
                    }
                }
                if (isRenchyan && gameState.isAllLast() && getTopPlayer() == getPlayer(gameState.getCurPlayer())) {
                    gameState.setPhase(GameState.Phase.GAME_OVER);
                    showGameOver();
                    return this;
                }
                if (!isRenchyan && gameState.isAllLast()) {
                    for (Player player : players) {
                        if (player.getScore() > (gameType.getPlayerCount() == 4 ? 30000 : 40000)) {
                            gameState.setPhase(GameState.Phase.GAME_OVER);
                            showGameOver();
                            return this;
                        }
                    }
                }
                nextPlay();
            }
        }
        return this;
    }

    private Game showGameOver() {
        ui.showReport();
        return this;
    }

    private Game gameOver() {
        ui.gameOver();
        return this;
    }

    public void nextPlay() {
        if (isRenchyan) gameState.nextExtra();
        else {
            gameState.nextHand();
            nextOya();
        }
        for (Player player : players) {
            player.reset();
        }
        river = new River(this);
        optionsSelected.clear();
        playersHasOptions.clear();
        deal();
    }

    public void agari(Player playerIn, Player fromPlayerIn, WinningHand winningHandIn) {
        gameState.setPhase(GameState.Phase.AGARI);
        optionsSelected.remove(playerIn);
        HashMap<Player, Integer> scoreChange = new HashMap<Player, Integer>();
        int scoreGet = winningHandIn.getScore();
        scoreGet += gameState.getCurExtra() * (gameType.getPlayerCount() == 4 ? 300 : 200);
        // calc lost player score
        for (Player player : players) {
            scoreChange.put(player, player == playerIn
                    ? scoreGet + gameState.getCountRiichi() * 1000
                    : player == fromPlayerIn
                        ? scoreGet * -1
                        : fromPlayerIn == null
                            ? (int) (Math.ceil(((double) (scoreGet
                                / (gameType.getPlayerCount() + (playerIn.isOya() ? 0 : 1))
                                * (player.isOya() ? 2 : 1)
                                - (gameType.getPlayerCount() == 3 ? 1000 : 0))) / 100) * 100) * -1
                            : 0);
        }
        // recalc winning player score
        if (fromPlayerIn != null) {
            scoreGet = 0;
            for (Player player : players) {
                if (player != playerIn) scoreGet += scoreChange.get(player) * -1;
            }
            scoreChange.put(playerIn, scoreGet);
        }
        // add to score
        for (Player player : players) {
            player.setScore(player.getScore() + scoreChange.get(player));
        }
        // return riichibou
        gameState.setCountRiichi(0);
        ui.showReport(playerIn, scoreChange);
        requestConfirm();
    }

    public Game ryuukyoku(Player playerIn, Ryuukyoku ryuukyokuIn) {
        gameState.setPhase(GameState.Phase.DRAW);
        if (ryuukyokuIn == Ryuukyoku.KOUHAIHEIKYOKU) {
            HashMap<Player, Integer> scoreChange = new HashMap<Player, Integer>();
            for (Player player1 : players) {
                scoreChange.put(player1, 0);
            }
            boolean isNagashimankan = true;
            for (Player player : players) {
                isNagashimankan = true;
                for (RiverTile tile : river.getTilesFromPosition(player.getCurWind())) {
                    if (!TileGroup.yaochyuu.contains(tile.getTile()) || !tile.isShown()) {
                        isNagashimankan = false;
                        break;
                    }
                }
                if (isNagashimankan) {
                    for (Player player1 : players) {
                        scoreChange.put(player1, scoreChange.get(player1)
                                + (player1 == player
                                    ? (player1.isOya()
                                        ? (gameType.getPlayerCount() == 3 ? 8000 : 12000)
                                        : (gameType.getPlayerCount() == 3 ? 6000 : 8000))
                                    : (player1.isOya()
                                        ? -4000
                                        : (player.isOya() ? -4000 : -2000))));
                    }
                }
            }
            if (isNagashimankan) {
                ui.showReport(Ryuukyoku.NAGASHIMANKAN, scoreChange);
                requestConfirm();
                return this;
            } else {
                int countTen = 0;
                int tenScore = gameType.getPlayerCount() == 3 ? 2000 : 3000;
                for (Player player1 : players) {
                    countTen++;
                }
                if (countTen == gameType.getPlayerCount() || countTen == 0) {
                    for (Player player1 : players) {
                        if (player1.getWaiting().isEmpty()) scoreChange.put(player1, 0);
                    }
                } else {
                    for (Player player1 : players) {
                        if (player1.getWaiting().isEmpty()) scoreChange.put(player1, -1 * tenScore / (gameType.getPlayerCount() - countTen));
                        else scoreChange.put(player1, tenScore / countTen);
                    }
                }
                for (Player player : players) {
                    player.setScore(player.getScore() + scoreChange.get(player));
                }
                ui.showReport(ryuukyokuIn, scoreChange);
                requestConfirm();
                return this;
            }
        }
        ui.showReport(ryuukyokuIn, null);
        requestConfirm();
        return this;
    }

    public Game breakIbbatsu() {
        for (Player player : players) {
            player.setCanIbbatsu(false);
        }
        return this;
    }

    public int getCountGang() {
        int count = 0;
        for (Player player : players) {
            count += player.getHand().getCountAnGang() + player.getHand().getCountGang();
        }
        return count;
    }

    public boolean isOnePersonGang() {
        int countPlayers = 0;
        for (Player player : players) {
            int count = player.getHand().getCountAnGang() + player.getHand().getCountGang();
            if (count > 0) countPlayers++;
        }
        return countPlayers < 2;
    }

    public Game nextOya() {
        for (Player player : players) {
            player.nextWind();
        }
        return this;
    }

    public Game checkRiichibou() {
        for (Player player : players) {
            if (player.isRiichi()) {
                player.setScore(player.getScore() - 1000);
                gameState.newRiichi();
                ui.riichi(player);
            }
        }
        return this;
    }

    public Player getTopPlayer() {
        int topScore = 0;
        Player top = null;
        for (Player player : players) {
            if (player.getScore() > topScore) top = player;
        }
        return top;
    }

    public UI getUi() {
        return ui;
    }

    public GameState getGameState() {
        return gameState;
    }

    public GameType getGameType() {
        return gameType;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(EnumPosition enumPosition) {
        for (Player player : players) {
            if (player.getCurWind() == enumPosition) return player;
        }
        return null;
    }

    public Player getPlayer(String idIn) {
        for (Player player : players) {
            if (player.getId().equals(idIn)) return player;
        }
        return null;
    }

    public Player getCurPlayer() {
        return getPlayer(getGameState().getCurPlayer());
    }

    public River getRiver() {
        return river;
    }

    public Mountain getMountain() {
        return mountain;
    }

    public Dices getDices() {
        return dices;
    }

    public int getCountRiichi() {
        int count = 0;
        for (Player player : players) {
            if (player.isRiichi()) count++;
        }
        return count;
    }

    public enum Ryuukyoku {KOUHAIHEIKYOKU, KYUUSHYUKYUUHAI, SUUFONRENTA, SUUKANSANRA, SUUCHYARIICHI, SANCHYAHOU, NAGASHIMANKAN}
}
