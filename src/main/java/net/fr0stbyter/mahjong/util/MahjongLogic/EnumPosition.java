package net.fr0stbyter.mahjong.util.MahjongLogic;

public enum EnumPosition {
    EAST("east", 1),
    SOUTH("south", 2),
    WEST("west", 3),
    NORTH("north", 4);
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

    public EnumPosition getPosition(int indexIn) {
        for (EnumPosition position : values()) {
            if (indexIn == position.index) return position;
        }
        return null;
    }
    public EnumPosition getNext() {
        return getPosition(index == 4 ? 1 : index + 1);
    }
    public EnumPosition getPrev() {
        return getPosition(index == 1 ? 4 : index - 1);
    }
    public EnumPosition getOpposite() {
        return getPosition(index < 3 ? index + 2 : index - 2);
    }
}
