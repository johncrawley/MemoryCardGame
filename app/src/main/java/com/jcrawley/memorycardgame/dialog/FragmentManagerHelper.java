package com.jcrawley.memorycardgame.dialog;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.dialog.settings.SettingsDialogFragment;


public class FragmentManagerHelper {

    public static void showAboutDialog(MainActivity mainActivity){
        log("entered showAboutDialog()");
        showDialog(mainActivity, AboutDialogFragment.newInstance(), "aboutDialogFragment");
    }


    public static void showSettingsDialog(MainActivity mainActivity){
        log("entered showAboutDialog()");
        showDialog(mainActivity, SettingsDialogFragment.newInstance(), "settingsDialogFragment");
    }


    public static void showOptionsDialog(MainActivity mainActivity){
        log("entered showAboutDialog()");
        showDialog(mainActivity, OptionsDialogFragment.newInstance(), "optionsDialogFragment");
    }

    private static void log(String msg){
        System.out.println("^^^ FragmentManagerHelper : " + msg);
    }

    public static void showDialog(Fragment parentFragment, DialogFragment dialogFragment, String tag, Bundle bundle){
        FragmentManager fragmentManager = parentFragment.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentTransaction, tag);
    }


    public static void showDialog(AppCompatActivity activity, DialogFragment dialogFragment, String tag){
        if(activity == null){
            return;
        }
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        dialogFragment.show(fragmentTransaction, tag);
    }


    private static void removePreviousFragmentTransaction(FragmentManager fragmentManager, String tag, FragmentTransaction fragmentTransaction){
        Fragment prev = fragmentManager.findFragmentByTag(tag);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
    }

    /*
    public static void setListener(Fragment fragment, String key, Consumer<Bundle> consumer){
        fragment.getParentFragmentManager().setFragmentResultListener(key, fragment, (requestKey, bundle) -> consumer.accept(bundle));
    }


    public static void setListener(Fragment fragment, Message key, Consumer<Bundle> consumer){
        fragment.getParentFragmentManager().setFragmentResultListener(key.toString(), fragment, (requestKey, bundle) -> consumer.accept(bundle));
    }
    */

    public static void sendMessages(Fragment fragment, Message... messages){
        for(Message message : messages){
            sendMessage(fragment, message);
        }
    }


    public static void sendMessage(Fragment fragment, Message message){
        sendMessage(fragment, message, new Bundle());
    }


    public static void sendMessage(Fragment fragment, Message message, Bundle bundle){
        fragment.getParentFragmentManager().setFragmentResult(message.toString(), bundle);
    }

}
