package net.fr0stbyter.mahjong.util.MahjongLogic;

public enum EnumWinningHand {
    GOKUSHIMUSOU(true, -1, true, 0),
    GOKUSHIMUSOU13(true, -2, true, 0),
    SUUANKOU(true, -1, true, 0),
    SUUANKOUDANKI(true, -2, true, 0),
    DAISANGEN(true, -1, false, -1),
    TSUUIISOU(true, -1, false, -1),
    SHYOUSUUSHII(true, -1, false, -1),
    DAISUUSHII(true, -2, false, -1),
    RYUUIISOU(true, -1, false, -1),
    CHINROUTOU(true, -1, false, -1),
    SUUKANTSU(true, -1, false, -1),
    CHYUURENPOUTOU(true, -1, true, 0),
    CHYUURENPOUTOU9(true, -2, true, 0),
    TENHOU(true, -1, true, 0),
    CHIIHOU(true, -1, true, 0),
    //
    CHINIISOU(false, 6, false, 5),
    HONIISOU(false, 3, false, 2),
    JYUNCHYANTAIYAOCHYUU(false, 3, false, 2),
    RYANBEEKOU(false, 3, true, 0),
    //2fan
    SANSHYOKUDOUJYUN(false, 2, false, 1),
    IKKITSUUKAN(false, 2, false, 1),
    HONCHYANTAIYAOCHYUU(false, 2, false, 1),
    CHIITOITSU(false, 2, true, 0),
    TOITOIHOU(false, 2, false, 2),
    SANANKOU(false, 2, false, 2),
    HONROUTOU(false, 2, false, 2),
    SANSHYOKUDOUKOU(false, 2, false, 2),
    SANKANTSU(false, 2, false, 2),
    SHYOUSANGEN(false, 2, false, 2),
    DABURURIICHI(false, 2, true, 0),
    //1fan
    RIICHI(false, 1, true, 0),
    IBBATSU(false, 1, true, 0),
    MENZENCHINTSUMOHOU(false, 1, true, 0),
    DANYAOCHYUU(false, 1, false, 1),
    BINHU(false, 1, true, 0),
    IIBEEKOU(false, 1, true, 0),
    YAKUHAI(false, 1, false, 1),
    RINSHYANKAIHOU(false, 1, false, 1),
    CHYANKAN(false, 1, false, 1),
    HAITEIMOUYUE(false, 1, false, 1),
    HOUTEIRAOYUI(false, 1, false, 1),
    KITA(false, 1, false, 1),
    DORA(false, 1, false, 1),
    URA(false, 1, false, 1),
    NAGASHIMANGAN(false, 0, true, 0);
    private boolean isYakuman;
    private int fan;
    private boolean isMenZenYaku;
    private int nakuFan;

    EnumWinningHand(boolean isYakumanIn, int fanIn, boolean isMenZenYakuIn, int nakuFanIn) {
        isYakuman = isYakumanIn;
        fan = fanIn;
        isMenZenYaku = isMenZenYakuIn;
        nakuFan = nakuFanIn;
    }

    public boolean isYakuman() {
        return isYakuman;
    }

    public int getFan() {
        return fan;
    }

    public boolean isMenZenYaku() {
        return isMenZenYaku;
    }

    public int getNakuFan() {
        return nakuFan;
    }
}

