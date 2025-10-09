package com.jcrawley.memorycardgame.view;

import android.widget.ImageView;


import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.game.CardAnimator;
import com.jcrawley.memorycardgame.game.CardLayoutManager;

import java.util.List;
import java.util.function.Consumer;

public interface GameView {

   void swipeInAll(List<ImageView> cards);
   void swipeInCardsAfterDelay();
   void swipeOut(Card card);

   void flipBothCardsBack(Card card1, Card card2, int secondFlipBackDelay);
   void flipBothCardsBackAfterDelay(Card card1, Card card2);
   void quickFlip(Card card);
   void flipOver(Card card, boolean isSecondCardSelected);

   void addCardViews(List<Card> cards, Consumer<Integer> clickConsumer);
   void setAllCardsFaceDown();
   void displayResults(int numberOfTurns, int currentRecord);
   void setTitleWithTurns(int numberOfTurns);

   void init(CardLayoutManager cardLayoutManager, CardAnimator cardAnimator);
}
