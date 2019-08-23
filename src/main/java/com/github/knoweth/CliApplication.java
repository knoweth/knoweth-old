package com.github.knoweth;

import com.github.knoweth.common.data.Card;
import com.github.knoweth.common.reviewing.AnkiAlgorithm;
import com.github.knoweth.common.reviewing.ReviewQuality;
import com.github.knoweth.common.reviewing.ReviewTracker;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class CliApplication {
    public static void main(String[] args) {
        System.out.println("Knoweth - Command-Line Reviewing Interface");

        ReviewTracker tracker = new ReviewTracker(new AnkiAlgorithm());
        Card card = new Card("What is 2 + 2?", "5", "random-id");

        Scanner in = new Scanner(System.in);
        tracker.markReviewed(card, Duration.ZERO, ReviewQuality.AGAIN);

        while (true) {
            System.out.println(":: FRONT ::");
            System.out.println(card.getFront());
            System.out.println(":::::::::::");
            System.out.println("Click to show back...");
            in.nextLine();
            System.out.println("::: BACK ::");
            System.out.println(card.getBack());
            System.out.println(":::::::::::");
            ReviewQuality quality = null;
            do {
                System.out.println();
                System.out.print("Action [Again, Hard, Good, Easy]: ");
                String action = in.nextLine();
                switch (action.toLowerCase()) {
                    case "easy":
                        quality = ReviewQuality.EASY;
                        break;
                    case "good":
                        quality = ReviewQuality.GOOD;
                        break;
                    case "hard":
                        quality = ReviewQuality.HARD;
                        break;
                    case "again":
                        quality = ReviewQuality.AGAIN;
                        break;
                    default:
                        System.out.println("Please say either again, hard, good, or easy.");
                        break;
                }
            } while (quality == null);
            // TODO mark duration
            tracker.markReviewed(card, Duration.ZERO, quality);

            System.out.println();
            System.out.println("Next review date: " + tracker.getNextReviewDate(card).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)));
            List<Card> reviews = tracker.getCurrentReviews();

            if (reviews.size() == 0) {
                System.out.println("All reviews done. Press enter to keep reviewing, or any key + enter to exit.");
                String input = in.nextLine();
                if (!input.equals("")) {
                    break;
                }
            }
        }

        System.out.println("All reviews done for today!");
    }
}
