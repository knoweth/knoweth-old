package com.github.knoweth.client.views;

import com.github.knoweth.client.services.Services;
import com.github.knoweth.common.data.Card;
import com.github.knoweth.common.data.Document;
import com.github.knoweth.common.reviewing.AnkiAlgorithm;
import com.github.knoweth.common.reviewing.ReviewAlgorithm;
import com.github.knoweth.common.reviewing.ReviewTracker;
import org.teavm.flavour.json.JSON;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;

import java.util.List;

@BindTemplate("templates/review.html")
public class ReviewView extends AuthenticatedView {
    private int id;
    private Document document;
    private ReviewTracker tracker;
    private Card currentCard;
    private int remaining;

    public ReviewView(int id) {
        this.id = id;
        new BackgroundWorker().run(() -> {
            document = Services.STORAGE.getDocument(id);
            tracker = new ReviewTracker(new AnkiAlgorithm());
            tracker.importCards(document);
            nextCard();
        });
    }

    private void nextCard() {
        currentCard = tracker.getNextReview();
        remaining = tracker.getRemainingReviews();
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
}
