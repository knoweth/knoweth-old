package com.github.knoweth.client.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownParserTest {
    /**
     * Smoke test of basic Markdown functions.
     */
    @Test
    void basicMarkdown() {
        assertEquals("<p><strong>test</strong></p>\n", MarkdownParser.renderToHtml("**test**"));
    }

    /**
     * Test that the markdown parser properly renders math to KaTeX HTML.
     */
    @Test
    void rendersMath() {
        // Render output should contain a KaTeX class
        assertTrue(MarkdownParser.renderToHtml("$`x = 5`$").contains("katex"));
    }
}