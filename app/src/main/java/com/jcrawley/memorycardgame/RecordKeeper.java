package com.jcrawley.memorycardgame;

import android.content.Context;
import android.content.SharedPreferences;

public class RecordKeeper {

    private final Context context;
    private final String PREFERENCES_NAME = "memoryCardGamePreferences";
    private final String PREF_NAME_TURNS_RECORD= "numberOfTurnsCurrentRecord_";
    private final String PREF_NAME_LAST_USED_NUMBER_OF_CARDS= "lastUsedNumberOfCards_";


    RecordKeeper(Context context){
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


    public void saveLastUsedNumberOfCards(String numberOfCards){
        SharedPreferences.Editor editor = getEditor();
        editor.putString(PREF_NAME_LAST_USED_NUMBER_OF_CARDS, numberOfCards );
        editor.apply();
    }


    public  String getLastUsedNumberOfCards(){
       return getPrefs().getString(PREF_NAME_LAST_USED_NUMBER_OF_CARDS, "SIXTEEN");
    }


    private SharedPreferences getPrefs(){
        return context.getSharedPreferences(PREFERENCES_NAME,0);
    }


    private SharedPreferences.Editor getEditor(){
        return context.getSharedPreferences(PREFERENCES_NAME,0).edit();
    }

}
