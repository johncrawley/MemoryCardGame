package com.jcrawley.memorycardgame.view.animation;


import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.jcrawley.memorycardgame.MainActivity;

public class AnimationManager {

    private final int screenHeight;
    private final Context context;

    public AnimationManager(MainActivity mainActivity, int screenHeight){
        context = mainActivity.getApplicationContext();
        this.screenHeight = screenHeight;
    }


    public void dropOut(ViewGroup layout, Runnable onAnimationFinished){
        var animation = createDropOutAnimation(onAnimationFinished);
        if(layout != null){
            layout.startAnimation(animation);
        }
    }


    public void fadeOut(ViewGroup layout, Runnable onAnimationFinished){
        var animation = AnimationHelper.createFadeOutAnimationForCards(context, onAnimationFinished);
        if(layout != null){
            layout.startAnimation(animation);
        }
    }


    public void dropIn(ViewGroup layout){
        var animation = createDropInAnimation();
        if(layout != null){
            layout.startAnimation(animation);
        }
    }


    public void dropIn(ViewGroup layout, Runnable onAnimationFinished){
        var animation = createDropInAnimation(onAnimationFinished);
        if(layout != null){
            layout.startAnimation(animation);
        }
    }


    public void startDropOutAnimation(ViewGroup layout, Runnable onAnimationFinished){
        var animation = createDropOutAnimation(onAnimationFinished);
        layout.startAnimation(animation);
    }


    private Animation createDropInAnimation(){
        return AnimationHelper.createDropInAnimation(context, screenHeight);
    }


    private Animation createDropInAnimation(Runnable runnable){
        return AnimationHelper.createDropInAnimation(context, screenHeight, runnable);
    }


    private Animation createDropOutAnimation(Runnable runnable){
        return AnimationHelper.createDropOutAnimation(context, screenHeight, runnable);
    }


}
