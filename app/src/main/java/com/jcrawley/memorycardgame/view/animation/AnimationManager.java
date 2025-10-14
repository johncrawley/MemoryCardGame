package com.jcrawley.memorycardgame.view.animation;


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
        resultsDropInAnimation = createDropInAnimation(mainActivity::onGameOverDialogShown);
        resultsDropOutAnimation = createDropOutAnimation(mainActivity::onResultsDismissed);
        newGameDropInAnimation = createDropInAnimation(()->{});
        newGameDropOutAnimation = createDropOutAnimation(mainActivity::onNewGameScreenDismissed);
        cardsFadeOutAnimation = AnimationHelper.createFadeOutAnimationForCards(context, mainActivity::onCardsFadedOut);
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
