package com.jcrawley.memorycardgame.view.dialog.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jcrawley.memorycardgame.view.GamePreferences;
import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.view.background.BackgroundFactory;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.card.CardType;
import com.jcrawley.memorycardgame.view.dialog.settings.list.BackgroundRecyclerAdapter;
import com.jcrawley.memorycardgame.view.dialog.settings.list.CardTypeRecyclerAdapter;
import com.jcrawley.memorycardgame.view.utils.BitmapLoader;
import com.jcrawley.memorycardgame.view.GameView;

public class SettingsDialogFragment extends DialogFragment {

    private BitmapLoader bitmapLoader;
    private GameView gameView;
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
        setDialogDimensions(view);
        assignFields();
        setupFaceTypesRecyclerView(view);
        setupBackTypesRecyclerView(view);
        setupBackgroundSettings(view);
    }


    private void assignFields(){
        MainActivity mainActivity = (MainActivity)getActivity();
        if(mainActivity == null){
            return;
        }
        cardBackManager = mainActivity.getCardBackManager();
        bitmapLoader = mainActivity.getBitmapLoader();
        gamePreferences = new GamePreferences(getContext());
        gameView = mainActivity.getGameView();
    }


    public void setDialogDimensions(View parentView){
        MainActivity mainActivity = (MainActivity)getActivity();
        if(mainActivity == null){
            return;
        }
        int width = (mainActivity.getScreenWidth() * 80) / 100;
        int height = (mainActivity.getScreenHeight() * 85) / 100;
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
                parentView.setLayoutParams(layoutParams);

            }
        });
    }


    private void setupBackgroundSettings(View parentView){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity == null){
            return;
        }
        ViewGroup backgroundsSettingsLayout = parentView.findViewById(R.id.backgroundsSettingInclude);
        setupSettingsSectionTitle(backgroundsSettingsLayout, R.string.background_style_settings);
        GamePreferences gamePreferences = new GamePreferences(getContext());
        RecyclerView backgroundRecyclerView = getRecyclerViewFrom(backgroundsSettingsLayout);
        BackgroundRecyclerAdapter backgroundRecyclerAdapter = new BackgroundRecyclerAdapter(BackgroundFactory.getAll());
        backgroundRecyclerAdapter.init(backgroundRecyclerView, mainActivity, gamePreferences.getInt(GamePreferences.PREF_NAME_BACKGROUND_INDEX));
    }


    private void setupFaceTypesRecyclerView(View parentView){
        var mainActivity = (MainActivity) getActivity();
        if(mainActivity == null){
            return;
        }
        var adapter = new CardTypeRecyclerAdapter(CardType.getCardFaces(),
                bitmapLoader,
                mainActivity.getCardTypeSetter(),
                ()-> bitmapLoader.clearCardFaceCache());

        initRecyclerAdapter(parentView,
                adapter,
                R.id.cardFacesSettingInclude,
                R.string.card_face_style_settings,
                GamePreferences.PREF_NAME_CARD_FACE_INDEX);
    }


    private void setupBackTypesRecyclerView(View parentView){
        var adapter = new CardTypeRecyclerAdapter(cardBackManager.getSelectableCardBackTypes(),
                bitmapLoader,
                cardBackManager,
                ()-> {
                    gameView.switchBacksOnFaceDownCards();
                    bitmapLoader.clearCardBackCache();
                });

        initRecyclerAdapter(parentView,
                adapter,
                R.id.cardBacksSettingInclude,
                R.string.card_back_style_settings,
                GamePreferences.PREF_NAME_CARD_BACK_INDEX);
    }


    private void initRecyclerAdapter(View parentView, CardTypeRecyclerAdapter adapter, int settingsViewId, int titleId, String prefName){
        ViewGroup settingView = parentView.findViewById(settingsViewId);
        setupSettingsSectionTitle(settingView, titleId);
        adapter.init(
                getRecyclerViewFrom(settingView),
                getContext(),
                gamePreferences,
                prefName);
    }


    private RecyclerView getRecyclerViewFrom(ViewGroup settingView){
        return settingView.findViewById(R.id.recyclerView);
    }


    private void setupSettingsSectionTitle(View parentView, int strId){
        TextView title = parentView.findViewById(R.id.settingTitleText);
        String text = getString(strId);
        title.setText(text);
    }

}