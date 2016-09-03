package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.Random;

public class Dices {
    private int[] dices;
    private long seed;
    private Random random;

    public Dices(int diceCountIn, long seedIn) {
        dices = new int[diceCountIn];
        seed = seedIn;
        random = new Random(seed);
        roll();
    }

    public Dices roll() {
        for (int i = 0; i < dices.length; i++) {
            dices[i] = (int) (Math.floor(random.nextDouble() * 6) + 1);
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
