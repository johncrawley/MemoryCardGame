package com.jcrawley.memorycardgame.view;

import android.graphics.Bitmap;

import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.CardFaceImages;
import com.jcrawley.memorycardgame.card.CardType;

import java.util.Map;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public Map<Integer, Bitmap> cardFaceMap, cardBackMap;
    public CardFaceImages cardFaceImages = new CardFaceImages();
    public CardType previouslySelectedCardTypeBack = CardType.BACK_KALEIDOSCOPE_RED;
    public int currentCardBackResourceId = R.drawable.card_back_kaleidoscope_red;

}
