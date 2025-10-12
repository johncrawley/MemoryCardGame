package com.jcrawley.memorycardgame.card;

public enum DeckSize {
    EIGHT(8),
    SIXTEEN(16),
    TWENTY_SIX(26),
    FIFTY_TWO(52);

    private final int value;

    DeckSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
