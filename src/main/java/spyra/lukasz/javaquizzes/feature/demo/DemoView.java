package spyra.lukasz.javaquizzes.feature.demo;

import lombok.Getter;

import java.util.UUID;

/**
 * DTO for demo quiz entity used to present details in controller for demo selection
 */
@Getter
final class DemoView {

    private final UUID id;

    private final String title;

    private final int maxScore;

    private final String created;

    private final String updated;

    private DemoView(final DemoView.Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.maxScore = builder.score;
        this.created = builder.created;
        this.updated = builder.updated;
    }

    static class Builder {

        private UUID id;
        private String title;
        private int score;
        private String created;
        private String updated;

        Builder withId(UUID id) {
            this.id = id;
            return this;
        }

        Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        Builder withScore(int score) {
            this.score = score;
            return this;
        }

        Builder withCreated(String created) {
            this.created = created;
            return this;
        }

        Builder withUpdated(String updated) {
            this.updated = updated;
            return this;
        }

        DemoView build() {
            return new DemoView(this);
        }
    }
}
