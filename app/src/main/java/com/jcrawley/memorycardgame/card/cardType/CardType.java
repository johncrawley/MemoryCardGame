package com.jcrawley.memorycardgame.card.cardType;


import com.jcrawley.memorycardgame.R;

public enum CardType {

    STANDARD(R.drawable.card_face_standard),
    SIMPLE(R.drawable.card_face_simple),
    BACK_1(R.drawable.card_back_1),
    BACK_2(R.drawable.card_back_2_resized);


    private final int resourceId;

    CardType(int resId){
        resourceId = resId;
    }

    public int getResourceId(){
        return resourceId;
    }


}


