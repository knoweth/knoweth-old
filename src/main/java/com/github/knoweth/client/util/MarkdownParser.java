package com.github.knoweth.client.util;

import com.vladsch.flexmark.ext.gitlab.GitLabExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.Collections;

/**
 * Utility class to render markdown to safe HTML.
 */
public class MarkdownParser {
    private static final Parser parser;
    private static final HtmlRenderer renderer;

    static {
        MutableDataSet options = new MutableDataSet()
                .set(GitLabExtension.RENDER_BLOCK_MATH, true)
                .set(Parser.EXTENSIONS, Collections.singletonList(GitLabExtension.create()));
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    public static String renderToHtml(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
