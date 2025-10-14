package com.jcrawley.memorycardgame.service.game;

import com.jcrawley.memorycardgame.view.GamePreferences;
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

    private Card firstSelectedCard, secondSelectedCard;
    
    private GameView gameView;


    public Game(GamePreferences gamePreferences){
        cardFactory = new CardFactory();
        ensureUnavailableCardsAreInvisible();
        this.gamePreferences = gamePreferences;
    }


    public void setView(GameView gameView){
        this.gameView = gameView;

        switch(turnState){
            case AWAITING_NEW_GAME -> gameView.showNewGameLayout();
            case GAME_OVER -> showGameOverOnView(-1);
            default -> initCardsAndUpdateView();
        }
    }


    private void initCardsAndUpdateView(){
        if(cards == null || cards.isEmpty()){
            initDeckOfCards();
        }
        gameView.addCardViews(cards, this::notifyClickOnPosition);
        quickFlipFirstSelectedCard();
        updateNumberOfTurnsOnView();
    }


    private void updateNumberOfTurnsOnView(){
        if(numberOfTurns > 0){
            gameView.setTitleWithTurns(numberOfTurns);
        }
    }


    public void onNewGameLayoutShown(){
        cards = null;
        setTurnState(TurnState.AWAITING_NEW_GAME);
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
    }


    private void initEachCard(){
        for(Card card : cards){
            card.init();
        }
    }


    public void resetTurnState(){
        setTurnState(TurnState.NOTHING_SELECTED);
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
            case FIRST_CARD_SELECTED: handleSecondSelection(position); break;
            case BOTH_CARDS_FLIPPED_OVER: handleClickAfterBothCardsAreFlipped(position); break;
            default: break;
        }
    }


    private void handleFirstSelection(int position){
        gameView.setTitleWithTurns(++numberOfTurns);
        setTurnState(TurnState.FIRST_CARD_SELECTED);
        firstSelectedPosition = position;
        firstSelectedCard = cards.get(position);
        flipCard(position);
        gameView.flipOver(firstSelectedCard, false);
    }


    private void handleSecondSelection(int position){
        if(position == firstSelectedPosition){
            return;
        }
        setTurnState(TurnState.SECOND_CARD_IS_FLIPPING_OVER);
        secondSelectedPosition = position;
        secondSelectedCard = cards.get(position);
        flipCard(position);
        hasFlipBackAlreadyBeenInitiated.set(false);
        gameView.flipOver(secondSelectedCard, true);
    }


    private void log(String msg){
        System.out.println("^^^ Game: " + msg);
    }


    private void handleClickAfterBothCardsAreFlipped(int position){
        immediatelyFlipBackBothCardsIfNoMatch();
        clickOnNextCard(position);
    }


    private void clickOnNextCard(int position){
        if(position != -1
                && position != firstSelectedPosition
                && position != secondSelectedPosition){
            resetTurnState();
            notifyClickOnPosition(position);
        }
    }


    public void immediatelyFlipBackBothCardsIfNoMatch(){
        if(turnState != TurnState.BOTH_CARDS_FLIPPED_OVER){
            return;
        }
        if(!areCardsMatching()){
            setTurnState(TurnState.IMMEDIATE_FLIP_BACK);
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
            updateNumberOfTurnsOnView();
            resetTurnState();
            showGameOverScreenWhenNoCardsRemain();
        }
        else{
            setTurnState(TurnState.BOTH_CARDS_FLIPPED_OVER);
            gameView.flipBothCardsBackAfterDelay(firstSelectedCard, secondSelectedCard);
            firstSelectedCard.setFaceDown();
            secondSelectedCard.setFaceDown();
        }
    }


    private void flipCard(int position){
        if(cards == null || position >= cards.size()){
            return;
        }
        cards.get(position).flipCard();
    }


    public int getFirstSelectedPosition(){
        return turnState == TurnState.NOTHING_SELECTED ? -1 : firstSelectedCard.getPosition();
    }


    private void removeSelectedCards(){
        removeCard(firstSelectedCard);
        removeCard(secondSelectedCard);
        numberOfRemainingCards -= 2;
    }


    private void showGameOverScreenWhenNoCardsRemain(){
        if(numberOfRemainingCards > 0){
            return;
        }
        setTurnState(TurnState.GAME_OVER);
        saveScore();
        showGameOverOnView(1000);
    }


    private void setTurnState(TurnState turnState){
        log("entered setTurnState():  " + turnState.name());
        this.turnState = turnState;
    }
    

    private void showGameOverOnView(int delay){
        gameView.displayResults(numberOfTurns, getCurrentRecord(), delay);
    }


    private void saveScore(){
        int currentRecord = getCurrentRecord();
        if(numberOfTurns < currentRecord){
            gamePreferences.saveNewTurnsRecord(numberOfTurns, numberOfCards);
        }
    }


    private int getCurrentRecord(){
        return gamePreferences.getCurrentTurnsRecordFromPreferences(numberOfCards);
    }


    private void removeCard(Card card){
        gameView.swipeOut(card);
        card.setUnavailable();
        card.setVisible(false);
    }

}
