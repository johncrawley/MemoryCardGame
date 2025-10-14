package com.jcrawley.memorycardgame.view;

import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.view.animation.CardAnimator;
import com.jcrawley.memorycardgame.service.game.CardLayoutManager;

import java.util.List;
import java.util.function.Consumer;

public interface GameView {

   void init(CardLayoutManager cardLayoutManager, CardAnimator cardAnimator);
   void swipeInCardsAfterDelay(List<Card> cards, Consumer<Integer> onClickConsumer);
   void swipeOut(Card card);
   void flipBothCardsBack(Card card1, Card card2, int secondFlipBackDelay);
   void flipBothCardsBackAfterDelay(Card card1, Card card2);
   void quickFlip(Card card);
   void flipOver(Card card, boolean isSecondCardSelected);
   void addCardViews(List<Card> cards, Consumer<Integer> clickConsumer);
   void displayResults(int numberOfTurns, int currentRecord, int delay);
   void setTitleWithTurns(int numberOfTurns);
   void switchBacksOnFaceDownCards();
   void showNewGameLayout();
   void reloadFirstSelectedCardFace();
}
