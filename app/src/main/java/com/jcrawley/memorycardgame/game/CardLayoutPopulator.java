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
    private final int MAX_NUMBER_OF_CARDS;
    private int currentNumberOfCards;
    private final Game game;
    private final List<ImageView> imageViews;
    private boolean hasRun = false;
    private final LinearLayout parentLayout;
    private int NUMBER_OF_CARDS_PER_ROW;
    private int NUMBER_OF_ROWS;

    @SuppressLint("UseCompatLoadingForDrawables")
    public CardLayoutPopulator(Activity activity, LinearLayout parentLayout, Game game){
        this.activity = activity;
        this.parentLayout = parentLayout;
        this.game = game;
        MAX_NUMBER_OF_CARDS = game.getNumberOfCards();
        imageViews = new ArrayList<>(MAX_NUMBER_OF_CARDS);
        createClickListener();
    }


    public void addCards(){
        if(hasRun){
            return;
        }
        hasRun = true;
        currentNumberOfCards = 0;
        cardBack = BitmapFactory.decodeResource(activity.getResources(), R.drawable.card_back_2);
        setDimensions();
        addCardsToParent();
    }


    private void setDimensions(){

        //int mainLayoutPaddingPixels = activity.getResources().getDimensionPixelSize(R.dimen.main_layout_padding) * 2;
        int parentWidth = parentLayout.getWidth();
        int parentHeight = parentLayout.getHeight();
        int reductionOffset = 0;
        do {
            calculateCardAndGridDimensions(parentWidth, parentHeight, reductionOffset);
            reductionOffset += 3;
        }
        while(NUMBER_OF_CARDS_PER_ROW * NUMBER_OF_ROWS < MAX_NUMBER_OF_CARDS);

        log("setDimensions() cardsPerRow: " + NUMBER_OF_CARDS_PER_ROW
                + " numberOfRows: " + NUMBER_OF_ROWS
                + " maxNumberOfCards: " + MAX_NUMBER_OF_CARDS
                + " reductionOffset: " + reductionOffset);
    }

    private void calculateCardAndGridDimensions(int parentWidth, int parentHeight, int reductionOffset){

        float parentArea = parentWidth * parentHeight;
        float maxAreaPerCard = parentArea / MAX_NUMBER_OF_CARDS;
        cardWidth = (int)Math.floor(Math.sqrt(maxAreaPerCard/ 2f)) + 10;
        cardWidth -= reductionOffset;
        cardHeight = (int)(cardWidth * 1.5);

        NUMBER_OF_CARDS_PER_ROW = parentWidth / cardWidth;
        NUMBER_OF_ROWS = (parentHeight / cardHeight);
    }

    private void log(String msg){
        System.out.println("^^^ CardLayoutPopulator: " + msg);
    }

    public List<ImageView> getImageViews(){
        return imageViews;
    }


    private void addCardsToParent(){
        for(int i = 0; i < NUMBER_OF_ROWS; i++){
            addRowOfCards(NUMBER_OF_CARDS_PER_ROW);
        }
    }

    private void addRowOfCards(int numberOfCardsPerRow){

        LinearLayout rowLayout = new LinearLayout(activity);
        for(int i=0; i< numberOfCardsPerRow; i++){
            if(currentNumberOfCards < MAX_NUMBER_OF_CARDS){
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
        imageView.setPadding(15,15,15,15);
        LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(cardWidth, cardHeight);
        imageView.setTag(R.string.position_tag, currentNumberOfCards);
        imageView.setOnClickListener(onClickListener);
        imageView.setLayoutParams(layoutParams);
        imageView.setVisibility(View.VISIBLE);
        currentNumberOfCards++;
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
