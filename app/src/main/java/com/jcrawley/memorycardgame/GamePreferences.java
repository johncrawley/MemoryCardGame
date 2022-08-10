package com.jcrawley.memorycardgame;

import android.content.Context;
import android.content.SharedPreferences;

public class GamePreferences {

    private final Context context;
    private final String PREFERENCES_NAME = "memoryCardGamePreferences";
    private final String PREF_NAME_TURNS_RECORD= "numberOfTurnsCurrentRecord_";
    private final String PREF_NAME_NUMBER_OF_CARDS= "numberOfCards_";
    private final String PREF_NAME_BACKGROUND_INDEX= "backgroundIndex";
    private final String PREF_NAME_CARD_FACE_INDEX= "cardFaceIndex";
    private final String PREF_NAME_CARD_BACK_INDEX= "cardBackIndex";


    GamePreferences(Context context){
        this.context = context;
    }


    public int getCurrentTurnsRecordFromPreferences(int numberOfCards){
        return getPrefs().getInt(PREF_NAME_TURNS_RECORD + numberOfCards, Integer.MAX_VALUE);
    }


    public void saveNewTurnsRecord(int numberOfTurns, int numberOfCards){
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(PREF_NAME_TURNS_RECORD + numberOfCards, numberOfTurns );
        editor.apply();
    }


    public void saveNumberOfCards(int numberOfCards){
        setInt(PREF_NAME_NUMBER_OF_CARDS, numberOfCards);
    }


    private void setInt(String prefName, int value){
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(prefName, value);
        editor.apply();
    }


    public int getNumberOfCards(){
        return getPrefs().getInt(PREF_NAME_NUMBER_OF_CARDS, 26);
    }


    private SharedPreferences getPrefs(){
        return context.getSharedPreferences(PREFERENCES_NAME,0);
    }


    private SharedPreferences.Editor getEditor(){
        return context.getSharedPreferences(PREFERENCES_NAME,0).edit();
    }


    public int getBackgroundIndex(){
        return getPrefs().getInt(PREF_NAME_BACKGROUND_INDEX, 0);
    }

    public void saveBackgroundIndex(int index){
        setInt(PREF_NAME_BACKGROUND_INDEX, index);
    }


}
