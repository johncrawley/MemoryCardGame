package com.jcrawley.memorycardgame.view;

import android.widget.ImageView;


import com.jcrawley.memorycardgame.card.Card;

import java.util.List;

public interface GameView {

   void swipeInAll(List<ImageView> cards);
   void swipeInCardsAfterDelay();
   void swipeOut(Card card);

   void flipBothCardsBack(Card card1, Card card2);
   void flipBothCardsBack(Card card1, Card card2, int secondFlipBackDelay);
   void flipBothCardsBackAfterDelay(Card card1, Card card2);
   void quickFlip(Card card);
   void flipOver(Card card, boolean isSecondCardSelected);

   void addCardViews(List<Card> cards);
   void addCardViews(boolean shouldBackCard);
   void setAllCardsFaceDown();
   void displayResults(int numberOfTurns, int currentRecord);
   void setTitleWithTurns(int numberOfTurns);
}
