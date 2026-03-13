/*
 * SPDX-FileCopyrightText: © The Haxe4E authors
 * SPDX-FileContributor: Sebastian Thomschke
 * SPDX-License-Identifier: EPL-2.0
 * SPDX-ArtifactOfProjectHomePage: https://github.com/haxe4e/haxe4e
 */
package org.haxe4e.tests.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.haxe4e.util.TreeBuilder;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Thomschke
 */
class TreeBuilderTest {

   @Test
   void testTreeBuilder() {
      final var mb = new TreeBuilder<String>();

      assertThat(mb.getMap()).isEmpty();
      assertThatIllegalArgumentException()
         .isThrownBy(() -> mb.put("foo", mb.getMap()));
      assertThatIllegalArgumentException()
         .isThrownBy(() -> mb.put("foo", mb));

      mb.put("a", true);
      mb.put("b", 1);
      mb.put("c", "");
      mb.put("d", (String) null);

      final int size3 = 3;
      assertThat(mb.getMap()).hasSize(size3);

      mb.put("e", "f", true);
      mb.put("e", "g", 1);
      mb.put("e", "h", "test");
      mb.put("e", "i", java.util.List.of("1", "2"));

      final int size4 = 4;
      assertThat(((java.util.Map<?, ?>) mb.getMap().get("e"))).hasSize(size4);

      mb.compute(b -> b.put("computed", true));
      assertThat(mb.getMap()).containsKey("computed");

      mb.compute("e", b -> b.put("computed", true));
      assertThat(((java.util.Map<?, ?>) mb.getMap().get("e")))
         .containsKey("computed");

      mb.compute("new_computed", b -> b.put("nested", true));
      assertThat(((java.util.Map<?, ?>) mb.getMap().get("new_computed")))
         .containsKey("nested");

      mb.putAll(java.util.Map.of("putall1", 1, "putall2", 2));
      assertThat(mb.getMap()).containsKeys("putall1", "putall2");

      mb.put("null_map", (java.util.Map<String, ?>) null);
      mb.put("null_builder", (TreeBuilder<String>) null);
      mb.put("null_list", (java.util.List<String>) null);
      mb.put("null_bool", (Boolean) null);
      mb.put("null_number", (Number) null);
      mb.put("null_string", (String) null);
      assertThat(mb.getMap()).doesNotContainKeys("null_map", "null_builder", "null_list",
         "null_bool", "null_number", "null_string");

      mb.put("e", "null_bool", (Boolean) null);
      mb.put("e", "null_list", (java.util.List<String>) null);
      mb.put("e", "null_number", (Number) null);
      mb.put("e", "null_string", (String) null);
      assertThat(((java.util.Map<?, ?>) mb.getMap().get("e"))).doesNotContainKeys(
         "null_bool", "null_list", "null_number", "null_string");
   }
}
