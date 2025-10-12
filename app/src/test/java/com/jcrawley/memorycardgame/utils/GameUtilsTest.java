package com.jcrawley.memorycardgame.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jcrawley.memorycardgame.view.utils.GameUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GameUtilsTest {


    @Test
    public void canVerifyAValidListPosition(){
        List<Boolean> list = List.of(true, true, true);

        int position = -1;
        assertBadListPosition(list, position);

        assertGoodListPosition(list, ++position);
        assertGoodListPosition(list, ++position);
        assertGoodListPosition(list, ++position);

        assertBadListPosition(list, ++position);
    }


    @Test
    public void nullListWontHaveAValidPosition(){
        assertFalse(GameUtils.isValidPosition(null, 0));
    }


    @Test
    public void emptyListWontHaveAValidPosition(){
        boolean result = GameUtils.isValidPosition(new ArrayList<>(), 0);
        assertFalse(result);
    }


    @Test
    public void negativePositionInListIsInvalid(){
        boolean result = GameUtils.isValidPosition(List.of(false), -1);
        assertFalse(result);
    }


    private void assertGoodListPosition(List<?> list, int position){
        assertTrue(GameUtils.isValidPosition(list, position));
    }


    private void assertBadListPosition(List<?> list, int position){
        assertFalse(GameUtils.isValidPosition(list, position));
    }
}
