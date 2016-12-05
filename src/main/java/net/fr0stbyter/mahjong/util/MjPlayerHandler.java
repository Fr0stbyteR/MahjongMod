package net.fr0stbyter.mahjong.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MjPlayerHandler {
    HashMap<Integer, ArrayList<Integer>> options;

    public MjPlayerHandler() {
        this.options = new HashMap<Integer, ArrayList<Integer>>();
    }

    public void add(int option, int indexTile) {
        if (options.containsKey(option)) options.get(option).add(indexTile);
        else options.put(option, new ArrayList<Integer>(Collections.singletonList(indexTile)));
    }

    public void clear() {
        options.clear();
    }

    public HashMap<Integer, ArrayList<Integer>> getOptions() {
        return options;
    }

    public ArrayList<Integer> getOptions(int option) {
        return options.get(option);
    }

    public void setOptions(HashMap<Integer, ArrayList<Integer>> options) {
        this.options = options;
    }
}
