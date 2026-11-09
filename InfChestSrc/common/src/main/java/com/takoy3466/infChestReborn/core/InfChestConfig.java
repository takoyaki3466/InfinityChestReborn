package com.takoy3466.infChestReborn.core;

import com.mojang.logging.LogUtils;
import com.takoy3466.infChestReborn.InfChestCommon;
import com.takoy3466.infChestReborn.core.platform.Services;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class InfChestConfig {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String FILE_NAME = InfChestCommon.MOD_ID + ".properties";
    private static final String KEY_ORE_NAME_CONVERT = "doOreNameConvert";
    private static final boolean DEFAULT_ORE_NAME_CONVERT = true;
    private static final String FILE_COMMENT = "doOreNameConvert: treat items sharing a common material tag (c:ingots/copper etc.) as the same item";

    private static boolean oreNameConvert = DEFAULT_ORE_NAME_CONVERT;

    private InfChestConfig() {
    }

    public static boolean isOreNameConvertEnabled() {
        return oreNameConvert;
    }

    public static void load() {
        Path file = Services.UTIL.getConfigDirectory().resolve(FILE_NAME);
        Properties properties = new Properties();
        properties.setProperty(KEY_ORE_NAME_CONVERT, Boolean.toString(DEFAULT_ORE_NAME_CONVERT));
        try {
            if (Files.exists(file)) {
                read(file, properties);
            } else {
                write(file, properties);
            }
        } catch (IOException exception) {
            LOGGER.error("Failed to access config file {}. Using default values.", file, exception);
        }
        oreNameConvert = Boolean.parseBoolean(properties.getProperty(KEY_ORE_NAME_CONVERT));
    }

    private static void read(Path file, Properties properties) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            properties.load(reader);
        }
    }

    private static void write(Path file, Properties properties) throws IOException {
        Files.createDirectories(file.getParent());
        try (Writer writer = Files.newBufferedWriter(file)) {
            properties.store(writer, FILE_COMMENT);
        }
    }
}
