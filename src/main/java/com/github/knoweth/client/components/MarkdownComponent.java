package com.github.knoweth.client.components;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.teavm.flavour.templates.BindAttribute;
import org.teavm.flavour.templates.BindElement;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Slot;
import org.teavm.flavour.widgets.AbstractWidget;

import java.util.function.Supplier;

/**
 * Component that renders its content as markdown.
 */
@BindTemplate("templates/components/markdown.html")
@BindElement(name = "markdown")
public class MarkdownComponent extends AbstractWidget {
    private Supplier<String> contentSupplier;

    public MarkdownComponent(Slot slot) {
        super(slot);
    }


    /*
     * Tell template engine that we want our component to support 'content' attribute.
     */
    @BindAttribute(name = "content")
    public void setContentSupplier(Supplier<String> contentSupplier) {
        this.contentSupplier = contentSupplier;
    }

    /*
     * Expose rendered content to `components/markdown.html`.
     */
    public String getRendered() {
        String raw = contentSupplier.get();
        Parser parser = Parser.builder().build();
        Node document = parser.parse(raw);
        HtmlRenderer render = HtmlRenderer.builder().build();
        return render.render(document);
    }
}
