package com.jcrawley.memorycardgame.game;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.List;

public class CardAnimator {

    private final int screenWidth;


    public CardAnimator(int screenWidth){
        this.screenWidth = screenWidth;
    }


    void swipeInAll(List<ImageView> cards){
        int delay = 50;
        int delayIncrement = 30;
        for(View card : cards){
            card.setVisibility(View.VISIBLE);
            swipeIn(card, delay);
            delay+=delayIncrement;
        }
    }


    private void swipeIn(View card, int delay){
        float previousElevation = card.getElevation();
        card.setElevation(15);
        Animation animation = new TranslateAnimation(
                screenWidth + 10,
                0,
                0,
                0);
        animation.setDuration(100);
        animation.setStartOffset(delay);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                card.clearAnimation();
                card.setElevation(previousElevation);
            }
        });
        card.startAnimation(animation);
    }



    void addSwipeAnimationTo(View card){
        float previousElevation = card.getElevation();
        card.setElevation(15);
        Animation animation = new TranslateAnimation(
                0,
                screenWidth + 100,
                0,
                0);
        animation.setDuration(700);
        animation.setStartOffset(400);

        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation arg0) { }
            public void onAnimationRepeat(Animation arg0) { }
            public void onAnimationEnd(Animation arg0) {
                card.setVisibility(View.INVISIBLE);
                card.clearAnimation();
                card.setElevation(previousElevation);
            }
        });
        card.startAnimation(animation);
    }

}
