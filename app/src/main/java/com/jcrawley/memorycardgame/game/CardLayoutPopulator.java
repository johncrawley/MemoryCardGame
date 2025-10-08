package com.jcrawley.memorycardgame.game;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.MainViewModel;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.CardBackManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CardLayoutPopulator {

    private int cardWidth, cardHeight;
    private final MainActivity activity;
    private View.OnClickListener onClickListener;
    private int numberOfCards;
    private int cardsAdded;
   // private final Game game;
    private List<ImageView> imageViews;
    private boolean hasRun = false;
    private final ViewGroup parentLayout;
    private int numberOfCardsPerRow;
    private int numberOfRows;
    private boolean isFirstRun = true;
    private int padding;
    private final MainViewModel viewModel;
    private final CardBackManager cardBackManager;
    private final List<ViewGroup> cardRows = new ArrayList<>();
    private final Consumer<Integer> clickConsumer;

    @SuppressLint("UseCompatLoadingForDrawables")
    public CardLayoutPopulator(MainActivity activity, int numberOfCards, Consumer<Integer> clickConsumer){
        this.activity = activity;
        this.parentLayout = activity.getCardLayout();
        this.viewModel = activity.getViewModel();
        this.clickConsumer = clickConsumer;
        this.cardBackManager = activity.getCardBackManager();
        this.numberOfCards = numberOfCards;
        imageViews = new ArrayList<>(numberOfCards);
        createClickListener();
    }


    public void addCardViews(){
        addCardViews(true);
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


    private int getInt(int resId){
        return activity.getResources().getInteger(resId);
    }


    public void addCardViews(int numberOfCards){
        hasRun = false;
        parentLayout.removeAllViewsInLayout();
        this.numberOfCards = numberOfCards;
        imageViews = new ArrayList<>(numberOfCards);
        addCardViews();
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


    public List<ImageView> getImageViews(){
        return imageViews;
    }


    public ImageView getImageViewAt(int position){
        int index = position >= imageViews.size() ? 0 : position;
        return imageViews.get(index);
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
        imageViews.add(imageView);
        cardBackManager.setCardBackOf(imageView);
        imageView.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(cardWidth, cardHeight);
        imageView.setTag(R.string.position_tag, cardsAdded);
        imageView.setOnClickListener(onClickListener);
        imageView.setLayoutParams(layoutParams);
        setVisibility(imageView);
        cardsAdded++;
        return  imageView;
    }


    private void setVisibility(ImageView cardView){
        boolean isVisible = viewModel.cards.get(cardsAdded).isVisible() && isFirstRun;
        cardView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }


    private void createPadding(){
        padding = getInt(R.integer.minimum_card_padding) + (cardWidth / getInt(R.integer.card_width_padding_divisor));
        if(numberOfCards < 10){
            padding += 10;
        }
    }


    private void createClickListener(){
        onClickListener = view -> {
            if(view.getVisibility() == View.VISIBLE){
                clickConsumer.accept((int)view.getTag(R.string.position_tag));
            }
        };
    }

}
