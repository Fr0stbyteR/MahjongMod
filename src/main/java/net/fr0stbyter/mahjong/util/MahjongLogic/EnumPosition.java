package net.fr0stbyter.mahjong.util.MahjongLogic;

public enum EnumPosition {
    EAST("east", 0),
    SOUTH("south", 1),
    WEST("west", 2),
    NORTH("north", 3);
    private String name;
    private int index;
    EnumPosition(String nameIn, int indexIn) {
        name = nameIn;
        index = indexIn;
    }
    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
    public String toString() {
        return name;
    }

    public static EnumPosition getPosition(int indexIn) {
        for (EnumPosition position : values()) {
            if (indexIn == position.index) return position;
        }
        return null;
    }
    public EnumPosition getNext() {
        return getPosition(index == 3 ? 0 : index + 1);
    }
    public EnumPosition getPrev() {
        return getPosition(index == 0 ? 3 : index - 1);
    }
    public EnumPosition getNextNoNorth() {
        return getPosition(index == 2 ? 0 : index + 1);
    }
    public EnumPosition getPrevNoNorth() {
        return getPosition(index == 0 ? 2 : index - 1);
    }
    public EnumPosition getOpposite() {
        return getPosition(index < 2 ? index + 2 : index - 2);
    }
}
