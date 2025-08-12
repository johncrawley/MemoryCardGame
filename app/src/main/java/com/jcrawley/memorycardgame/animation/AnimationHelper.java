package com.jcrawley.memorycardgame.animation;

import static android.view.View.INVISIBLE;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.jcrawley.memorycardgame.R;

public class AnimationHelper {

    public static Animation createDropAnimation(Context context, AnimationDirection direction, int screenHeight, Runnable onAnimationEnd ){
        Animation animation = new TranslateAnimation(
                0,
                0,
                direction == AnimationDirection.DROP_IN ? -screenHeight : 0,
                direction == AnimationDirection.DROP_OUT ? screenHeight : 0);
        animation.setDuration(context.getResources().getInteger(R.integer.view_drop_duration));
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationStart(Animation arg0) { }
            public void onAnimationRepeat(Animation arg0) { }
            @Override
            public void onAnimationEnd(Animation arg0) {
                onAnimationEnd.run();
            }});

        return animation;
    }


    public static Animation createDropInAnimation(Context context, int screenHeight, Runnable onAnimationEnd ){
        return createDropAnimation(context, AnimationDirection.DROP_IN, screenHeight, onAnimationEnd);
    }


    public static Animation createDropOutAnimation(Context context, int screenHeight, Runnable onAnimationEnd ){
        return createDropAnimation(context, AnimationDirection.DROP_OUT, screenHeight, onAnimationEnd);
    }


    public static Animation createFadeOutAnimationForCards(Context context, Runnable onFinish){
        Animation animation = new AlphaAnimation(1, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setStartOffset(getInt(context, R.integer.fade_out_cards_start_offset));
        animation.setDuration(getInt(context, R.integer.fade_out_cards_duration));
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                onFinish.run();
            }
            public void onAnimationStart(Animation animation) { }
            public void onAnimationRepeat(Animation animation) { }
        });
        return animation;
    }


    public static Animation createFadeOutAnimation(Context context, View view){
        Animation animation = new AlphaAnimation(1, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setStartOffset(getInt(context, R.integer.fade_out_cards_start_offset));
        animation.setDuration(getInt(context, R.integer.fade_out_cards_duration));
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(INVISIBLE);
            }
            public void onAnimationStart(Animation animation) { }
            public void onAnimationRepeat(Animation animation) { }
        });
        return animation;
    }



    public static Animation createFadeInAnimation(Context context){
        Animation animation = new AlphaAnimation(0, 1);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setStartOffset(getInt(context, R.integer.fade_out_cards_start_offset));
        animation.setDuration(getInt(context, R.integer.fade_out_cards_duration));
        return animation;
    }


    public static int getInt(Context context, int resId){
        return context.getResources().getInteger(resId);
    }

}
