package net.fr0stbyter.mahjong.util.MahjongLogic;

public enum EnumTileGroup {
    MAN("man"),
    PIN("pin"),
    SOU("sou"),
    HUA("hua"),
    WIND("wind"),
    DRAGON("dragon");
    private String name;

    EnumTileGroup(String nameIn) {
        name = nameIn;
    }

    public static EnumTileGroup getTileGroup(String name) {
        for (EnumTileGroup group : values()) {
            if (name.equals(group.name)) return group;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
