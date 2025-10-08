package com.jcrawley.memorycardgame.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jcrawley.memorycardgame.GamePreferences;
import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.game.OldGame;

public class GameService extends Service {
    IBinder binder = new LocalBinder();
    private Game game;
    private MainActivity mainActivity;

    public GameService() {
        super();
    }


    @Override
    public void onCreate() {
        Context context = getApplicationContext();
        GamePreferences gamePreferences = new GamePreferences(context);
        game = new Game(gamePreferences);
    }


    private void log(String msg){
        System.out.println("^^^ GameService: " + msg);
    }


    public Game getGame(){
        return game;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY; // service is not restarted when terminated
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }



    @Override
    public void onDestroy() {
        mainActivity = null;
    }


    public boolean isActivityUnbound(){
        return mainActivity == null;
    }


    public void setActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        game.setView(mainActivity.getGameView());
    }


    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

}