package com.jcrawley.memorycardgame;

import android.graphics.Bitmap;

import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardDeckImages;
import com.jcrawley.memorycardgame.card.cardType.CardType;
import com.jcrawley.memorycardgame.game.TurnState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public boolean isAlreadyInitialised = false;
    public Map<Integer, Bitmap> cardFaceMap,cardBackMap;
    public int numberOfCards;
    public int remainingCards;
    public int numberOfTurns;
    public TurnState turnState;
    public int currentPosition;
    public List<Card> cards = new ArrayList<>();
    public int firstSelectedPosition = -1;
    public int secondSelectedPosition = -1;
    public CardDeckImages cardDeckImages = new CardDeckImages();
    public CardType previouslySelectedCardTypeBack = CardType.BACK_KALEIDOSCOPE_RED;

    public int currentCardBackResourceId = R.drawable.card_back_kaleidoscope_red;

}
