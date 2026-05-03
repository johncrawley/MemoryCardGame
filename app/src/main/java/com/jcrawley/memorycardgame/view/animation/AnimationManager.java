package com.jcrawley.memorycardgame.view.animation;


import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.Animation;


public class AnimationManager {

    private int screenHeight;
    private final Context context;

    public AnimationManager(Context context, int screenHeight){
        this.context =  context;
        this.screenHeight = screenHeight;
    }


    public void setScreenHeight(int screenHeight){
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
