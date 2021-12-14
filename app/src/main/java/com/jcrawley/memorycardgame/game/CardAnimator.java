package com.jcrawley.memorycardgame.game;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.jcrawley.memorycardgame.R;

import java.util.List;

public class CardAnimator {

    private final int screenWidth;
    private final Context context;


    public CardAnimator(int screenWidth, Context context){
        this.screenWidth = screenWidth;
        this.context = context;
    }


    void swipeInAll(List<ImageView> cards){
        int delay = getInt(R.integer.swipe_in_all_cards_initial_delay);
        int delayIncrement = getInt(R.integer.swipe_in_all_cards_delay_offset);
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
        animation.setDuration(getInt(R.integer.swipe_card_in_animation_duration));
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


    void addSwipeOutAnimationTo(View card){
        float previousElevation = card.getElevation();
        int duration = getInt(R.integer.swipe_card_out_animation_duration);
        int intitialOffset = getInt(R.integer.swipe_card_out_animation_initial_offset);
        card.setElevation(getInt(R.integer.card_elevation));
        Animation animation = new TranslateAnimation(
                0,
                screenWidth + 100,
                0,
                0);
        animation.setDuration(duration);
        animation.setStartOffset(intitialOffset);

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


    public int getInt(int resId){
        return context.getResources().getInteger(resId);
    }

}
