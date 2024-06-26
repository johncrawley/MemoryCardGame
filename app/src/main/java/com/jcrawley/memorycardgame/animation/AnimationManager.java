package com.jcrawley.memorycardgame.animation;


import android.content.Context;
import android.view.animation.Animation;

import com.jcrawley.memorycardgame.MainActivity;

public class AnimationManager {


    private Animation resultsDropInAnimation, resultsDropOutAnimation, newGameDropInAnimation, newGameDropOutAnimation;
    private Animation aboutDialogDropInAnimation, aboutDialogDropOutAnimation;
    private Animation settingsDropInAnimation, settingsDropOutAnimation;
    private Animation cardsFadeOutAnimation;
    private int screenHeight;
    private Context context;

    public AnimationManager(MainActivity mainActivity, int screenHeight){
        setupAnimations(mainActivity, screenHeight);
    }


    private void setupAnimations(MainActivity mainActivity, int screenHeight){
        context = mainActivity.getApplicationContext();
        this.screenHeight = screenHeight;

        resultsDropInAnimation = createDropInAnimation(mainActivity::onResultsDialogShown);
        resultsDropOutAnimation = createDropOutAnimation(mainActivity::onResultsDismissed);
        newGameDropInAnimation = createDropInAnimation(()->{});
        newGameDropOutAnimation = createDropOutAnimation(mainActivity::onNewGameScreenDismissed);
        aboutDialogDropInAnimation = createDropInAnimation(()->{});
        aboutDialogDropOutAnimation = createDropOutAnimation(mainActivity::onAboutDialogDismissed);
        settingsDropInAnimation = createDropInAnimation(() -> {});
        settingsDropOutAnimation = createDropOutAnimation(mainActivity::onSettingsDialogDismissed);
        cardsFadeOutAnimation= AnimationHelper.createFadeOutAnimationForCards(context, mainActivity::onCardsFadedOut);
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


    public Animation getAboutDialogDropInAnimation(){
        return aboutDialogDropInAnimation;
    }

    public Animation getAboutDialogDropOutAnimation(){
        return aboutDialogDropOutAnimation;
    }

    public Animation getSettingsDropInAnimation(){
        return settingsDropInAnimation;
    }

    public Animation getSettingsDropOutAnimation(){
        return settingsDropOutAnimation;
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
