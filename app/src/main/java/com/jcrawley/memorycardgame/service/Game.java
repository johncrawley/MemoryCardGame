package com.jcrawley.memorycardgame.service;

import android.os.Handler;
import android.os.Looper;

import com.jcrawley.memorycardgame.game.TurnState;
import com.jcrawley.memorycardgame.GamePreferences;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardFactory;
import com.jcrawley.memorycardgame.view.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private final GamePreferences gamePreferences;
    private final CardFactory cardFactory;
    private final AtomicBoolean hasFlipBackAlreadyBeenInitiated = new AtomicBoolean();
    private List<Card> cards = new ArrayList<>();
    private TurnState turnState = TurnState.NOTHING_SELECTED;
    private int numberOfTurns, numberOfCards;
    private int firstSelectedPosition = -1;
    private int secondSelectedPosition = -1;
    private int numberOfRemainingCards;
    private boolean isAlreadyInitialised;

    private Card firstSelectedCard, secondSelectedCard;
    
    private GameView gameView;


    public Game(GamePreferences gamePreferences){
        cardFactory = new CardFactory();
        ensureUnavailableCardsAreInvisible();
        this.gamePreferences = gamePreferences;
        initModel();
    }

    private void log(String msg){
        System.out.println("^^^ Game: " + msg);
    }


    public void setView(GameView gameView){
        log("************* entered setView() ****************");
        this.gameView = gameView;
        if(cards == null || cards.isEmpty()){
            initDeckOfCards();
        }
        gameView.addCardViews(cards, this::notifyClickOnPosition);
        quickFlipFirstSelectedCard();
    }


    private void quickFlipFirstSelectedCard(){
        if(turnState == TurnState.FIRST_CARD_SELECTED){
            gameView.quickFlip(firstSelectedCard);
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
    }


    public void startAgain(){
        initDeckOfCards();
        gameView.swipeInCardsAfterDelay(cards, this::notifyClickOnPosition);
    }


    private void initDeckOfCards(){
        cards = cardFactory.getCards(gamePreferences.getNumberOfCards());
        numberOfTurns = 0;
        numberOfCards = cards.size();
        numberOfRemainingCards = numberOfCards;
        resetTurnState();
        initEachCard();
        log("initDeckOfCards() cards size: " + cards.size());
    }


    private void initEachCard(){
        for(Card card : cards){
            card.init();
        }
    }


    public void resetTurnState(){
        turnState = TurnState.NOTHING_SELECTED;
    }


    private boolean areCardsMatching(){
        return firstSelectedCard.getRank() == secondSelectedCard.getRank();
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


    private void handleFirstSelection(int position){
        log("entered handleFirstSelection() position: " + position);
        gameView.setTitleWithTurns(++numberOfTurns);
        turnState = TurnState.FIRST_CARD_SELECTED;
        firstSelectedPosition = position;
        firstSelectedCard = cards.get(position);
        flipCard(position);
        gameView.flipOver(firstSelectedCard, false);
    }


    private void handleSecondSelection(int position){
        log("entered handleSecondSelection() position: " + position);
        if(position == firstSelectedPosition){
            return;
        }
        turnState = TurnState.SECOND_CARD_SELECTED;
        secondSelectedPosition = position;
        secondSelectedCard = cards.get(position);
        flipCard(position);
        hasFlipBackAlreadyBeenInitiated.set(false);
        gameView.flipOver(secondSelectedCard, true);
    }


    private void handleClickAfterSecondSelection(int position){
        immediatelyFlipBackBothCardsIfNoMatch();
        if(position != firstSelectedPosition && position != secondSelectedPosition){
            resetTurnState();
            notifyClickOnPosition(position);
        }
    }


    public void immediatelyFlipBackBothCardsIfNoMatch(){
        if(turnState != TurnState.SECOND_CARD_SELECTED){
            return;
        }

        if(!areCardsMatching()){
            gameView.flipBothCardsBack(firstSelectedCard, secondSelectedCard, 0);
            firstSelectedCard.setFaceDown();
            secondSelectedCard.setFaceDown();
        }
    }


    public void checkCards(boolean hasSecondCardBeenTurnedOver){
        if(!hasSecondCardBeenTurnedOver
                || firstSelectedCard == null
                || secondSelectedCard == null){
            return;
        }
        if(areCardsMatching()){
            removeSelectedCards();
        }
        else{
            gameView.flipBothCardsBackAfterDelay(firstSelectedCard, secondSelectedCard);
            firstSelectedCard.setFaceDown();
            secondSelectedCard.setFaceDown();
        }
    }

    private void flipCard(int position){
        log("entered flipCard()");
        if(cards == null || position >= cards.size()){
            return;
        }
        cards.get(position).flipCard();
    }


    public int getFirstSelectedPosition(){
        return turnState == TurnState.NOTHING_SELECTED ? -1 : firstSelectedCard.getPosition();
    }


    private void displayResults(){
        int currentRecord = gamePreferences.getCurrentTurnsRecordFromPreferences(numberOfCards);
        if(numberOfTurns < currentRecord){
            gamePreferences.saveNewTurnsRecord(numberOfTurns, numberOfCards);
        }
        gameView.displayResults(numberOfTurns, currentRecord );
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
        gameView.swipeOut(card);
        card.setUnavailable();
        card.setVisible(false);
    }

}
