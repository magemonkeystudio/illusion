/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class NMSVersionStringParserTest {

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource("normalizeYearBasedVersionTestArguments")
    void normalizeYearBasedVersion(String bukkitVersion, String expected) {
        assertThat(NMSVersionStringParser.normalizeYearBasedVersion(bukkitVersion)).isEqualTo(expected);
    }

    static Stream<Arguments> normalizeYearBasedVersionTestArguments() {
        return Stream.of(
                // Spigot format, unchanged since before the year-based versioning switch
                Arguments.of("26.1.2-R0.1-SNAPSHOT", "26.1.2"),
                Arguments.of("26.2-R0.1-SNAPSHOT", "26.2"),

                // Paper release format: "<version>.build.<n>-<channel>"
                Arguments.of("26.2.build.62-beta", "26.2"),
                Arguments.of("26.1.2.build.5-default", "26.1.2"),

                // Paper development build format: "<version>-DEV-<branch>@<commit>"
                Arguments.of("26.1.2-DEV-main@7799bf2", "26.1.2"),
                Arguments.of("26.2-DEV-feature/foo@abc1234", "26.2"),

                // Already-normalized or unrecognized strings are returned unchanged
                Arguments.of("26.2", "26.2"),
                Arguments.of("27.0-R0.1-SNAPSHOT", "27.0")
        );
    }

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource("extractLegacyNmsVersionNameTestArguments")
    void extractLegacyNmsVersionName(String nmsPackageName, String expected) {
        assertThat(NMSVersionStringParser.extractLegacyNmsVersionName(nmsPackageName)).isEqualTo(expected);
    }

    static Stream<Arguments> extractLegacyNmsVersionNameTestArguments() {
        return Stream.of(
                Arguments.of("org.bukkit.craftbukkit.v1_12_R1", "v1_12_R1"),
                Arguments.of("org.bukkit.craftbukkit.v1_16_R3.CraftServer", "v1_16_R3"),
                Arguments.of("org.bukkit.craftbukkit.v1_21_R10", "v1_21_R10"),
                Arguments.of("org.bukkit.craftbukkit", null)
        );
    }

}
