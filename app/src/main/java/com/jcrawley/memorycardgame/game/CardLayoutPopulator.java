package com.jcrawley.memorycardgame.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcrawley.memorycardgame.R;

public class CardLayoutPopulator {


    @SuppressLint("UseCompatLoadingForDrawables")
    public CardLayoutPopulator(Activity activity){

        final int NUMBER_OF_CARDS_PER_ROW = 7;
        final int NUMBER_OF_COLUMNS = 8;
        LinearLayout rowLayout1 = activity.findViewById(R.id.cardRow1Layout);
        int width = rowLayout1.getWidth();
        int maxWidth = Math.max(120, width/(NUMBER_OF_CARDS_PER_ROW + 1));
        int maxHeight = Math.max(240, rowLayout1.getHeight()/ (NUMBER_OF_COLUMNS + 1));
        System.out.println("CardLayoutPopulator, max width and height: " + maxWidth + " " + maxHeight);

        for(int i=0; i< NUMBER_OF_CARDS_PER_ROW; i++){
            ImageView imageView = new ImageView(activity);
            imageView.setImageDrawable(activity.getDrawable(R.drawable.card_back_1));
            imageView.setMaxWidth(maxWidth);
            imageView.setMaxHeight(maxHeight);
            imageView.setPadding(15,15,15,15);
            LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(maxWidth, maxWidth * 2);//mageView.getLayoutParams();
            layoutParams.width = maxWidth;
            layoutParams.height = maxHeight;
            setClickListener(imageView);
            imageView.setLayoutParams(layoutParams);
            imageView.setVisibility(View.VISIBLE);
            rowLayout1.addView(imageView);
        }

    }

    private void setClickListener(View view){

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("Hello Card!");
            }
        });


    }


}
