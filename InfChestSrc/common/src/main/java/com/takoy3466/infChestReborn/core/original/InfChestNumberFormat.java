package com.takoy3466.infChestReborn.core.original;

import net.minecraft.locale.Language;

/**
 * 在庫数の表示文字列を作る。
 *
 * <p>元 Mod は日本語設定かを直接判定していたが、ツールチップは共通コードから呼ばれるため
 * クライアント専用クラスに依存しないよう、翻訳ファイルの {@value #STYLE_KEY} で表記を切り替える。
 */
public final class InfChestNumberFormat {

    private static final String STYLE_KEY = "infchest_reborn.number_style";
    private static final String MYRIAD_STYLE = "myriad";

    private static final int OKU = 100_000_000;
    private static final int MAN = 10_000;
    private static final int GIGA = 1_000_000_000;
    private static final int MEGA = 1_000_000;
    private static final int KILO = 1_000;

    /**
     * ラージチェスト 1 個分のアイテム数（54 スロット × 64 個）。
     */
    private static final int LARGE_CHEST_CAPACITY = 3456;

    private InfChestNumberFormat() {
    }

    public static String formatStack(int size) {
        return formatStack(size, usesMyriadStyle());
    }

    public static String formatMax(int size) {
        return formatMax(size, usesMyriadStyle());
    }

    static String formatStack(int size, boolean myriadStyle) {
        return myriadStyle ? formatMyriad(size) + "個" : String.format("%,d items", size);
    }

    static String formatMax(int size, boolean myriadStyle) {
        if (myriadStyle) {
            return formatMyriad(size);
        }
        if (size > GIGA) {
            return size / GIGA + "G";
        }
        if (size > MEGA) {
            return size / MEGA + "M";
        }
        if (size > KILO) {
            return size / KILO + "K";
        }
        return "";
    }

    /**
     * ラージチェスト換算の個数を返す。1 個に満たない場合は空文字列を返す。
     */
    public static String formatLargeChests(int size) {
        int largeChests = size / LARGE_CHEST_CAPACITY;
        return largeChests > 0 ? String.format("%,d LC", largeChests) : "";
    }

    private static boolean usesMyriadStyle() {
        return MYRIAD_STYLE.equals(Language.getInstance().getOrDefault(STYLE_KEY, ""));
    }

    private static String formatMyriad(int size) {
        StringBuilder text = new StringBuilder();
        appendIfPositive(text, size / OKU, "億");
        appendIfPositive(text, size % OKU / MAN, "万");
        appendIfPositive(text, size % MAN, "");
        return text.toString();
    }

    private static void appendIfPositive(StringBuilder text, int value, String unit) {
        if (value > 0) {
            text.append(value).append(unit);
        }
    }
}
