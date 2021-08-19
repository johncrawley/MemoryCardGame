package com.jcrawley.memorycardgame.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcrawley.memorycardgame.R;

public class CardLayoutPopulator {

    private int width, height;
    private final Activity activity;
    private Bitmap cardBack;
    private View.OnClickListener onClickListener;
    private final int MAX_NUMBER_OF_CARDS = 52;
    private int currentNumberOfCards;
    private Game game;

    @SuppressLint("UseCompatLoadingForDrawables")
    public CardLayoutPopulator(Activity activity, LinearLayout parentLayout, Game game){
        this.activity = activity;
        this.parentLayout = parentLayout;
        this.game = game;
        createClickListener();
    }

    private boolean hasRun = false;
    private LinearLayout parentLayout;

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
            System.out.println("Hello Card!");
            game.notifyClickOnPosition(view);
        };
    }

}
