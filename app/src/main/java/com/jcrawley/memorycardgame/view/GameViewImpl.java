package com.jcrawley.memorycardgame.view;

import android.animation.Animator;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import androidx.annotation.NonNull;

import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.card.CardFaceImages;
import com.jcrawley.memorycardgame.view.animation.CardAnimator;
import com.jcrawley.memorycardgame.service.game.CardLayoutManager;
import com.jcrawley.memorycardgame.service.game.Game;
import com.jcrawley.memorycardgame.service.GameService;
import com.jcrawley.memorycardgame.view.utils.BitmapLoader;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class GameViewImpl implements GameView {

    private final MainActivity mainActivity;
    private final BitmapLoader bitmapLoader;
    private final AtomicBoolean isFlipBackInitiated = new AtomicBoolean(false);
    private CardLayoutManager cardLayoutManager;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private CardAnimator cardAnimator;
    private final CardBackManager cardBackManager;
    private ScheduledFuture <?> flipBackFuture;
    private final CardFaceImages cardFaceImages;

    public GameViewImpl(MainActivity mainActivity, CardFaceImages cardFaceImages){
        this.mainActivity = mainActivity;
        this.cardFaceImages = cardFaceImages;
        bitmapLoader = mainActivity.getBitmapLoader();
        this.cardBackManager = mainActivity.getCardBackManager();
    }


    @Override
    public void init(CardLayoutManager cardLayoutManager, CardAnimator cardAnimator){
        this.cardLayoutManager = cardLayoutManager;
        this.cardAnimator = cardAnimator;
    }


    @Override
    public void addCardViews(List<Card> cards, Consumer<Integer> clickConsumer){
        addCardViews(cards, clickConsumer, true);
    }


    public void addCardViews(List<Card> cards, Consumer<Integer> clickConsumer, boolean isVisible){
        int numberOfCards = cards.size();
        cardLayoutManager.addViewsFor(cards, clickConsumer, isVisible);

        for(int i = 0; i < numberOfCards; i++){
            var card = cards.get(i);
            ImageView cardView = cardLayoutManager.getImageViewAt(i);
            if(card.isFaceDown()){
                setFaceDown(cardView);
            }
            else{
                setBitmapForCardFace(cardView, card);
            }
        }
    }


    @Override
    public void flipOver(Card card, boolean isSecondCardSelected){
        ImageView cardView = getImageViewFor(card);
        Animator.AnimatorListener halfWayFlip = createAnimatorListener(() -> onFinishedHalfFlip(cardView, card, isSecondCardSelected));
        animateCardFlip(cardView, 1, halfWayFlip);
    }


    private void onFinishedHalfFlip(ImageView cardView, Card card, boolean isSecondCard){
        Animator.AnimatorListener fullWayFlippedListener = createAnimatorListener(() -> {
            cardView.clearAnimation();
            if(isSecondCard){
                checkCards();
            }
        });
        cardView.clearAnimation();
        setBitmapForCardFace(cardView, card);
        animateCardFlip(cardView, 2, fullWayFlippedListener);
    }


    private void checkCards(){
        GameService gameService = mainActivity.getGameService();
        if(gameService != null){
            gameService.getGame().checkCards(true);
        }
    }


    @Override
    public void quickFlip(Card card) {
        ImageView cardView = getImageViewFor(card);
        setBitmapForCardFace(cardView, card);
        new Handler(Looper.getMainLooper()).post(() ->
            cardView.animate()
                    .rotationY(180)
                    .setDuration(1)
                    .start()
        );
    }


    @Override
    public void displayResults(int numberOfTurns, int currentRecord, int delay) {
        mainActivity.displayResults(numberOfTurns, currentRecord, delay);
    }


    @Override
    public void setTitleWithTurns(int numberOfTurns) {
        mainActivity.setTitleWithTurns(numberOfTurns);
    }


    @Override
    public void showNewGameLayout(){
        mainActivity.showNewGameDialog();
    }


    private void resetTurnState(){
        getGame().ifPresent(Game::resetTurnState);
    }


    private Optional<Game> getGame(){
        GameService gameService = mainActivity.getGameService();
        if(gameService != null){
            Game game = gameService.getGame();
            if(game != null){
                return Optional.of(game);
            }
        }
        return Optional.empty();
    }


    @Override
    public void swipeInCardsAfterDelay(List<Card> cards, Consumer<Integer> onClickConsumer){
        addCardViews(cards, onClickConsumer, false);
        setAllCardsFaceDown();
        handleRandomCardAssignment();
        int initialDelay = getInt(R.integer.swipe_in_cards_initial_delay);
        new Handler(Looper.getMainLooper()).postDelayed(this::swipeInCards, initialDelay);
    }


    private void handleRandomCardAssignment(){
        cardBackManager.chooseNewRandomBack();
        if(cardBackManager.isRandomEnabled()){
            switchBacksOnFaceDownCards();
        }
    }


    @Override
    public void swipeOut(Card card) {
        ImageView cardView = getImageViewFor(card);
        cardAnimator.swipeOut(cardView);
    }


    private void swipeInCards(){
        cardAnimator.swipeInAll(cardLayoutManager.getCardViews());
    }


    @Override
    public void flipBothCardsBackAfterDelay(Card card1, Card card2){
        flipBackFuture = scheduledExecutorService.schedule(
                ()-> flipBothCardsBack(card1, card2, 200),
                getInt(R.integer.flip_cards_back_delay),
                TimeUnit.MILLISECONDS);
    }


    public void flipBothCardsBack(Card card1, Card card2, int secondFlipBackDelay){
        if(isFlipBackInitiated.get()){
            return;
        }
        isFlipBackInitiated.set(true);
        cancelFlipBackFuture();
        mainActivity.runOnUiThread(()->{
            flipCardBack(card1, 0 );
            flipCardBack(card2, secondFlipBackDelay);
            resetTurnState();
        });
    }


    private void flipCardBack(Card card, int delay) {
        ImageView cardView = cardLayoutManager.getImageViewAt(card.getPosition());
        var onFullWayFlippedBackListener = createAnimatorListener( () -> onCardFullyFlippedBack(cardView));
        var onHalfWayFlippedBackListener = createAnimatorListener( () -> onHalfWayFlippedBack(cardView, card, onFullWayFlippedBackListener));
        animateCardFlip(cardView, 1, onHalfWayFlippedBackListener, delay);
    }


    private ImageView getImageViewFor(Card card){
        return cardLayoutManager.getImageViewAt(card.getPosition());
    }


    private void onCardFullyFlippedBack(ImageView cardView){
        isFlipBackInitiated.set(false);
        cardView.clearAnimation();
    }


    private void onHalfWayFlippedBack(ImageView cardView, Card card, Animator.AnimatorListener fullWayFlippedBackListener){
        setBitmapForCardFace(cardView, card);
        setFaceDown(cardView);
        animateCardFlip(cardView, 0, fullWayFlippedBackListener);
    }


    private void cancelFlipBackFuture(){
        if(flipBackFuture != null
                && !flipBackFuture.isCancelled()
                && !flipBackFuture.isDone()){
            flipBackFuture.cancel(false);
        }
    }


    public void setAllCardsFaceDown(){
        for(ImageView card : cardLayoutManager.getCardViews()){
            setFaceDown(card);
        }
    }


    public void setFaceDown(ImageView imageView){
        cardBackManager.setCardBackOf(imageView);
    }


    private void animateCardFlip(ImageView cardView, int rotationMultiplier, Animator.AnimatorListener onFinishedListener) {
        animateCardFlip(cardView, rotationMultiplier, onFinishedListener, 0);
    }


    private void animateCardFlip(ImageView cardView, int rotationMultiplier, Animator.AnimatorListener onFinishedListener, int startDelay){
        cardView.animate()
                .rotationY(90 * rotationMultiplier)
                .setDuration(140)
                .setStartDelay(startDelay)
                .setListener(onFinishedListener)
                .start();
    }


    private void setBitmapForCardFace(ImageView cardView, Card card){
        int imageId = cardFaceImages.getImageIdFor(card);
        if(imageId == -1){
            return;
        }
        bitmapLoader.setCardFace(cardView, imageId);
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


    public void switchBacksOnFaceDownCards(){
        getGame().ifPresent(game ->{
            int firstSelectedPosition = game.getFirstSelectedPosition();
            List<ImageView> views = cardLayoutManager.getCardViews();
            for(int i = 0; i < views.size(); i++){
                if(i == firstSelectedPosition){
                    continue;
                }
                cardBackManager.setCardBackOf(views.get(i));
            }
        });
    }


    private int getInt(int resId){
        return mainActivity.getResources().getInteger(resId);
    }
}
