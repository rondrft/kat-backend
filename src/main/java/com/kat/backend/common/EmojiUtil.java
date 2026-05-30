package com.kat.backend.common;

import com.vdurmont.emoji.EmojiParser;

public class EmojiUtil {

    private EmojiUtil() {}

    public static String normalize(String emoji) {
        if (emoji == null) return null;

        if (!emoji.startsWith(":")) return emoji;

        String parsed = EmojiParser.parseToUnicode(emoji);

        return parsed.equals(emoji) ? emoji : parsed;
    }
}