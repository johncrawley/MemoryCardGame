package com.jcrawley.memorycardgame.game;

import static com.jcrawley.memorycardgame.game.TurnState.BOTH_CARDS_FLIPPED_OVER;

import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameModel {

    private final CardFactory cardFactory;
    private final AtomicBoolean hasFlipBackAlreadyBeenInitiated = new AtomicBoolean();
    private List<Card> cards = new ArrayList<>();
    private TurnState turnState = TurnState.NOTHING_SELECTED;
    private int numberOfTurns, numberOfCards;
    private int secondSelectedPosition = -1;
    private int numberOfRemainingCards;
    private Card firstSelectedCard;
    private Card secondSelectedCard;

    public Card getFirstSelectedCard() {
        return firstSelectedCard;
    }


    public Card getSecondSelectedCard() {
        return secondSelectedCard;
    }


    public GameModel(){
        cardFactory = new CardFactory();
    }


    public boolean hasCards(){
        return cards != null && !cards.isEmpty();
    }


    public boolean hasNoCards(){
        return cards == null || cards.isEmpty();
    }


    public void destroyCards(){
        cards = null;
        setTurnState(TurnState.AWAITING_NEW_GAME);
    }


    public void initDeckOfCards(int numberOfCards){
        numberOfTurns = 0;
        this.numberOfCards = numberOfCards;
        cards = cardFactory.getCards(numberOfCards);
        numberOfRemainingCards = numberOfCards;
        resetTurnState();
        initEachCard();
    }


    public void setBothSelectedCardsFaceDown(boolean isSimultaneousFlip){
        if(!isSimultaneousFlip){
            setTurnState(BOTH_CARDS_FLIPPED_OVER);
        }
        firstSelectedCard.setFaceDown();
        secondSelectedCard.setFaceDown();
    }


    public void selectFirstPosition(int position){
        setTurnState(TurnState.FIRST_CARD_SELECTED);
        firstSelectedCard = cards.get(position);
        flipCard(position);
    }


    public void removeSelectedCards(){
        removeCard(firstSelectedCard);
        removeCard(secondSelectedCard);
        resetTurnState();
    }


    private void removeCard(Card card){
        numberOfRemainingCards--;
        card.setUnavailable();
        card.setVisible(false);
    }


    public void selectSecondPosition(int position){
        setTurnState(TurnState.SECOND_CARD_IS_FLIPPING_OVER);
        setSecondSelectedPosition(position);
        secondSelectedCard = cards.get(position);
        flipCard(position);
        hasFlipBackAlreadyBeenInitiated.set(false);
    }


    private void initEachCard(){
        for(Card card : cards){
            card.init();
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


    public boolean hasRemainingCards(){
        return numberOfRemainingCards > 0;
    }


    public int getSecondSelectedPosition(){
        return secondSelectedPosition;
    }

    public void setSecondSelectedPosition(int position){
        secondSelectedPosition = position;
    }

    public List<Card> getCards(){
        return cards;
    }

    public TurnState getTurnState(){
        return turnState;
    }

    public int getNumberOfTurns(){
        return numberOfTurns;
    }

    public void incNumberOfTurns(){
        numberOfTurns++;
    }

    public int getNumberOfCards(){
        return numberOfCards;
    }

    public void resetTurnState(){
        setTurnState(TurnState.NOTHING_SELECTED);
    }


    public void setTurnState(TurnState turnState){
        this.turnState = turnState;
    }


    public boolean areCardsMatching(){
        return firstSelectedCard.getRank() == secondSelectedCard.getRank();
    }


}
