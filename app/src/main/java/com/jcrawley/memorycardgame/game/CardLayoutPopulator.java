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

    private int width, height;
    private final Activity activity;
    private Bitmap cardBack;
    private View.OnClickListener onClickListener;
    private final int MAX_NUMBER_OF_CARDS;
    private int currentNumberOfCards;
    private final Game game;
    private List<ImageView> imageViews;
    private boolean hasRun = false;
    private final LinearLayout parentLayout;

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
        cardBack = BitmapFactory.decodeResource(activity.getResources(), R.drawable.card_back_1);
        final int NUMBER_OF_CARDS_PER_ROW = 7;
        final int NUMBER_OF_ROWS = 8;
        width = parentLayout.getWidth() / (NUMBER_OF_CARDS_PER_ROW + 1);
        height = parentLayout.getHeight() / (NUMBER_OF_ROWS + 1);
        System.out.println("Width and height: " + width + ", " + height);

        for(int i=0; i< NUMBER_OF_ROWS; i++){
            addRowOfCards(NUMBER_OF_CARDS_PER_ROW);
        }
    }


    public List<ImageView> getImageViews(){
        return imageViews;
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
        LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(width, height);//mageView.getLayoutParams();
        imageView.setTag(R.string.position_tag, currentNumberOfCards);
        imageView.setOnClickListener(onClickListener);
        imageView.setLayoutParams(layoutParams);
        imageView.setVisibility(View.VISIBLE);
        currentNumberOfCards++;
        return  imageView;
    }


    private void createClickListener(){
        onClickListener = view -> {
            System.out.println("Hello Card! position: " + (int)view.getTag(R.string.position_tag));
            if(view.getVisibility() == View.VISIBLE){
                game.notifyClickOnPosition((ImageView)view);
            }
        };
    }

}
