package com.github.knoweth.client.components;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.teavm.flavour.templates.*;
import org.teavm.flavour.widgets.AbstractWidget;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.function.Supplier;

@BindElement(name = "markdown")
@IgnoreContent
public class MarkdownComponent extends AbstractWidget {
    private Supplier<String> contentSupplier;
    private NodeHolder renderedSlot;

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
    @Override
    public void render() {
        if (renderedSlot != null) {
            renderedSlot.delete();
        }

        String raw = contentSupplier.get();
        Parser parser = Parser.builder().build();
        Node document = parser.parse(raw);
        HtmlRenderer render = HtmlRenderer.builder().build();
        String html = render.render(document);
        HTMLElement node = Window.current().getDocument().createElement("div");
        node.setInnerHTML(html);

        renderedSlot = new NodeHolder(node);
        getSlot().append(renderedSlot);
    }
}
