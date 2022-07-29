package com.jcrawley.memorycardgame.card.cardType;


import com.jcrawley.memorycardgame.R;

public enum CardType {

    STANDARD(R.drawable.card_diamonds_king_2),
    SIMPLE(R.drawable.card_diamonds_king_mirror),
    BACK_1(R.drawable.card_back_1),
    BACK_2(R.drawable.card_back_2);


    private final int resourceId;

    CardType(int resId){
        resourceId = resId;
    }

    public int getResourceId(){
        return resourceId;
    }


}


