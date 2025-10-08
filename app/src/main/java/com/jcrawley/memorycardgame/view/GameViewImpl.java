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
import com.jcrawley.memorycardgame.card.CardDeckImages;
import com.jcrawley.memorycardgame.game.CardAnimator;
import com.jcrawley.memorycardgame.game.CardLayoutPopulator;
import com.jcrawley.memorycardgame.service.Game;
import com.jcrawley.memorycardgame.service.GameService;
import com.jcrawley.memorycardgame.utils.BitmapLoader;
import com.jcrawley.memorycardgame.utils.GameUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameViewImpl implements GameView {

    private final MainActivity mainActivity;
    private final BitmapLoader bitmapLoader;
    private final AtomicBoolean hasFlipBackAlreadyBeenInitiated = new AtomicBoolean(false);
    private final CardLayoutPopulator cardLayoutPopulator;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final CardAnimator cardAnimator;
    private int currentPosition;
    private final CardBackManager cardBackManager;
    public CardDeckImages cardDeckImages = new CardDeckImages();


    public GameViewImpl(MainActivity mainActivity, CardLayoutPopulator cardLayoutPopulator){
        this.mainActivity = mainActivity;
        bitmapLoader = mainActivity.getBitmapLoader();
        this.cardLayoutPopulator = cardLayoutPopulator;
        this.cardAnimator = mainActivity.getCardAnimator();
        this.cardBackManager = mainActivity.getCardBackManager();
    }


    private void flipOver(ImageView cardView, Card card, boolean isSecondCardSelected){
        currentPosition = card.getPosition();
        flipCardView(cardView, card, isSecondCardSelected);
    }


    private void flipCardView(ImageView cardView, Card card, boolean isSecondCard) {
        Animator.AnimatorListener halfWayFlip = createAnimatorListener(() -> onFinishedHalfFlip(cardView, card, isSecondCard));
        animateCardFlip(cardView, 1, halfWayFlip);
    }


    private void onFinishedHalfFlip(ImageView cardView, Card card, boolean isSecondCard){
        Animator.AnimatorListener fullWayFlippedListener = createAnimatorListener(() -> {
            cardView.clearAnimation();
            checkCards(isSecondCard);
        });
        cardView.clearAnimation();
        setFaceBitmapFor(cardView, card);
        animateCardFlip(cardView, 2, fullWayFlippedListener);
    }


    private void checkCards(boolean isSecondCard){
        getGame().ifPresent(game -> game.checkCards(isSecondCard));
    }


    @Override
    public void quickFlip(Card card){
        List<ImageView> imageViews = cardLayoutPopulator.getImageViews();
        if(GameUtils.isValidPosition(imageViews, card.getPosition())){
            ImageView cardImageView = imageViews.get(card.getPosition());
            setFaceBitmapFor(cardImageView, card);
            cardImageView.animate()
                    .rotationY(180)
                    .setDuration(1)
                    .start();
        }
    }

    @Override
    public void flipOver(Card card, boolean isSecondCardSelected) {

    }


    @Override
    public void displayResults(int numberOfTurns, int currentRecord) {

    }


    @Override
    public void setTitleWithTurns(int numberOfTurns) {

    }


    public void flipBothCardsBack(Card card1, Card card2, int secondFlipBackDelay){
        if(hasFlipBackAlreadyBeenInitiated.get()){
            return;
        }
        hasFlipBackAlreadyBeenInitiated.set(true);
        mainActivity.runOnUiThread(()->{
            flipCardBack(card1, 0 );
            flipCardBack(card2, secondFlipBackDelay);
            resetTurnState();
            //firstSelectedCard = null;
        });
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
    public void swipeInAll(List<ImageView> cards) {

    }

    @Override
    public void swipeInCardsAfterDelay(){
        int initialDelay = mainActivity.getResources().getInteger(R.integer.swipe_in_cards_initial_delay);
        new Handler(Looper.getMainLooper()).postDelayed(this::swipeInCards, initialDelay);
    }

    @Override
    public void swipeOut(Card card) {

    }



    @Override
    public void flipBothCardsBack(Card card1, Card card2) {

    }


    private void swipeInCards(){
        cardAnimator.swipeInAll(cardLayoutPopulator.getImageViews());
    }


    private void flipCardBack(Card card, int delay) {
        ImageView cardView = cardLayoutPopulator.getImageViewAt(card.getPosition());
        Animator.AnimatorListener onFullWayFlippedBack = createAnimatorListener(cardView::clearAnimation);
        Animator.AnimatorListener onHalfWayFlippedBack = createAnimatorListener( () -> onHalfWayFlippedBack(cardView, card, onFullWayFlippedBack));
        animateCardFlip(cardView, 1, onHalfWayFlippedBack, delay);
    }


    private ImageView getCardAtPosition(int position){
        return cardLayoutPopulator.getImageViews().get(position);
    }


    private void onHalfWayFlippedBack(ImageView cardView, Card card, Animator.AnimatorListener fullWayFlippedBackListener){
        setFaceBitmapFor(cardView, card);
        setFaceDown(cardView);
        animateCardFlip(cardView, 0, fullWayFlippedBackListener);
    }


    @Override
    public void flipBothCardsBackAfterDelay(Card card1, Card card2){
        scheduledExecutorService.schedule(
                ()-> flipBothCardsBack(card1, card2, 200),
                mainActivity.getResources().getInteger(R.integer.flip_cards_back_delay),
                TimeUnit.MILLISECONDS);
    }


    @Override
    public void addCardViews(List<Card> cards){
        int numberOfCards = cards.size();
        cardLayoutPopulator.addCardViews(numberOfCards);

        for(int i = 0; i < numberOfCards; i++){
            Card card = cards.get(i);
            ImageView cardView = cardLayoutPopulator.getImageViewAt(i);
            if(card.isFaceDown()){
                setFaceDown(cardView);
            }
            else{
                setFaceBitmapFor(cardView, card);
            }
        }
    }


    @Override
    public void addCardViews( boolean shouldCardBackBeRefreshed){
        cardLayoutPopulator.addCardViews(shouldCardBackBeRefreshed);
    }


    @Override
    public void setAllCardsFaceDown(){
        for(ImageView card : cardLayoutPopulator.getImageViews()){
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
        long duration = getInt(R.integer.flip_card_duration);
        float halfRotation = getInt(R.integer.flip_card_half_rotation);
        cardView.animate()
                .rotationY(halfRotation * rotationMultiplier)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setListener(onFinishedListener)
                .start();
    }


    private void setFaceBitmapFor(ImageView cardView, Card card){
        int imageId = getImageIdAt(card);
        if(imageId == -1){
            return;
        }
        bitmapLoader.setCardFace(cardView, getImageIdAt(card));
    }


    private int getImageIdAt(Card card){
       // return cardDeckImages.getImageIdFor(cards.get(position));
        return cardDeckImages.getImageIdFor(card);
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
