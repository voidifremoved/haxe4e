/*
 * SPDX-FileCopyrightText: © The Haxe4E authors
 * SPDX-FileContributor: Sebastian Thomschke
 * SPDX-License-Identifier: EPL-2.0
 * SPDX-ArtifactOfProjectHomePage: https://github.com/haxe4e/haxe4e
 */
package org.haxe4e.tests.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.haxe4e.util.Ansi;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Thomschke
 */
class AnsiTest {

    @Test
    void testAnsi() {
        assertThat(Ansi.RESET).isEqualTo("\033[0m");
        assertThat(Ansi.BOLD).isEqualTo("\033[1m");
        assertThat(Ansi.RED).isEqualTo("\033[31m");
        assertThat(Ansi.GREEN).isEqualTo("\033[32m");
        assertThat(Ansi.YELLOW).isEqualTo("\033[33m");
        assertThat(Ansi.BLUE).isEqualTo("\033[34m");
        assertThat(Ansi.MAGENTA).isEqualTo("\033[35m");
        assertThat(Ansi.CYAN).isEqualTo("\033[36m");
        assertThat(Ansi.WHITE).isEqualTo("\033[37m");
        assertThat(Ansi.GRAY).isEqualTo("\033[90m");
    }
}
