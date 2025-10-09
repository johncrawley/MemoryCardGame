package com.jcrawley.memorycardgame.game;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.jcrawley.memorycardgame.utils.BitmapLoader;
import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.MainViewModel;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.GamePreferences;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.card.CardFactory;
import com.jcrawley.memorycardgame.card.DeckSize;
import com.jcrawley.memorycardgame.utils.GameUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class OldGame {

    private ImageView firstSelectedCard, secondSelectedCard;
    private final BitmapLoader bitmapLoader;
    private CardLayoutManager cardLayoutManager;
    private final CardAnimator cardAnimator;
    private final GamePreferences recordKeeper;
    private final MainActivity mainActivity;
    private final Context context;
    private final MainViewModel viewModel;
    private boolean isFirstRunSinceCreate;
    private final CardBackManager cardBackManager;
    private final CardFactory cardFactory;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final AtomicBoolean hasFlipBackAlreadyBeenInitiated = new AtomicBoolean();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    public OldGame(MainActivity mainActivity, CardBackManager cardBackManager, BitmapLoader bitmapLoader, int screenWidth){
        this.mainActivity = mainActivity;
        this.cardBackManager = cardBackManager;
        cardFactory = new CardFactory();
        isFirstRunSinceCreate = true;
        viewModel = mainActivity.getViewModel();
        context = mainActivity.getApplicationContext();
        ensureUnavailableCardsAreInvisible();
        this.recordKeeper = mainActivity.getGamePreferences();
        this.bitmapLoader = bitmapLoader;
        initModel();
        cardAnimator = new CardAnimator(screenWidth, context);
        initBackgroundClickListener();
    }


    private void ensureUnavailableCardsAreInvisible(){
        if(viewModel.cards == null){
            return;
        }
        for(Card card : viewModel.cards){
            if(card.isUnavailable()){
                card.setVisible(false);
            }
        }
    }


    public void startAgain(DeckSize deckSize){
        viewModel.numberOfTurns = 0;
        viewModel.numberOfCards = deckSize.getValue();
        viewModel.remainingCards = viewModel.numberOfCards;
        viewModel.cards = cardFactory.getCards(deckSize.getValue());
        //cardLayoutManager.addViewsFor(viewModel.numberOfCards);
        resetTurnState();
        if(viewModel.cards == null){
            return;
        }
        Collections.shuffle(viewModel.cards);
        initCards();
        setAllCardsFaceDown();
        swipeInCardsAfterDelay();
    }


    private void resetTurnState(){
        viewModel.turnState = TurnState.NOTHING_SELECTED;
    }


    private void initCards(){
        for(Card card : viewModel.cards){
            card.init();
        }
    }


    private void swipeInCardsAfterDelay(){
        int initialDelay = mainActivity.getResources().getInteger(R.integer.swipe_in_cards_initial_delay);
        new Handler(Looper.getMainLooper()).postDelayed(this::swipeInCards, initialDelay);
    }


    public void shuffleCards(){
        Collections.shuffle(viewModel.cards);
    }


    public int getNumberOfCards(){
        return viewModel.numberOfCards;
    }


    public void initCards(CardLayoutManager cardLayoutManager){
        if(isFirstRunSinceCreate){
            this.cardLayoutManager = cardLayoutManager;
            boolean shouldCardBackBeRefreshed = !viewModel.isAlreadyInitialised;
            /*
            executorService.execute(()->
                  cardLayoutManager.addViewsFor(shouldCardBackBeRefreshed));


             */
            if(viewModel.turnState == TurnState.FIRST_CARD_SELECTED){
                quickFlipFirstSelectedCard();
            }
        }
        isFirstRunSinceCreate = false;
    }


    public void notifyClickOnPosition(ImageView view){
        int position = getPositionTag(view);
        if(viewModel.cards == null  || viewModel.cards.size() <= position){
            return;
        }
        Card card = viewModel.cards.get(position);
        if (card.isUnavailable()) {
            return;
        }
        switch(viewModel.turnState){
            case NOTHING_SELECTED: handleFirstSelection(view, position); break;
            case FIRST_CARD_SELECTED:handleSecondSelection(view, position); break;
            case SECOND_CARD_SELECTED: handleClickAfterSecondSelection(view, position); break;
            default: break;
        }
    }


    private int getPositionTag(ImageView view){
       return (int)view.getTag(R.string.position_tag);
    }


    private void handleClickAfterSecondSelection(ImageView view, int position){
        immediatelyFlipBackBothCardsIfNoMatch();
        if(position != viewModel.firstSelectedPosition && position != viewModel.secondSelectedPosition){
            resetTurnState();
            notifyClickOnPosition(view);
        }
    }


    public void initBackgroundClickListener(){
        ViewGroup background = mainActivity.findViewById(R.id.cardLayoutHolder);
        if(background == null){
            return;
        }
        background.setOnClickListener(v -> {
            if(viewModel.turnState == TurnState.SECOND_CARD_SELECTED){
                immediatelyFlipBackBothCardsIfNoMatch();
            }
        });
    }


    private void quickFlipFirstSelectedCard(){
        int position = viewModel.firstSelectedPosition;
        List<ImageView> imageViews = cardLayoutManager.getCardViews();
        if(GameUtils.isValidPosition(imageViews, position)){
            firstSelectedCard = imageViews.get(position);
            setBitmapForCard(firstSelectedCard, position);
            firstSelectedCard.animate()
                    .rotationY(180)
                    .setDuration(1)
                    .start();
        }
    }


    public void switchBacksOnFaceDownCards(){
        List<ImageView> cards = cardLayoutManager.getCardViews();
        for(ImageView card: cards){
            if(firstSelectedCard == card){
                continue;
            }
            setCardFaceDown(card);
        }
    }


    private void initModel(){
        if(viewModel.isAlreadyInitialised){
            mainActivity.setTitleWithTurns(viewModel.numberOfTurns);
            return;
        }
        viewModel.isAlreadyInitialised = true;
        viewModel.cards = cardFactory.getCards(recordKeeper.getNumberOfCards());
        resetTurnState();
        assert viewModel.cards != null;
        viewModel.numberOfCards = viewModel.cards.size();
        viewModel.remainingCards = viewModel.numberOfCards;
        shuffleCards();
    }


    private void handleFirstSelection(ImageView view, int position){
        mainActivity.setTitleWithTurns(++viewModel.numberOfTurns);
        viewModel.turnState = TurnState.FIRST_CARD_SELECTED;
        viewModel.firstSelectedPosition = position;
        firstSelectedCard = view;
        flipCard(position);
        flipOver(firstSelectedCard, position, false);
    }


    private void handleSecondSelection(ImageView view, int position){
            if(position == viewModel.firstSelectedPosition){
                return;
            }
            viewModel.turnState = TurnState.SECOND_CARD_SELECTED;
            viewModel.secondSelectedPosition = position;
            flipCard(position);
            secondSelectedCard = view;
            hasFlipBackAlreadyBeenInitiated.set(false);
            flipOver(secondSelectedCard, position, true);
    }


    private void flipCard(int position){
        if(viewModel.cards == null || position >= viewModel.cards.size()){
            return;
        }
        viewModel.cards.get(position).flipCard();
    }


    private String getStr(int resId){
        return context.getString(resId);
    }


    private void removeSelectedCards(){
        removeCard(firstSelectedCard, viewModel.firstSelectedPosition);
        removeCard(secondSelectedCard, viewModel.secondSelectedPosition);
        resetTurnState();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            viewModel.remainingCards -= 2;
            if(viewModel.remainingCards <= 0){
                displayResults();
            }
        }, 1000);
    }


    private void removeCard(View view, int position){
        if(isCardsListValidFor(position)){
            Card card = viewModel.cards.get(position);
            card.setUnavailable();
        }
        cardAnimator.swipeOut(view);
        viewModel.cards.get(viewModel.firstSelectedPosition).setVisible(false);
    }


    public boolean isCardsListValidFor(int position){
        return viewModel.cards != null && viewModel.cards.size() > position;
    }


    private void displayResults(){
        String recordText;
        int currentRecord = recordKeeper.getCurrentTurnsRecordFromPreferences(viewModel.numberOfCards);
        if(viewModel.numberOfTurns < currentRecord){
            recordText = getStr(R.string.results_status_new_record);
            recordKeeper.saveNewTurnsRecord(viewModel.numberOfTurns, viewModel.numberOfCards);
        }
        else if(viewModel.numberOfTurns == currentRecord){
            recordText = getStr(R.string.results_status_matching_record);
        }
        else{
            recordText = getStr(R.string.results_status_current_record) + currentRecord;
        }
        mainActivity.displayResults(viewModel.numberOfTurns, recordText);
    }


    private void swipeInCards(){
        cardAnimator.swipeInAll(cardLayoutManager.getCardViews());
    }


    private void setAllCardsFaceDown(){
        for(ImageView card : cardLayoutManager.getCardViews()){
            setCardFaceDown(card);
        }
    }


    private void setCardFaceDown(ImageView imageView){
        cardBackManager.setCardBackOf(imageView);
    }


    private void flipOver(ImageView cardView, int position, boolean isSecondCardSelected){
        viewModel.currentPosition = position;
        flipCardView(cardView, isSecondCardSelected, position);
    }


    private void flipCardView(ImageView cardView, boolean isSecondCard, int position) {
        Animator.AnimatorListener halfWayFlip = createAnimatorListener(() -> onFinishedHalfFlip(cardView, isSecondCard, position));
        animateCardFlip(cardView, 1, halfWayFlip);
    }


    private void onFinishedHalfFlip(ImageView cardView, boolean isSecondCard, int position){
        Animator.AnimatorListener fullWayFlippedListener = createAnimatorListener(() ->{
            cardView.clearAnimation();
            checkCards(isSecondCard);
        });
        cardView.clearAnimation();
        setBitmapForCard(cardView, position);
        animateCardFlip(cardView, 2, fullWayFlippedListener);
    }


    private void checkCards(boolean hasSecondCardBeenTurnedOver){
        if(!hasSecondCardBeenTurnedOver || firstSelectedCard == null || secondSelectedCard == null){
            return;
        }
        if(cardsMatch()){
            removeSelectedCards();
        }
        else{
            flipBothCardsBackAfterDelay();
        }
    }


    private boolean cardsMatch(){
        Card card1 = viewModel.cards.get(viewModel.firstSelectedPosition);
        Card card2 = viewModel.cards.get(viewModel.secondSelectedPosition);
        return card1.getRank() == card2.getRank();
    }


    private void flipBothCardsBackAfterDelay(){
        int position1 = getPositionTag(firstSelectedCard);
        int position2 = getPositionTag(secondSelectedCard);
        scheduledExecutorService.schedule(
                ()-> flipBothCardsBack(position1, position2, 200),
                getInt(R.integer.flip_cards_back_delay),
                TimeUnit.MILLISECONDS);
    }


    private void immediatelyFlipBackBothCardsIfNoMatch(){
        if(firstSelectedCard == null || secondSelectedCard == null){
            return;
        }
        if(!cardsMatch()){
            int position1 = getPositionTag(firstSelectedCard);
            int position2 = getPositionTag(secondSelectedCard);
            flipBothCardsBack(position1, position2, 0);
        }
    }


    private void flipBothCardsBack(int position1, int position2, int secondFlipBackDelay){
        if(hasFlipBackAlreadyBeenInitiated.get() || areCurrentCardsDifferent(position1, position2)){
            return;
        }
        hasFlipBackAlreadyBeenInitiated.set(true);
        mainActivity.runOnUiThread(()->{
            flipCardBack(firstSelectedCard, 0 );
            flipCardBack(secondSelectedCard, secondFlipBackDelay);
            resetTurnState();
            firstSelectedCard = null;
        });
    }


    private boolean areCurrentCardsDifferent(int position1, int position2){
        return position1 != getPositionTag(firstSelectedCard) && position2 != getPositionTag(secondSelectedCard);
    }


    private void flipCardBack(ImageView cardView, int delay) {
        if(cardView == null){
            return;
        }
        Animator.AnimatorListener onFullWayFlippedBack = createAnimatorListener(cardView::clearAnimation);
        Animator.AnimatorListener onHalfWayFlippedBack = createAnimatorListener( () -> onHalfWayFlippedBack(cardView, onFullWayFlippedBack));
        animateCardFlip(cardView, 1, onHalfWayFlippedBack, delay);
    }


    private void onHalfWayFlippedBack(ImageView cardView, Animator.AnimatorListener fullWayFlippedBackListener){
        if(viewModel.cards == null){
            return;
        }
        setBitmapForCard(cardView, viewModel.currentPosition);
        setCardFaceDown(cardView);
        animateCardFlip(cardView, 0, fullWayFlippedBackListener);
    }


    private void animateCardFlip(ImageView cardView, int rotationMultiplier, Animator.AnimatorListener onFinishedListener) {
        animateCardFlip(cardView, rotationMultiplier, onFinishedListener, 0);
    }


    private void animateCardFlip(ImageView cardView, int rotationMultiplier, Animator.AnimatorListener onFinishedListener, int startDelay){
        long duration = getInt(R.integer.flip_card_duration);
        float halfRotation = getInt(R.integer.flip_card_half_rotation);
        cardView.animate()
                .rotationY(halfRotation * rotationMultiplier)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setListener(onFinishedListener)
                .start();
    }


    private void setBitmapForCard(ImageView cardView, int position){
        int imageId = getImageIdAt(position);
        if(imageId == -1){
            return;
        }
        bitmapLoader.setCardFace(cardView, getImageIdAt(position));
    }


    private int getImageIdAt(int position){
        if(position > viewModel.cards.size()-1){
            return -1;
        }
        return viewModel.cardDeckImages.getImageIdFor(viewModel.cards.get(position));
    }


    private Animator.AnimatorListener createAnimatorListener(Runnable onFinished){
        return new Animator.AnimatorListener() {
            public void onAnimationEnd(@NonNull Animator animator) {
                new Handler(Looper.getMainLooper()).post(onFinished);
            }
            public void onAnimationStart(@NonNull Animator animator) {}
            public void onAnimationCancel(@NonNull Animator animator) {}
            public void onAnimationRepeat(@NonNull Animator animator) {}
        };
    }


    private int getInt(int resId){
        return mainActivity.getResources().getInteger(resId);
    }

}
