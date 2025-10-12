package com.jcrawley.memorycardgame.view.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jcrawley.memorycardgame.R;


public class AboutDialogFragment extends DialogFragment {

    public static AboutDialogFragment newInstance() {
        System.out.println("^^^ AboutDialogFragment entered newInstance()");
        return new AboutDialogFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_about, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupButtons(view);
        //DialogFragmentUtils.setScrollViewHeight(this, view, R.id.aboutInfoScrollView, R.id.aboutInfoLayout);
        //DialogFragmentUtils.setTransparentBackground(this);
    }


    private void setupButtons(View parentView){
       // ButtonMaker.setupButton(parentView, R.id.dismissAboutDialogButton, this::dismiss);
    }


}