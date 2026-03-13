/*
 * SPDX-FileCopyrightText: © The Haxe4E authors
 * SPDX-FileContributor: Sebastian Thomschke
 * SPDX-License-Identifier: EPL-2.0
 * SPDX-ArtifactOfProjectHomePage: https://github.com/haxe4e/haxe4e
 */
package org.haxe4e.tests.util.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;

import org.haxe4e.util.io.VSCodeJsonRpcLineTracing;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Thomschke
 */
class VSCodeJsonRpcLineTracingTest {

   @Test
   void testTraceLine() {
      final var out = new ByteArrayOutputStream();

      VSCodeJsonRpcLineTracing.traceLine(VSCodeJsonRpcLineTracing.Source.CLIENT_OUT, "test", out, true, false);
      assertThat(out.toString()).contains("CLIENT >> test");

      out.reset();
      VSCodeJsonRpcLineTracing.traceLine(VSCodeJsonRpcLineTracing.Source.SERVER_OUT, "test", out, true, false);
      assertThat(out.toString()).contains("SERVER << test");

      out.reset();
      VSCodeJsonRpcLineTracing.traceLine(VSCodeJsonRpcLineTracing.Source.SERVER_ERR, "test", out, true, false);
      assertThat(out.toString()).contains("SRVERR << test");

      out.reset();
      VSCodeJsonRpcLineTracing.traceLine(VSCodeJsonRpcLineTracing.Source.CLIENT_OUT,
         "Content-Length: 10\r\ntest", out, false, false);
      assertThat(out.toString()).contains("CLIENT >> Content-Length: 10\r\ntest");

      out.reset();
      VSCodeJsonRpcLineTracing.traceLine(VSCodeJsonRpcLineTracing.Source.CLIENT_OUT,
         "\"jsonrpc\":\"2.0\",test", out, false, false);
      assertThat(out.toString()).contains("CLIENT >> test");

      out.reset();
      VSCodeJsonRpcLineTracing.traceLine(VSCodeJsonRpcLineTracing.Source.CLIENT_OUT,
         "Content-Type: application/vscode-jsonrpc; charset=utf-8", out, false, false);
      assertThat(out.toString()).isEmpty();

      out.reset();
      VSCodeJsonRpcLineTracing.traceLine(VSCodeJsonRpcLineTracing.Source.CLIENT_OUT, "   ", out, false, false);
      assertThat(out.toString()).isEmpty();
   }
}
