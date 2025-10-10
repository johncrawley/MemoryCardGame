package com.jcrawley.memorycardgame.game;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardBackManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CardLayoutManager {

    private int cardWidth, cardHeight;
    private final MainActivity activity;
    private View.OnClickListener onClickListener;
    private int numberOfCards;
    private int cardsAdded;
   // private final Game game;
    private List<ImageView> cardViews;
    private boolean hasRun = false;
    private final ViewGroup parentLayout;
    private int numberOfCardsPerRow;
    private int numberOfRows;
    private boolean isFirstRun = true;
    private int padding;
    private final CardBackManager cardBackManager;
    private final List<ViewGroup> cardRows = new ArrayList<>();
    private Consumer<Integer> clickConsumer;

    @SuppressLint("UseCompatLoadingForDrawables")
    public CardLayoutManager(MainActivity activity){
        this.activity = activity;
        this.parentLayout = activity.getCardLayout();
       // this.viewModel = activity.getViewModel();
        this.cardBackManager = activity.getCardBackManager();
    }


    public void init(Consumer<Integer> clickConsumer){
        log("entered init()");
        this.clickConsumer = clickConsumer;
        createClickListener();
    }


    private int getInt(int resId){
        return activity.getResources().getInteger(resId);
    }


    public void addViewsFor(List<Card> cards, Consumer<Integer> clickConsumer, boolean isVisible){
        hasRun = false;
        this.clickConsumer = clickConsumer;
        createClickListener();
        parentLayout.removeAllViewsInLayout();
        this.numberOfCards = cards.size();
        cardViews = new ArrayList<>(numberOfCards);
        addCardViews( true);
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


    public void addCardViews(boolean shouldCardBackTypeBeRefreshed){
        if(hasRun){
            return;
        }
        if(shouldCardBackTypeBeRefreshed) {
            cardBackManager.refreshCardBackType();
        }
        hasRun = true;
        cardsAdded = 0;
        setDimensions();
        addCardsToParent();
        isFirstRun = false;
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
        cardWidth = (int)Math.floor(Math.sqrt(cardArea)) + 10;
        cardWidth -= reductionOffset;
        cardHeight = (int)(cardWidth * 1.5);

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
        return cardViews;
    }


    public ImageView getImageViewAt(int position){
        log("getImageViewAt() cardViews size: " + cardViews.size());
        int index = position >= cardViews.size() ? 0 : position;
        return cardViews.get(index);
    }


    private void addCardsToParent(){
        cardRows.clear();
        for(int i = 0; i < numberOfRows; i++){
          cardRows.add(createRowOfCards(numberOfCardsPerRow));
        }
        activity.setCardRows(cardRows);
    }


    private ViewGroup createRowOfCards(int numberOfCardsPerRow){
        LinearLayout rowLayout = new LinearLayout(activity);
        for(int i = 0; i < numberOfCardsPerRow; i++){
            if(cardsAdded < numberOfCards){
                ImageView imageView = createCard();
                rowLayout.addView(imageView);
            }
        }
        return rowLayout;
        //parentLayout.addView(rowLayout);
    }


    private ImageView createCard(){
        ImageView imageView = new ImageView(activity);
        int id = View.generateViewId();
        imageView.setId(id);
        cardViews.add(imageView);
        cardBackManager.setCardBackOf(imageView);
        imageView.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(cardWidth, cardHeight);
        imageView.setTag(R.string.position_tag, cardsAdded);
        log("about to assign clickListener to card");
        imageView.setOnClickListener(onClickListener);
        imageView.setLayoutParams(layoutParams);
        cardsAdded++;
        return  imageView;
    }


    private void createPadding(){
        padding = getInt(R.integer.minimum_card_padding) + (cardWidth / getInt(R.integer.card_width_padding_divisor));
        if(numberOfCards < 10){
            padding += 10;
        }
    }


    private void createClickListener(){
        log("Entered createClickListener()");
        onClickListener = view -> {
            if(view.getVisibility() == VISIBLE){
                log("createClickListener() card view clicked!");
                clickConsumer.accept((int)view.getTag(R.string.position_tag));
                log("createClickListener() click consumer accepted!");
            }
        };
    }

    private void log(String msg){
        System.out.println("^^^ CardLayoutManager: " + msg);
    }

}
