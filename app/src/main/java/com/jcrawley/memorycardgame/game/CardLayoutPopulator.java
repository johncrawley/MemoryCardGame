package com.jcrawley.memorycardgame.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.MainViewModel;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.CardBackManager;

import java.util.ArrayList;
import java.util.List;

public class CardLayoutPopulator {

    private int cardWidth, cardHeight;
    private final Activity activity;
    private View.OnClickListener onClickListener;
    private int numberOfCards;
    private int cardsAdded;
    private final Game game;
    private List<ImageView> imageViews;
    private boolean hasRun = false;
    private final LinearLayout parentLayout;
    private int numberOfCardsPerRow;
    private int numberOfRows;
    private boolean isFirstRun = true;
    private int padding;
    private final MainViewModel viewModel;
    private final CardBackManager cardBackManager;

    @SuppressLint("UseCompatLoadingForDrawables")
    public CardLayoutPopulator(MainActivity activity, LinearLayout parentLayout, Game game, CardBackManager cardBackManager){
        this.activity = activity;
        this.viewModel = activity.getViewModel();
        this.parentLayout = parentLayout;
        this.game = game;
        this.cardBackManager = cardBackManager;
        numberOfCards = game.getNumberOfCards();
        imageViews = new ArrayList<>(numberOfCards);

        createClickListener();
    }


    public void addCardViews(){
        if(hasRun){
            return;
        }
        hasRun = true;
        cardsAdded = 0;
        setDimensions();
        addCardsToParent();
        isFirstRun = false;
    }


    private int getInt(int resId){
        return activity.getResources().getInteger(resId);
    }


    public void addCardViews(int numberOfCards){
        hasRun = false;
        parentLayout.removeAllViewsInLayout();
        this.numberOfCards = numberOfCards;
        imageViews = new ArrayList<>(numberOfCards);
        addCardViews();
    }


    private void setDimensions(){
        int parentWidth = parentLayout.getWidth();
        int parentHeight = parentLayout.getHeight();
        int reductionOffset = 0;
        do {
            calculateCardAndGridDimensions(parentWidth, parentHeight, reductionOffset);
            reductionOffset += 3;
        }
        while(numberOfCardsPerRow * numberOfRows < numberOfCards);
        createPadding();
    }


    private void calculateCardAndGridDimensions(int parentWidth, int parentHeight, int reductionOffset){
        float parentArea = parentWidth * parentHeight;
        float maxAreaPerCard = parentArea / numberOfCards;
        cardWidth = (int)Math.floor(Math.sqrt(maxAreaPerCard/ 2f)) + 10;
        cardWidth -= reductionOffset;
        cardHeight = (int)(cardWidth * 1.5);

        numberOfCardsPerRow = parentWidth / cardWidth;
        numberOfRows = (parentHeight / cardHeight);
    }


    public List<ImageView> getImageViews(){
        return imageViews;
    }


    private void addCardsToParent(){
        for(int i = 0; i < numberOfRows; i++){
            addRowOfCards(numberOfCardsPerRow);
        }
    }


    private void addRowOfCards(int numberOfCardsPerRow){
        LinearLayout rowLayout = new LinearLayout(activity);
        for(int i=0; i< numberOfCardsPerRow; i++){
            if(cardsAdded < numberOfCards){
                ImageView imageView = createCard();
                rowLayout.addView(imageView);
            }
        }
        parentLayout.addView(rowLayout);
    }


    private ImageView createCard(){
        ImageView imageView = new ImageView(activity);
        int id = View.generateViewId();
        imageView.setId(id);
        imageViews.add(imageView);
        cardBackManager.setCardBackTo(imageView);
        imageView.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(cardWidth, cardHeight);
        imageView.setTag(R.string.position_tag, cardsAdded);
        imageView.setOnClickListener(onClickListener);
        imageView.setLayoutParams(layoutParams);
        setVisibility(imageView);
        cardsAdded++;
        return  imageView;
    }


    private void setVisibility(ImageView cardView){
        boolean isVisible = viewModel.cards.get(cardsAdded).isVisible() && isFirstRun;
        cardView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }


    private void createPadding(){
        padding = getInt(R.integer.minimum_card_padding) + (cardWidth / getInt(R.integer.card_width_padding_divisor));
        if(numberOfCards < 10){
            padding += 10;
        }
    }


    private void createClickListener(){
        onClickListener = view -> {
            if(view.getVisibility() == View.VISIBLE){
                game.notifyClickOnPosition((ImageView)view);
            }
        };
    }

}
