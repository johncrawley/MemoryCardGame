package com.jcrawley.memorycardgame;

import android.content.Context;
import android.content.SharedPreferences;

public class GamePreferences {

    private final Context context;
    private final String PREFERENCES_NAME = "memoryCardGamePreferences";
    private final String PREF_NAME_TURNS_RECORD= "numberOfTurnsCurrentRecord_";
    private final String PREF_NAME_NUMBER_OF_CARDS= "numberOfCards_";
    public final static String PREF_NAME_BACKGROUND_INDEX= "backgroundIndex";
    public final static String PREF_NAME_CARD_FACE_INDEX= "cardFaceIndex";
    public final static String PREF_NAME_CARD_BACK_INDEX= "cardBackIndex";


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
        saveInt(PREF_NAME_NUMBER_OF_CARDS, numberOfCards);
    }


    public void saveInt(String prefName, int value){
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


    public int getInt(String prefName){
        return getPrefs().getInt(prefName, 0);
    }

}
