package com.jcrawley.memorycardgame.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcrawley.memorycardgame.R;

import java.util.ArrayList;
import java.util.List;

public class CardLayoutPopulator {

    private int cardWidth, cardHeight;
    private final Activity activity;
    private Bitmap cardBack;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    public CardLayoutPopulator(Activity activity, LinearLayout parentLayout, Game game){
        this.activity = activity;
        this.parentLayout = parentLayout;
        this.game = game;
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
        cardBack = BitmapFactory.decodeResource(activity.getResources(), R.drawable.card_back_2);
        setDimensions();
        addCardsToParent();
        isFirstRun = false;
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
        imageView.setImageBitmap(cardBack);
        int padding = (int) (5 + (cardWidth / 12f));
        imageView.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(cardWidth, cardHeight);
        imageView.setTag(R.string.position_tag, cardsAdded);
        imageView.setOnClickListener(onClickListener);
        imageView.setLayoutParams(layoutParams);
        imageView.setVisibility(isFirstRun ? View.VISIBLE : View.GONE);
        cardsAdded++;
        return  imageView;
    }


    private void createClickListener(){
        onClickListener = view -> {
            if(view.getVisibility() == View.VISIBLE){
                game.notifyClickOnPosition((ImageView)view);
            }
        };
    }

}
