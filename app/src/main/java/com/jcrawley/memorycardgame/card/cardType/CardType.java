package com.jcrawley.memorycardgame.card.cardType;


import com.jcrawley.memorycardgame.R;

public enum CardType {

    STANDARD(R.drawable.card_face_standard),
    SIMPLE(R.drawable.card_face_simple),
    CAT(R.drawable.card_face_cat_2),
    BACK_KALEIDOSCOPE_RED(R.drawable.card_back_kaleidoscope_red),
    BACK_KALEIDOSCOPE_BLUE(R.drawable.card_back_kaleidoscope_blue),
    BACK_KALEIDOSCOPE_GREEN(R.drawable.card_back_kaleidoscope_green),
    BACK_KALEIDOSCOPE_YELLOW(R.drawable.card_back_kaleidoscope_yellow),
    BACK_KALEIDOSCOPE_METALLIC(R.drawable.card_back_kaleidoscope_metallic_),
    BACK_GREEN_DIAGONALS_LIGHT(R.drawable.card_back_green_diag_light),
    BACK_YELLOW_DIAGONALS_LIGHT(R.drawable.card_back_yellow_diagonals),
    BACK_CRISSCROSS_RED_BLUE(R.drawable.card_back_crisscross_red_blue),
    BACK_WIDGETS_BLUE_GREEN(R.drawable.card_back_widgets_blue_green),
    BACK_WIDGETS_GREY(R.drawable.card_back_widgets_grey),
    BACK_WIDGETS_BLUE(R.drawable.card_back_widgets_blue),
    BACK_WIDGETS_RED_BLUE(R.drawable.card_back_widgets_red_blue),
    BACK_SPORES_GREEN(R.drawable.card_back_spores_green),
    BACK_SPORES_PURPLE(R.drawable.card_back_spores_purple),
    BACK_SPORES_YELLOW_RED(R.drawable.card_back_spores_yellow_red),
    BACK_SPORES_DARK_YELLOW(R.drawable.card_back_spores_dark_yellow),
    BACK_FLORAL_ORANGE(R.drawable.card_back_floral_orange),
    BACK_FLORAL_BLUE(R.drawable.card_back_floral_blue),
    BACK_FLORAL_GREEN(R.drawable.card_back_floral_green),
    BACK_MODERN_ROSE(R.drawable.card_back_modern_rose),
    BACK_MODERN_MINT(R.drawable.card_back_modern_mint),
    BACK_MODERN_BLUE(R.drawable.card_back_modern_blue),
    BACK_MODERN_YELLOW(R.drawable.card_back_modern_yellow),
    BACK_WEAVE_BLUE(R.drawable.card_back_weave_blue),
    BACK_WEAVE_RED(R.drawable.card_back_weave_red),
    BACK_WEAVE_GREEN(R.drawable.card_back_weave_green),
    BACK_PLAIN_TRUE_BLUE(R.drawable.card_back_plain_true_blue),
    BACK_PLAIN_BLUE(R.drawable.card_back_plain_blue),
    BACK_PLAIN_RED(R.drawable.card_back_plain_red),
    BACK_PLAIN_GREEN(R.drawable.card_back_plain_green),
    BACK_PLAIN_YELLOW(R.drawable.card_back_plain_yellow),
    BACK_PLAIN_BLACK(R.drawable.card_back_plain_black),
    BACK_RANDOM(R.drawable.card_back_random);


    private final int resourceId;

    CardType(int resId){
        resourceId = resId;
    }

    public int getResourceId(){
        return resourceId;
    }


}


