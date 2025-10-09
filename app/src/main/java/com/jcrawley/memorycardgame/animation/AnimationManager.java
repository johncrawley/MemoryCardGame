package com.jcrawley.memorycardgame.animation;


import android.content.Context;
import android.view.animation.Animation;

import com.jcrawley.memorycardgame.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnimationManager {


    private Animation resultsDropInAnimation, resultsDropOutAnimation, newGameDropInAnimation, newGameDropOutAnimation;
    private Animation cardsFadeOutAnimation;
    private int screenHeight;
    private Context context;

    public AnimationManager(MainActivity mainActivity, int screenHeight){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()-> setupAnimations(mainActivity, screenHeight));
    }


    private void setupAnimations(MainActivity mainActivity, int screenHeight){
        context = mainActivity.getApplicationContext();
        this.screenHeight = screenHeight;
        long startTime = System.currentTimeMillis();
        resultsDropInAnimation = createDropInAnimation(mainActivity::onResultsDialogShown);
        resultsDropOutAnimation = createDropOutAnimation(mainActivity::onResultsDismissed);
        newGameDropInAnimation = createDropInAnimation(()->{});
        newGameDropOutAnimation = createDropOutAnimation(mainActivity::onNewGameScreenDismissed);
        cardsFadeOutAnimation = AnimationHelper.createFadeOutAnimationForCards(context, mainActivity::onCardsFadedOut);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("^^^ setupAnimations time: " + duration);
    }


    private void log(String msg){
        System.out.println("^^^ AnimationManager: " + msg);
    }


    public Animation getResultsDropInAnimation(){
        return resultsDropInAnimation;
    }


    public Animation getResultsDropOutAnimation(){
        return resultsDropOutAnimation;
    }


    public Animation getNewGameDropInAnimation(){
        return newGameDropInAnimation;
    }


    public Animation getNewGameDropOutAnimation(){
        boolean isNull = newGameDropOutAnimation == null;
        log("entered getNewGameDropOutAnimation() is null: " + isNull);
        return newGameDropOutAnimation;
    }


    public Animation getCardsFadeOutAnimation(){
        return cardsFadeOutAnimation;
    }


    private Animation createDropInAnimation(Runnable runnable){
        return AnimationHelper.createDropInAnimation(context, screenHeight, runnable);
    }


    private Animation createDropOutAnimation(Runnable runnable){
        return AnimationHelper.createDropOutAnimation(context, screenHeight, runnable);
    }


}
