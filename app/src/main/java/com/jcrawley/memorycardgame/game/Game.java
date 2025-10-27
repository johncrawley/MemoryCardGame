package com.jcrawley.memorycardgame.game;

import com.jcrawley.memorycardgame.view.GamePreferences;
import com.jcrawley.memorycardgame.view.GameView;

public class Game {

    private final GamePreferences gamePreferences;
    private final GameView gameView;
    private final GameModel gameModel;

    public Game(GameModel gameModel, GameView gameView, GamePreferences gamePreferences){
        ensureUnavailableCardsAreInvisible();
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.gamePreferences = gamePreferences;

        switch(gameModel.getTurnState()){
            case AWAITING_NEW_GAME -> gameView.showNewGameLayout();
            case GAME_OVER -> showGameOverOnView(-1);
            default -> initCardsAndUpdateView();
        }
    }

    private void log(String msg){
        System.out.println("^^^ Game: " + msg);
    }

    private void initCardsAndUpdateView(){
        log("entered initCardsAndUpdateView()");
        if(gameModel.hasNoCards()){
            int numberOfCards = gamePreferences.getNumberOfCards();
            log("game model has no cards, initialising deck with "  + numberOfCards + " cards");
            gameModel.initDeckOfCards(numberOfCards);
        }
        gameView.addCardViews(gameModel.getCards(), this::notifyClickOnPosition);
        quickFlipFirstSelectedCard();
        updateNumberOfTurnsOnView();
    }


    public void resetTurnState(){
        gameModel.resetTurnState();
    }


    public void onNewGameLayoutShown(){
        gameModel.destroyCards();
    }


    private void quickFlipFirstSelectedCard(){
        if(gameModel.getTurnState() == TurnState.FIRST_CARD_SELECTED){
            gameView.quickFlip(gameModel.getFirstSelectedCard());
        }
    }


    private void ensureUnavailableCardsAreInvisible(){
        if(gameModel != null && gameModel.hasCards()){
            for(var card : gameModel.getCards()){
                if(card.isUnavailable()){
                    card.setVisible(false);
                }
            }
        }
    }


    public void startAgain(){
        gameModel.initDeckOfCards(gamePreferences.getNumberOfCards());
        gameView.swipeInCardsAfterDelay(gameModel.getCards(), this::notifyClickOnPosition);
    }


    public void notifyClickOnPosition(int position){
        var cards = gameModel.getCards();
        if(cards == null
                || cards.size() <= position
                || cards.get(position).isUnavailable()){
            return;
        }
        switch(gameModel.getTurnState()){
            case NOTHING_SELECTED: handleFirstSelection(position); break;
            case FIRST_CARD_SELECTED: handleSecondSelection(position); break;
            case BOTH_CARDS_FLIPPED_OVER: handleClickAfterBothCardsAreFlipped(position); break;
            default: break;
        }
    }


    private void handleFirstSelection(int position){
        gameModel.incNumberOfTurns();
        gameView.setTitleWithTurns(gameModel.getNumberOfTurns());
        gameModel.selectFirstPosition(position);
        gameView.flipOver(gameModel.getFirstSelectedCard(), false);
    }


    private void handleSecondSelection(int position){
        if(position == gameModel.getFirstSelectedPosition()){
            return;
        }
        gameModel.selectSecondPosition(position);
        gameView.flipOver(gameModel.getSecondSelectedCard(), true);
    }


    private void handleClickAfterBothCardsAreFlipped(int position){
        immediatelyFlipBackBothCardsIfNoMatch();
        clickOnNextCard(position);
    }


    private void clickOnNextCard(int position){
        if(position != -1
                && position != gameModel.getFirstSelectedPosition()
                && position != gameModel.getSecondSelectedPosition()){
            gameModel.resetTurnState();
            notifyClickOnPosition(position);
        }
    }


    public void immediatelyFlipBackBothCardsIfNoMatch(){
        if(gameModel.getTurnState() == TurnState.BOTH_CARDS_FLIPPED_OVER && !gameModel.areCardsMatching()){
            var card1 = gameModel.getFirstSelectedCard();
            var card2 = gameModel.getSecondSelectedCard();
            gameView.flipBothCardsBack(card1, card2, 0);
            gameModel.setBothSelectedCardsFaceDown(true);
        }
    }


    public void checkCards(boolean hasSecondCardBeenTurnedOver){
        if(!hasSecondCardBeenTurnedOver
                || gameModel.getFirstSelectedCard() == null
                || gameModel.getSecondSelectedCard() == null){
            return;
        }
        if(gameModel.areCardsMatching()){
            removeSelectedCards();
            updateNumberOfTurnsOnView();
            showGameOverScreenWhenNoCardsRemain();
        }
        else{
            gameModel.setBothSelectedCardsFaceDown(false);
            gameView.flipBothCardsBackAfterDelay(gameModel.getFirstSelectedCard(), gameModel.getSecondSelectedCard());
        }
    }


    public int getFirstSelectedPosition(){
        return gameModel.getFirstSelectedPosition();
    }


    private void removeSelectedCards(){
        gameView.swipeOut(gameModel.getFirstSelectedCard());
        gameView.swipeOut(gameModel.getSecondSelectedCard());
        gameModel.removeSelectedCards();
    }


    private void updateNumberOfTurnsOnView(){
        int numberOfTurns = gameModel.getNumberOfTurns();
        if(numberOfTurns > 0){
            gameView.setTitleWithTurns(numberOfTurns);
        }
    }


    private void showGameOverScreenWhenNoCardsRemain(){
        if(!gameModel.hasRemainingCards()){
            gameModel.setTurnState(TurnState.GAME_OVER);
            saveScore();
            showGameOverOnView(1000);
        }
    }


    private void showGameOverOnView(int delay){
        gameView.displayResults(gameModel.getNumberOfTurns(), getCurrentRecord(), delay);
    }


    private void saveScore(){
        int currentRecord = getCurrentRecord();
        int numberOfTurns = gameModel.getNumberOfTurns();
        if(numberOfTurns < currentRecord){
            gamePreferences.saveNewTurnsRecord(numberOfTurns, gameModel.getNumberOfCards());
        }
    }


    private int getCurrentRecord(){
        return gamePreferences.getCurrentTurnsRecordFromPreferences(gameModel.getNumberOfCards());
    }

}
