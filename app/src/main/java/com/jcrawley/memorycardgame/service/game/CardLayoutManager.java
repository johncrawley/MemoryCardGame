package com.jcrawley.memorycardgame.service.game;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardBackManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class CardLayoutManager {

    private int cardWidth, cardHeight;
    private View.OnClickListener onClickListener;
    private int numberOfCards;
    private int cardsAdded;
    private List<ImageView> cardViews;
    private final ViewGroup parentLayout;
    private int numberOfCardsPerRow;
    private int numberOfRows;
    private int padding;
    private final CardBackManager cardBackManager;
    private final List<ViewGroup> cardRows = new ArrayList<>();
    private Consumer<Integer> clickConsumer;
    private final Context context;

    @SuppressLint("UseCompatLoadingForDrawables")
    public CardLayoutManager(Context context, CardBackManager cardBackManager, ViewGroup cardLayout){
        this.context = context;
        this.parentLayout = cardLayout;
        this.cardBackManager = cardBackManager;
    }


    public void init(Consumer<Integer> clickConsumer){
        this.clickConsumer = clickConsumer;
        createClickListener();
    }


    public void addViewsFor(List<Card> cards, Consumer<Integer> clickConsumer, boolean isVisible){
        this.clickConsumer = clickConsumer;
        createClickListener();
        parentLayout.removeAllViewsInLayout();
        this.numberOfCards = cards.size();
        cardViews = new ArrayList<>(numberOfCards);
        addCardViews();
        setVisibilityOnCardViews(cards, isVisible);
    }


    private void setVisibilityOnCardViews(List<Card> cards, boolean isVisible){
        for(int i = 0; i < cardViews.size(); i++){
            View cardView = cardViews.get(i);
            Card card = cards.get(i);
            int visibility = isVisible && card.isVisible() ? VISIBLE : INVISIBLE;
            cardView.setVisibility(visibility);
        }
    }


    private void addCardViews(){
        cardsAdded = 0;
        setDimensions();
        addCardsToParent();
    }


    private void setDimensions(){
        int reductionOffset = 0;
        if(parentLayout.getWidth() == 0 || parentLayout.getHeight() == 0){
            return;
        }
        do {
            calculateCardAndGridDimensions( parentLayout.getWidth(), parentLayout.getHeight(), reductionOffset);
            reductionOffset += 3;
        }
        while(numberOfCardsPerRow * numberOfRows < numberOfCards);
        createPadding();
    }


    private void calculateCardAndGridDimensions(int parentWidth, int parentHeight, int reductionOffset){
        float parentArea = parentWidth * parentHeight;
        if(numberOfCards == 0){
            calculateDefaultCardAndGridDimensions(parentWidth, parentHeight);
            return;
        }
        float maxAreaPerCard = parentArea / numberOfCards;
        float cardArea = maxAreaPerCard / 2f;
        if(cardArea < 0){
            cardArea = 900;
        }
        cardWidth = (int) Math.floor(Math.sqrt(cardArea)) + 10;
        cardWidth -= reductionOffset;
        cardHeight = (int) (cardWidth * 1.5);

        numberOfCardsPerRow = parentWidth / (Math.max(50, cardWidth));
        numberOfRows = parentHeight / (Math.max(50, cardHeight));
    }


    private void calculateDefaultCardAndGridDimensions(int parentWidth, int parentHeight){
        cardWidth = 50;
        cardHeight = 80;
        numberOfCardsPerRow = parentWidth > parentHeight ? 8 : 7;
        numberOfRows = parentWidth > parentHeight ? 7 : 8;
    }


    public List<ImageView> getCardViews(){
        return cardViews != null ? cardViews : Collections.emptyList();
    }


    public ImageView getImageViewAt(int position){
        int index = position >= cardViews.size() ? 0 : position;

        return cardViews.get(index);
    }


    private void addCardsToParent(){
        cardRows.clear();
        for(int i = 0; i < numberOfRows; i++){
          cardRows.add(createRowOfCards(numberOfCardsPerRow));
        }
        for(ViewGroup cardRow : cardRows){
            parentLayout.addView(cardRow);
        }
    }


    private ViewGroup createRowOfCards(int numberOfCardsPerRow){
        LinearLayout rowLayout = new LinearLayout(context);
        for(int i = 0; i < numberOfCardsPerRow; i++){
            if(cardsAdded < numberOfCards){
                ImageView imageView = createCard();
                rowLayout.addView(imageView);
            }
        }
        return rowLayout;
    }


    private ImageView createCard(){
        ImageView imageView = new ImageView(context);
        int id = View.generateViewId();
        imageView.setId(id);
        cardViews.add(imageView);
        cardBackManager.setCardBackOf(imageView);
        imageView.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(cardWidth, cardHeight);
        imageView.setTag(R.string.position_tag, cardsAdded);
        imageView.setOnClickListener(onClickListener);
        imageView.setLayoutParams(layoutParams);
        cardsAdded++;
        return  imageView;
    }


    private void createPadding(){
        int minimumCardPadding = 5;
        int cardWidthPaddingDivisor = 18;
        padding = minimumCardPadding + (cardWidth / cardWidthPaddingDivisor);
        if(numberOfCards < 10){
            padding += 10;
        }
    }


    private void createClickListener(){
        onClickListener = view -> {
            if(view.getVisibility() == VISIBLE){
                clickConsumer.accept((int)view.getTag(R.string.position_tag));
            }
        };
    }

}
