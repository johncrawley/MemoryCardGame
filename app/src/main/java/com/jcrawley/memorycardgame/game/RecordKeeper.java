package com.jcrawley.memorycardgame.game;

import android.content.Context;
import android.content.SharedPreferences;

public class RecordKeeper {

    private final Context context;
    private final String PREFERENCES_NAME = "memoryCardGamePreferences";
    private final String PREF_NAME_TURNS_RECORD= "numberOfTurnsCurrentRecord";

    RecordKeeper(Context context){
        this.context = context;
    }


    public int getCurrentTurnsRecordFromPreferences(){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_NAME,0);
        return prefs.getInt(PREF_NAME_TURNS_RECORD, Integer.MAX_VALUE);
    }


    public void saveNewTurnsRecord(int numberOfTurns){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME,0).edit();
        editor.putInt(PREF_NAME_TURNS_RECORD, numberOfTurns );
        editor.apply();
    }


    public boolean hasOriginalRecordBeenBroken(){
        return getCurrentTurnsRecordFromPreferences() < Integer.MAX_VALUE;
    }


}
