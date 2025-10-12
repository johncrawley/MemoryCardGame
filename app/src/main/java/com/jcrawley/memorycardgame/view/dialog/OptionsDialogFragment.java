package com.jcrawley.memorycardgame.view.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.R;

public class OptionsDialogFragment extends DialogFragment {

    public static OptionsDialogFragment newInstance() {
        return new OptionsDialogFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_options, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupButtons(view);
    }


    private void setupButtons(View parentView) {
        setupButton(parentView, R.id.aboutButton, this::showAboutDialog);
        setupButton(parentView, R.id.settingsButton, this::showSettingsDialog);
        setupButton(parentView, R.id.newGameButton, this::startNewGame);
    }


    private void showAboutDialog(){
        dismiss();
        FragmentManagerHelper.showAboutDialog((MainActivity) getActivity());
    }


    private void showSettingsDialog(){
        dismiss();
        FragmentManagerHelper.showSettingsDialog((MainActivity) getActivity());
    }


    private void startNewGame(){
       var mainActivity = (MainActivity) getActivity();
       if(mainActivity != null){
           mainActivity.showNewGameDialog();
           dismissAfterDelay();
       }
    }


    private void dismissAfterDelay(){
        new Handler(Looper.getMainLooper()).postDelayed(this::dismiss, 200);
    }


    public static void setupButton(View parentView, int id, Runnable onClickAction){
        var button = parentView.findViewById(id);
        if(button != null){
            button.setOnClickListener((View v)-> onClickAction.run());
        }
    }

}