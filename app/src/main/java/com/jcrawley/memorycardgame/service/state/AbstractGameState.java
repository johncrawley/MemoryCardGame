package com.jcrawley.memorycardgame.service.state;

import com.jcrawley.memorycardgame.service.game.Game;

public abstract class AbstractGameState {

    Game game;


    public AbstractGameState(Game game){
        this.game = game;
    }


    public void onClick(int position){

    }


}
