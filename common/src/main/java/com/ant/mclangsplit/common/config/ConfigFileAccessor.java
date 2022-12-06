package com.ant.mclangsplit.common.config;

import com.ant.mclangsplit.common.MCLangSplit;
import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigFileAccessor {
    public static Path path = null;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void save() {
        final Path configPath = path.resolve(MCLangSplit.MOD_NAME + ".json");

        try {
            Files.createDirectories(configPath.getParent());
            BufferedWriter writer = Files.newBufferedWriter(configPath);
            gson.toJson(Config.INSTANCE, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load() {
        final Path configPath = path.resolve(MCLangSplit.MOD_NAME + ".json");

        if (Files.exists(configPath)) {
            try {
                BufferedReader reader = Files.newBufferedReader(configPath);
                Config.INSTANCE = gson.fromJson(reader, Config.class);
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
