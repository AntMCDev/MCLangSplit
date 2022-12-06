package com.ant.mclangsplit.common;

import com.ant.mclangsplit.common.translation.Storage;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * [COMMON] MAIN - This is the common entry point for shared functionality. All platform-specific initializers should
 * call the init method of this class
 */
public class MCLangSplit {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_NAME = "mclangsplit";

    private MCLangSplit(){}

    public static void init() {
        Storage.load();
    }

    public static boolean isLogicalClient(Level level) { return level.isClientSide; }

    public static boolean isLogicalServer(Level level) {
        return !isLogicalClient(level);
    }

}
