/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core;

import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.holographicdisplays.nms.common.NMSManager;
import me.filoghost.holographicdisplays.nms.v26_2.VersionNMSManager;
import org.bukkit.Bukkit;

/**
 * The package name used by version-dependent Bukkit classes and NMS classes (before 1.17), for example "v1_13_R2".
 * Different versions usually imply internal changes that require multiple implementations.
 */
public enum NMSVersion {

    // Not using shorter method reference syntax here because it initializes the class, causing a ClassNotFoundException

    /* 1.12 - 1.12.2   */ v1_12_R1(me.filoghost.holographicdisplays.nms.v1_12_R1.VersionNMSManager::new),
    /* 1.13            */ v1_13_R1(NMSManagerFactory.outdatedVersion("1.13.1")),
    /* 1.13.1 - 1.13.2 */ v1_13_R2(me.filoghost.holographicdisplays.nms.v1_13_R2.VersionNMSManager::new),
    /* 1.14 - 1.14.4   */ v1_14_R1(me.filoghost.holographicdisplays.nms.v1_14_R1.VersionNMSManager::new),
    /* 1.15 - 1.15.2   */ v1_15_R1(me.filoghost.holographicdisplays.nms.v1_15_R1.VersionNMSManager::new),
    /* 1.16 - 1.16.1   */ v1_16_R1(NMSManagerFactory.outdatedVersion("1.16.4")),
    /* 1.16.2 - 1.16.3 */ v1_16_R2(NMSManagerFactory.outdatedVersion("1.16.4")),
    /* 1.16.4 - 1.16.5 */ v1_16_R3(me.filoghost.holographicdisplays.nms.v1_16_R3.VersionNMSManager::new),
    /* 1.17            */ v1_17_R1(me.filoghost.holographicdisplays.nms.v1_17_R1.VersionNMSManager::new),
    /* 1.18 - 1.18.1   */ v1_18_R1(me.filoghost.holographicdisplays.nms.v1_18_R1.VersionNMSManager::new),
    /* 1.18.2          */ v1_18_R2(me.filoghost.holographicdisplays.nms.v1_18_R2.VersionNMSManager::new),
    /* 1.19 - 1.19.2   */ v1_19_R1(me.filoghost.holographicdisplays.nms.v1_19_R1.VersionNMSManager::new),
    /* 1.19.3          */ v1_19_R2(me.filoghost.holographicdisplays.nms.v1_19_R2.VersionNMSManager::new),
    /* 1.19.4          */ v1_19_R3(me.filoghost.holographicdisplays.nms.v1_19_R3.VersionNMSManager::new),
    /* 1.20 - 1.20.1   */ v1_20_R1(me.filoghost.holographicdisplays.nms.v1_20_R1.VersionNMSManager::new),
    /* 1.20.2          */ v1_20_R2(me.filoghost.holographicdisplays.nms.v1_20_R2.VersionNMSManager::new),
    /* 1.20.3 - 1.20.4 */ v1_20_R3(me.filoghost.holographicdisplays.nms.v1_20_R3.VersionNMSManager::new),
    /* 1.20.5 - 1.20.6 */ v1_20_R4(me.filoghost.holographicdisplays.nms.v1_20_R4.VersionNMSManager::new),
    /* 1.21   - 1.21.1 */ v1_21_R1(me.filoghost.holographicdisplays.nms.v1_21_R1.VersionNMSManager::new),
    /* 1.21.2 - 1.21.3 */ v1_21_R2(me.filoghost.holographicdisplays.nms.v1_21_R2.VersionNMSManager::new),
    /* 1.21.4          */ v1_21_R3(me.filoghost.holographicdisplays.nms.v1_21_R3.VersionNMSManager::new),
    /* 1.21.5          */ v1_21_R4(me.filoghost.holographicdisplays.nms.v1_21_R4.VersionNMSManager::new),
    /* 1.21.6 - 1.21.8 */ v1_21_R5(me.filoghost.holographicdisplays.nms.v1_21_R5.VersionNMSManager::new),
    /* 1.21.9- 1.21.10 */ v1_21_R6(me.filoghost.holographicdisplays.nms.v1_21_R6.VersionNMSManager::new),
    /* 1.21.11         */ v1_21_R7(me.filoghost.holographicdisplays.nms.v1_21_R7.VersionNMSManager::new),
    /* 26.1.2          */ v26_1(me.filoghost.holographicdisplays.nms.v26_1.VersionNMSManager::new),
    /* 26.2 - X        */ v26_2(VersionNMSManager::new),
    /* Other versions  */ UNKNOWN(NMSManagerFactory.unknownVersion());

    private static final NMSVersion CURRENT_VERSION = detectCurrentVersion();

    private final NMSManagerFactory nmsManagerFactory;

    NMSVersion(NMSManagerFactory nmsManagerFactory) {
        this.nmsManagerFactory = nmsManagerFactory;
    }

    public NMSManager createNMSManager(ErrorCollector errorCollector) throws
            OutdatedVersionException,
            UnknownVersionException {
        return nmsManagerFactory.create(errorCollector);
    }

    public static NMSVersion getCurrent() {
        return CURRENT_VERSION;
    }

    private static NMSVersion detectCurrentVersion() {
        return detectVersion(
                Bukkit.getServer().getBukkitVersion(),
                Bukkit.getServer().getClass().getPackage().getName());
    }

    private static NMSVersion detectVersion(String bukkitVersion, String nmsPackageName) {
        String[] versionParts     = bukkitVersion.split("[.-]");
        int      firstVersionPart = Integer.parseInt(versionParts[0]);

        if (firstVersionPart != 1) {
            // CraftBukkit no longer relocates NMS/CraftBukkit classes into per-version packages starting with the
            // 2026 release cycle, so there's nothing to match with the regex fallback below either.
            String normalizedVersion = NMSVersionStringParser.normalizeYearBasedVersion(bukkitVersion);

            switch (normalizedVersion) {
                case "26.1.2":
                    return v26_1;
                case "26.2":
                    return v26_2;
                default:
                    return UNKNOWN;
            }
        }

        int majorVersion = Integer.parseInt(versionParts[1]);
        if (majorVersion >= 20) {
            switch (bukkitVersion) {
                case "1.20-R0.1-SNAPSHOT":
                case "1.20.1-R0.1-SNAPSHOT":
                    return v1_20_R1;
                case "1.20.2-R0.1-SNAPSHOT":
                    return v1_20_R2;
                case "1.20.3-R0.1-SNAPSHOT":
                case "1.20.4-R0.1-SNAPSHOT":
                    return v1_20_R3;
                case "1.20.5-R0.1-SNAPSHOT":
                case "1.20.6-R0.1-SNAPSHOT":
                    return v1_20_R4;
                case "1.21-R0.1-SNAPSHOT":
                case "1.21.1-R0.1-SNAPSHOT":
                    return v1_21_R1;
                case "1.21.2-R0.1-SNAPSHOT":
                case "1.21.3-R0.1-SNAPSHOT":
                    return v1_21_R2;
                case "1.21.4-R0.1-SNAPSHOT":
                    return v1_21_R3;
                case "1.21.5-R0.1-SNAPSHOT":
                    return v1_21_R4;
                case "1.21.6-R0.1-SNAPSHOT":
                case "1.21.7-R0.1-SNAPSHOT":
                case "1.21.8-R0.1-SNAPSHOT":
                    return v1_21_R5;
                case "1.21.9-R0.1-SNAPSHOT":
                case "1.21.10-R0.1-SNAPSHOT":
                    return v1_21_R6;
                case "1.21.11-R0.1-SNAPSHOT":
                    return v1_21_R7;
                default:
                    return UNKNOWN;
            }
        }

        String nmsVersionName = NMSVersionStringParser.extractLegacyNmsVersionName(nmsPackageName);
        if (nmsVersionName == null) {
            return UNKNOWN;
        }

        try {
            return valueOf(nmsVersionName);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }


    @FunctionalInterface
    private interface NMSManagerFactory {

        NMSManager create(ErrorCollector errorCollector) throws UnknownVersionException, OutdatedVersionException;

        static NMSManagerFactory unknownVersion() {
            return errorCollector -> {
                throw new UnknownVersionException();
            };
        }

        static NMSManagerFactory outdatedVersion(String minimumSupportedVersion) {
            return errorCollector -> {
                throw new OutdatedVersionException(minimumSupportedVersion);
            };
        }

    }

    public static class UnknownVersionException extends Exception {
    }

    public static class OutdatedVersionException extends Exception {

        private final String minimumSupportedVersion;

        public OutdatedVersionException(String minimumSupportedVersion) {
            this.minimumSupportedVersion = minimumSupportedVersion;
        }

        public String getMinimumSupportedVersion() {
            return minimumSupportedVersion;
        }

    }

}
