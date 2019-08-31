package com.github.knoweth.client.views;

import com.github.knoweth.client.services.Services;
import com.github.knoweth.common.data.Card;
import com.github.knoweth.common.data.Document;
import com.github.knoweth.common.reviewing.AnkiAlgorithm;
import com.github.knoweth.common.reviewing.ReviewQuality;
import com.github.knoweth.common.reviewing.ReviewTracker;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Templates;
import org.teavm.flavour.widgets.BackgroundWorker;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.threeten.bp.Duration;

/**
 * State for the reviewing view.
 */
@BindTemplate("templates/review.html")
public class ReviewView extends AuthenticatedView {
    private int id;
    private Document document;
    private ReviewTracker tracker;
    private Card currentCard;
    private int remaining;
    private boolean showingBack;

    public ReviewView(int id) {
        this.id = id;
        new BackgroundWorker().run(() -> {
            // Fetch the document and initialize the review tracker
            document = Services.STORAGE.getDocument(id);
            tracker = new ReviewTracker(new AnkiAlgorithm());
            tracker.importCards(document);
            nextCard();

            // Note: there is no way that I know of to remove this event
            // listener, but it should be harmless on other pages.
            Window.current().getDocument().addEventListener("keydown", this::onKeydown);
        });
    }

    /**
     * Called when any key is pressed on the site.
     * @param event a KeyboardEvent
     */
    private void onKeydown(Event event) {
        KeyboardEvent kbd = (KeyboardEvent)event;

        switch (kbd.getKey()) {
            case "1":
                markReviewed(ReviewQuality.AGAIN);
                break;
            case "2":
                markReviewed(ReviewQuality.HARD);
                break;
            case "3":
                markReviewed(ReviewQuality.GOOD);
                break;
            case "4":
                markReviewed(ReviewQuality.EASY);
                break;
            case "Enter":
                showBack();
                break;
            default:
                break;
        }
        // Update templates since TeaVM will not do it automatically here
        Templates.update();
    }

    private void nextCard() {
        showingBack = false;
        currentCard = tracker.getNextReview();
        remaining = tracker.getRemainingReviews();
    }

    public void markReviewed(ReviewQuality quality) {
        tracker.markReviewed(currentCard, Duration.ZERO, quality);
        System.out.println("Marked reviewed - " + quality.toString());
        nextCard();
    }

    public int getId() {
        return id;
    }

    public Document getDocument() {
        return document;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public int getRemaining() {
        return remaining;
    }

    public boolean getShowingBack() {
        return showingBack;
    }

    public void showBack() {
        showingBack = true;
    }
}
