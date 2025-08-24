package com.jcrawley.memorycardgame.utils;

import java.util.List;

public class GameUtils {


    public static boolean isValidPosition(List<?> list, int position){
        return list != null && list.size() > position && position > -1;
    }


}
