package com.ant.mclangsplit.common.config;

import java.io.*;

/**
 * [COMMON] CONFIG - This is the generic container class for configurations, it is platform independent - however
 * any GUI libs will have to interpret this dynamically
 */
public class Config implements Serializable {
    public enum Mode {
        ORIGINAL, ALTERNATE, BOTH
    }
    public static Config INSTANCE = new Config();

    public String language = "en_us";
    public String[] ignoreKeys = new String[0];
    public String[] includeKeys = new String[0];
    public boolean ignoreTooltips = false;
    public Mode mode = Mode.BOTH;
}
