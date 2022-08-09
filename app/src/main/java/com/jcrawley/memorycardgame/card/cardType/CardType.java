package com.jcrawley.memorycardgame.card.cardType;


import com.jcrawley.memorycardgame.R;

public enum CardType {

    STANDARD(R.drawable.card_face_standard),
    SIMPLE(R.drawable.card_face_simple),
    BACK_2(R.drawable.card_back_2_resized),
    BACK_GREEN_DIAGONALS(R.drawable.card_back_green_diagonals),
    BACK_GREEN_DIAGONALS_LIGHT(R.drawable.card_back_green_diag_light),
    BACK_CRISSCROSS_RED_BLUE(R.drawable.card_back_crisscross_red_blue),
    BACK_GRADIENT_BLUE_MINT(R.drawable.card_back_gradient_blue_mint),
    BACK_WAVES_BLUE(R.drawable.card_back_waves_blue),
    BACK_WAVES_PURPLE(R.drawable.card_back_waves_purple),
    BACK_WAVES_GOLD(R.drawable.card_back_waves_gold),
    BACK_WIDGETS_BLUE_GREEN(R.drawable.card_back_widgets_blue_green),
    BACK_WIDGETS_GREY(R.drawable.card_back_widgets_grey),
    BACK_WIDGETS_BLUE(R.drawable.card_back_widgets_blue),
    BACK_WIDGETS_RED_BLUE(R.drawable.card_back_widgets_red_blue),
    BACK_SPORES_GREEN(R.drawable.card_back_spores_green),
    BACK_SPORES_LIGHT(R.drawable.card_back_spores_light),
    BACK_SPORES_PURPLE(R.drawable.card_back_spores_purple),
    BACK_SPORES_YELLOW_RED(R.drawable.card_back_spores_yellow_red),
    BACK_SPORES_DARK_YELLOW(R.drawable.card_back_spores_dark_yellow);


    private final int resourceId;

    CardType(int resId){
        resourceId = resId;
    }

    public int getResourceId(){
        return resourceId;
    }


}


