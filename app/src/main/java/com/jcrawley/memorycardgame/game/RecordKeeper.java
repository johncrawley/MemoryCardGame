package com.jcrawley.memorycardgame.game;

import android.content.Context;
import android.content.SharedPreferences;

public class RecordKeeper {

    private final Context context;
    private final String PREFERENCES_NAME = "memoryCardGamePreferences";
    private final String PREF_NAME_TURNS_RECORD= "numberOfTurnsCurrentRecord_";

    RecordKeeper(Context context){
        this.context = context;
    }


    public int getCurrentTurnsRecordFromPreferences(int numberOfCards){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_NAME,0);
        return prefs.getInt(PREF_NAME_TURNS_RECORD + numberOfCards, Integer.MAX_VALUE);
    }


    public void saveNewTurnsRecord(int numberOfTurns, int numberOfCards){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME,0).edit();
        editor.putInt(PREF_NAME_TURNS_RECORD + numberOfCards, numberOfTurns );
        editor.apply();
    }

}
