package com.jcrawley.memorycardgame;

import android.graphics.Bitmap;

import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.DeckSize;
import com.jcrawley.memorycardgame.game.GameState;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public boolean isAlreadyInitialised = false;
    public Map<Integer, Bitmap> bitmapMap;
    public int numberOfCards;
    public int remainingCards;
    public int numberOfTurns;
    public GameState gameState;
    public int currentPosition;
    public Map<DeckSize, List<Card>> deck;
    public List<Card> cards;
    public int firstSelectedPosition = -1;
    public int secondSelectedPosition = -1;

}
