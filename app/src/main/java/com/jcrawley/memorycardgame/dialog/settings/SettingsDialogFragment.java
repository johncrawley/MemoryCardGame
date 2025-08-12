package com.jcrawley.memorycardgame.dialog.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jcrawley.memorycardgame.GamePreferences;
import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.background.BackgroundFactory;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.card.CardTypeSetter;
import com.jcrawley.memorycardgame.card.cardType.CardType;
import com.jcrawley.memorycardgame.game.Game;
import com.jcrawley.memorycardgame.dialog.settings.list.BackgroundRecyclerAdapter;
import com.jcrawley.memorycardgame.dialog.settings.list.CardTypeRecyclerAdapter;
import com.jcrawley.memorycardgame.utils.BitmapLoader;

public class SettingsDialogFragment extends DialogFragment {


    private BitmapLoader bitmapLoader;
    private Game game;
    private CardBackManager cardBackManager;
    private GamePreferences gamePreferences;

    public static SettingsDialogFragment newInstance() {
        return new SettingsDialogFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //DialogFragmentUtils.setScrollViewHeight(this, view, R.id.aboutInfoScrollView, R.id.aboutInfoLayout);
        //DialogFragmentUtils.setTransparentBackground(this);
        assignFields();
        setupFaceTypesRecyclerView(view);
        setupBackTypesRecyclerView(view);
        setupLists(view);

    }


    private void assignFields(){
        MainActivity mainActivity = (MainActivity)getActivity();
        if(mainActivity == null){
            return;
        }
        cardBackManager = mainActivity.getCardBackManager();
        bitmapLoader = mainActivity.getBitmapLoader();
        gamePreferences = new GamePreferences(getContext());
        game = mainActivity.getGame();
    }



    private void setupLists(View parentView){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity == null){
            return;
        }
        GamePreferences gamePreferences = new GamePreferences(getContext());
        RecyclerView backgroundRecyclerView = parentView.findViewById(R.id.backgroundRecyclerView);
        BackgroundRecyclerAdapter backgroundRecyclerAdapter = new BackgroundRecyclerAdapter(BackgroundFactory.getAll());
        backgroundRecyclerAdapter.init(backgroundRecyclerView, mainActivity, gamePreferences.getInt(GamePreferences.PREF_NAME_BACKGROUND_INDEX));
    }



    private void setupBackTypesRecyclerView(View parentView){
        RecyclerView cardBacksRecyclerView = parentView.findViewById(R.id.cardBackRecycleView);

        CardTypeRecyclerAdapter cardTypeRecyclerAdapter = new CardTypeRecyclerAdapter(cardBackManager.getSelectableCardBackTypes(),
                bitmapLoader,
                cardBackManager,
                ()->{
                    game.switchBacksOnFaceDownCards();
                    bitmapLoader.clearCardBackCache();
                } );
        cardTypeRecyclerAdapter.init(cardBacksRecyclerView, getContext(), gamePreferences, GamePreferences.PREF_NAME_CARD_BACK_INDEX);
    }


    private void setupFaceTypesRecyclerView(View parentView){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity == null){
            return;
        }
        CardTypeSetter cardTypeSetter = mainActivity.getCardTypeSetter();
        RecyclerView cardFacesRecyclerView = parentView.findViewById(R.id.cardTypeRecycleView);
        CardTypeRecyclerAdapter cardTypeRecyclerAdapter = new CardTypeRecyclerAdapter(CardType.getCardFaces(),
                bitmapLoader,
                cardTypeSetter,
                ()-> bitmapLoader.clearCardFaceCache());

        cardTypeRecyclerAdapter.init(cardFacesRecyclerView,
                getContext(),
                gamePreferences,
                GamePreferences.PREF_NAME_CARD_FACE_INDEX);
    }


}