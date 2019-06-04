package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Document;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;

import javax.smartcardio.Card;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class ReviewTracker<R extends ReviewAlgorithm> {
    public class MetaData {
        private LocalDate reviewDate;
        private List<Session> pastReviews;
    }

    public class Session {
        private ZonedDateTime reviewStartDateTime;
        private Duration duration;
        private ReviewQuality quality;
    }

    /**
     * Creates a new ReviewTracker based on a given document, with all cards unreviewed
     */

    public ReviewTracker(Document document) {
        
    }

    private Map<LocalDate, List<Card>> reviewTimes;
    private Map<Card, MetaData> metadata;




    public void markReviewed(Card card, ReviewQuality quality)

    public List<Card> getCurrentReviews() {


    }

    public void merge(ReviewTracker other)

}
