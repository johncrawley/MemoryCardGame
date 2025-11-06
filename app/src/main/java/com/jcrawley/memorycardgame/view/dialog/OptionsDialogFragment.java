package com.jcrawley.memorycardgame.view.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.R;

public class OptionsDialogFragment extends DialogFragment {

    private Button aboutButton, settingsButton, freshGameButton;

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
       aboutButton = setupButton(parentView, R.id.aboutButton, this::showAboutDialog);
       settingsButton = setupButton(parentView, R.id.settingsButton, this::showSettingsDialog);
       freshGameButton = setupButton(parentView, R.id.newGameButton, this::startNewGame);
    }


    private void showAboutDialog(){
        disableAllButtons();
        dismiss();
        FragmentManagerHelper.showAboutDialog((MainActivity) getActivity());
    }


    private void showSettingsDialog(){
        disableAllButtons();
        dismiss();
        FragmentManagerHelper.showSettingsDialog((MainActivity) getActivity());
    }


    private void startNewGame(){
        disableAllButtons();
        var mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
           mainActivity.dropInNewGameDialog();
           dismissAfterDelay();
       }
    }


    private void disableAllButtons(){
        disable(aboutButton);
        disable(settingsButton);
        disable(freshGameButton);
    }


    private void disable(Button button){
        if(button != null){
            button.setEnabled(false);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialogInterface){
        super.onDismiss(dialogInterface);
        enableOptionsButton();
    }


    private void enableOptionsButton(){
        var mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.enableOptionsButton();
        }
    }


    private void dismissAfterDelay(){
        new Handler(Looper.getMainLooper()).postDelayed(this::dismiss, 200);
    }


    public static Button setupButton(View parentView, int id, Runnable onClickAction){
        Button button = parentView.findViewById(id);
        if(button != null){
            button.setOnClickListener((View v)-> onClickAction.run());
        }
        return button;
    }

}