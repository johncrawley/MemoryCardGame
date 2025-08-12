package com.jcrawley.memorycardgame.utils;

import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.jcrawley.memorycardgame.GamePreferences;
import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.MainViewModel;
import com.jcrawley.memorycardgame.background.Background;
import com.jcrawley.memorycardgame.background.BackgroundFactory;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.List;

public class AppearanceSetter {


    public static void setSavedAppearance(MainActivity mainActivity, CardBackManager cardBackManager, MainViewModel viewModel){
        GamePreferences gamePreferences = mainActivity.getGamePreferences();
        setSavedCardBackType(cardBackManager, gamePreferences);
        setSavedCardFaceType(gamePreferences, viewModel);
        setSavedBackground(mainActivity);
    }


    private static void setSavedCardFaceType(GamePreferences gamePreferences, MainViewModel viewModel){
        List<CardType> cardFaces = CardType.getCardFaces();
        int savedIndex = getSavedPosition(gamePreferences, GamePreferences.PREF_NAME_CARD_FACE_INDEX, cardFaces);
        viewModel.cardDeckImages.setCardType(cardFaces.get(savedIndex));

    }


    private static void setSavedCardBackType(CardBackManager cardBackManager, GamePreferences gamePreferences){
        int savedIndex = getSavedPosition(gamePreferences, GamePreferences.PREF_NAME_CARD_BACK_INDEX,
                cardBackManager.getSelectableCardBackTypes());
        cardBackManager.setCardType(savedIndex);
    }


    public static int getSavedPosition(GamePreferences gamePreferences, String preferenceName, List<CardType> cardTypes){
        return Math.min(gamePreferences.getInt(preferenceName), cardTypes.size()-1);
    }


    private static void setSavedBackground(MainActivity mainActivity){
        GamePreferences gamePreferences = mainActivity.getGamePreferences();
        List<Background> backgrounds = BackgroundFactory.getAll();
        int savedBgIndex = gamePreferences.getInt(GamePreferences.PREF_NAME_BACKGROUND_INDEX);
        Background savedBackground = backgrounds.get(savedBgIndex);
        Drawable background = AppCompatResources.getDrawable(mainActivity, savedBackground.getResourceId());
        mainActivity.setBackground(background);
    }

}
