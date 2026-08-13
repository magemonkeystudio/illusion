/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pure string parsing helpers used by {@link NMSVersion} to identify the running server version. Kept separate from
 * the {@link NMSVersion} enum so that this logic can be unit tested without triggering the enum's static
 * initializer, which requires every supported NMS implementation to be present on the classpath.
 */
final class NMSVersionStringParser {

    private static final Pattern LEGACY_NMS_PACKAGE_PATTERN = Pattern.compile("v\\d+_\\d+_R\\d+");

    private NMSVersionStringParser() {
    }

    /**
     * Starting with the 2026 release cycle, Minecraft dropped the "1.x" versioning scheme in favor of a year-based
     * one (e.g. "26.1", "26.2"). Spigot reports this version as "&lt;version&gt;-R0.1-SNAPSHOT", same as before, but
     * Paper instead reports "&lt;version&gt;.build.&lt;n&gt;-&lt;channel&gt;" (e.g. "26.2.build.62-beta") for
     * releases, or "&lt;version&gt;-DEV-&lt;branch&gt;@&lt;commit&gt;" (e.g. "26.1.2-DEV-main@7799bf2") for
     * development builds, where the build number, channel, branch and commit change on every release, so only the
     * leading "&lt;version&gt;" part can be matched reliably.
     */
    static String normalizeYearBasedVersion(String bukkitVersion) {
        String normalizedVersion = bukkitVersion;
        int buildSuffixIndex = normalizedVersion.indexOf(".build.");
        int devSuffixIndex = normalizedVersion.indexOf("-DEV-");
        if (buildSuffixIndex != -1) {
            normalizedVersion = normalizedVersion.substring(0, buildSuffixIndex);
        } else if (devSuffixIndex != -1) {
            normalizedVersion = normalizedVersion.substring(0, devSuffixIndex);
        } else if (normalizedVersion.endsWith("-R0.1-SNAPSHOT")) {
            normalizedVersion = normalizedVersion.substring(0, normalizedVersion.length() - "-R0.1-SNAPSHOT".length());
        }
        return normalizedVersion;
    }

    /**
     * Before 1.17, CraftBukkit/NMS classes were relocated into per-version packages (e.g. "v1_13_R2"), which is
     * matched here to identify the running version. Returns {@code null} if no such package name is found.
     */
    static String extractLegacyNmsVersionName(String nmsPackageName) {
        Matcher matcher = LEGACY_NMS_PACKAGE_PATTERN.matcher(nmsPackageName);
        return matcher.find() ? matcher.group() : null;
    }

}
