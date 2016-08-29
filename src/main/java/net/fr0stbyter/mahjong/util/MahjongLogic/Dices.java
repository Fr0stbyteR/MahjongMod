package net.fr0stbyter.mahjong.util.MahjongLogic;

public class Dices {
    private int[] dices;

    public Dices(int diceCountIn) {
        dices = new int[diceCountIn];
        roll();
    }

    public Dices roll() {
        for (int i = 0; i < dices.length; i++) {
            dices[i] = (int) (Math.floor(Math.random() * 6) + 1);
        }
        return this;
    }

    public int getSum() {
        int sum = 0;
        for (int i = 0; i < dices.length; i++) {
            sum += dices[i];
        }
        return sum;
    }

    public int[] getDices() {
        return dices;
    }
}
