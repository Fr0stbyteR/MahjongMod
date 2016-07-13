package net.fr0stbyter.mahjong.util.MahjongLogic;

public enum EnumPosition {
    EAST(1),
    SOUTH(2),
    WEST(3),
    NORTH(4);
    private int index;
    EnumPosition(int indexIn) {
        index = indexIn;
    }
    public int getIndex() {
        return index;
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
