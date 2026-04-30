package com.jcrawley.memorycardgame.game;

public enum TurnState {
    AWAITING_NEW_GAME,
    NOTHING_SELECTED,
    FIRST_CARD_SELECTED,
    BOTH_CARDS_FLIPPED_OVER,
    SECOND_CARD_IS_FLIPPING_OVER,
    GAME_OVER
}
