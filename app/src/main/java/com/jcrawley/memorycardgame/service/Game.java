package com.jcrawley.memorycardgame.service;

import android.os.Handler;
import android.os.Looper;

import com.jcrawley.memorycardgame.game.TurnState;
import com.jcrawley.memorycardgame.GamePreferences;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardFactory;
import com.jcrawley.memorycardgame.view.GameView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private final GamePreferences gamePreferences;
    private boolean isFirstRunSinceCreate;
    private final CardFactory cardFactory;
    private final AtomicBoolean hasFlipBackAlreadyBeenInitiated = new AtomicBoolean();
    private List<Card> cards = new ArrayList<>();
    private TurnState turnState;
    private int numberOfTurns, numberOfCards;
    private int firstSelectedPosition = -1;
    private int secondSelectedPosition = -1;
    private int numberOfRemainingCards;
    private boolean isAlreadyInitialised;

    private Card firstSelectedCard, secondSelectedCard;
    
    private GameView gameView;


    public Game(GamePreferences gamePreferences){
        cardFactory = new CardFactory();
        isFirstRunSinceCreate = true;
        ensureUnavailableCardsAreInvisible();
        this.gamePreferences = gamePreferences;
        initModel();
    }


    public void setView(GameView gameView){
        this.gameView = gameView;
        if(cards == null){
            initDeckOfCards();
        }
        else{
            gameView.addCardViews(cards);
        }
    }


    private void ensureUnavailableCardsAreInvisible(){
        if(cards == null){
            return;
        }
        for(Card card : cards){
            if(card.isUnavailable()){
                card.setVisible(false);
            }
        }
    }


    private void initModel(){
        if(isAlreadyInitialised){
            gameView.setTitleWithTurns(numberOfTurns);
            return;
        }
        isAlreadyInitialised = true;
        initDeckOfCards();
    }


    public void startAgain(){
        initDeckOfCards();
        gameView.swipeInCardsAfterDelay();
    }


    private void initDeckOfCards(){
        cards = cardFactory.getCards(gamePreferences.getNumberOfCards());
        numberOfTurns = 0;
        numberOfCards = cards.size();
        numberOfRemainingCards = numberOfCards;
        resetTurnState();
        initEachCard();
        gameView.addCardViews(cards);
    }

    private void initEachCard(){
        for(Card card : cards){
            card.init();
        }
    }


    public void initCards2(){
        if(isFirstRunSinceCreate){
            boolean shouldCardBackBeRefreshed = !isAlreadyInitialised;
            gameView.addCardViews(shouldCardBackBeRefreshed);
            if(turnState == TurnState.FIRST_CARD_SELECTED){
                gameView.quickFlip(getFirstSelectedCard());
            }
        }
        isFirstRunSinceCreate = false;
    }


    private Card getFirstSelectedCard(){
        return cards.get(firstSelectedPosition);
    }


    private Card getSecondSelectedCard(){
        return cards.get(secondSelectedPosition);
    }


    public void resetTurnState(){
        turnState = TurnState.NOTHING_SELECTED;
    }


    public void immediatelyFlipBackBothCardsIfNoMatch(){
        if(firstSelectedPosition < 0
                || secondSelectedPosition < 0
                || turnState != TurnState.SECOND_CARD_SELECTED){
            return;
        }
        Card card1 = cards.get(firstSelectedPosition);
        Card card2 = cards.get(secondSelectedPosition);
        if(!areCardsMatching()){
            gameView.flipBothCardsBack(card1, card2, 0);
        }
    }


    private boolean areCardsMatching(){
        Card card1 = cards.get(firstSelectedPosition);
        Card card2 = cards.get(secondSelectedPosition);
        return card1.getRank() == card2.getRank();
    }



    public void shuffleCards(){
        Collections.shuffle(cards);
    }


    public int getNumberOfCards(){
        return numberOfCards;
    }


    public void notifyClickOnPosition(int position){
        if(cards == null  || cards.size() <= position){
            return;
        }
        Card card = cards.get(position);
        if (card.isUnavailable()) {
            return;
        }
        switch(turnState){
            case NOTHING_SELECTED: handleFirstSelection(position); break;
            case FIRST_CARD_SELECTED:handleSecondSelection(position); break;
            case SECOND_CARD_SELECTED: handleClickAfterSecondSelection(position); break;
            default: break;
        }
    }


    private void handleClickAfterSecondSelection(int position){
        immediatelyFlipBackBothCardsIfNoMatch();
        if(position != firstSelectedPosition && position != secondSelectedPosition){
            resetTurnState();
            notifyClickOnPosition(position);
        }
    }


    private void handleFirstSelection(int position){
        gameView.setTitleWithTurns(++numberOfTurns);
        turnState = TurnState.FIRST_CARD_SELECTED;
        firstSelectedPosition = position;
        firstSelectedCard = cards.get(position);
        flipCard(position);
        gameView.flipOver(getFirstSelectedCard(), false);
    }


    private void handleSecondSelection(int position){
        if(position == firstSelectedPosition){
            return;
        }
        turnState = TurnState.SECOND_CARD_SELECTED;
        secondSelectedPosition = position;
        secondSelectedCard = cards.get(position);
        flipCard(position);
        hasFlipBackAlreadyBeenInitiated.set(false);
        gameView.flipOver(getSecondSelectedCard(), true);
    }


    private void flipCard(int position){
        if(cards == null || position >= cards.size()){
            return;
        }
        cards.get(position).flipCard();
    }


    public boolean isCardsListValidFor(int position){
        return cards != null && cards.size() > position;
    }


    private void displayResults(){
        int currentRecord = gamePreferences.getCurrentTurnsRecordFromPreferences(numberOfCards);
        if(numberOfTurns < currentRecord){
            gamePreferences.saveNewTurnsRecord(numberOfTurns, numberOfCards);
        }
        gameView.displayResults(numberOfTurns, currentRecord );
    }


    public void checkCards(boolean hasSecondCardBeenTurnedOver){
        if(!hasSecondCardBeenTurnedOver
                || firstSelectedCard == null
                || secondSelectedCard == null){
            return;
        }
        if(cardsMatch()){
            removeSelectedCards();
        }
        else{
            gameView.flipBothCardsBackAfterDelay(getFirstSelectedCard(), getSecondSelectedCard());
        }
    }


    private void removeSelectedCards(){
        removeCard(firstSelectedCard);
        removeCard(secondSelectedCard);
        resetTurnState();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            numberOfRemainingCards -= 2;
            if(numberOfRemainingCards <= 0){
                displayResults();
            }
        }, 1000);
    }


    private void removeCard(Card card){
        if(isCardsListValidFor(card.getPosition())){
            card.setUnavailable();
        }
        gameView.swipeOut(card);
        cards.get(firstSelectedPosition).setVisible(false);
    }


    private boolean cardsMatch(){
        return getFirstSelectedCard().getRank() == getSecondSelectedCard().getRank();
    }


/*
    private boolean areCurrentCardsDifferent(int position1, int position2){
        return position1 != getPositionTag(firstSelectedCard) && position2 != getPositionTag(secondSelectedCard);
    }
*/


}
