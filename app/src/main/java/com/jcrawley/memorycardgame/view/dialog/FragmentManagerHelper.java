package com.jcrawley.memorycardgame.view.dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.view.dialog.settings.SettingsDialogFragment;


public class FragmentManagerHelper {

    public static void showAboutDialog(MainActivity mainActivity){
        showDialog(mainActivity, AboutDialogFragment.newInstance(), "aboutDialogFragment");
    }


    public static void showSettingsDialog(MainActivity mainActivity){
        showDialog(mainActivity, SettingsDialogFragment.newInstance(), "settingsDialogFragment");
    }


    public static void showOptionsDialog(MainActivity mainActivity){
        showDialog(mainActivity, OptionsDialogFragment.newInstance(), "optionsDialogFragment");
    }


    public static void showDialog(AppCompatActivity activity, DialogFragment dialogFragment, String tag){
        var fragmentManager = activity.getSupportFragmentManager();
        var existingFragment = fragmentManager.findFragmentByTag(tag);
        if (existingFragment != null) {
            fragmentManager.beginTransaction()
                    .remove(existingFragment)
                    .commit();
        }
        fragmentManager.beginTransaction()
                .add(dialogFragment , tag)
                .commit();
    }

}
