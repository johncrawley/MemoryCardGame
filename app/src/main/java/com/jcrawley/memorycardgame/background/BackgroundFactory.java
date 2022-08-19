package com.jcrawley.memorycardgame.background;

import com.jcrawley.memorycardgame.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackgroundFactory {

    private final static List<Integer> backgroundResourceIds = Arrays.asList(
            R.drawable.background_plain_green,
            R.drawable.background_plain_black,
            R.drawable.background_plain_red,
            R.drawable.background_plain_sand,
            R.drawable.background_plain_navy,
            R.drawable.background_gradient_green_to_black,
            R.drawable.background_gradient_blue_to_black,
            R.drawable.background_gradient_black_green_black,
            R.drawable.background_gradient_black_red_black,
            R.drawable.background_gradient_black_blue_black,
            R.drawable.background_gradient_dark_grey_to_grey,
            R.drawable.background_gradient_peach_to_orange,
            R.drawable.background_tiled_horizontal_lines_red,
            R.drawable.background_tiled_horizontal_lines_brown,
            R.drawable.background_tiled_grey_cross_small,
            R.drawable.background_tiled_dark_grey_cross,
            R.drawable.background_tiled_black_diagonal,
            R.drawable.background_tiled_black_diagonal_mirrored,
            R.drawable.background_tiled_green_diagonal_lines,
            R.drawable.background_tiled_fake_wood);


    public static List<Background> getAll(){
        List<Background> backgrounds = new ArrayList<>();
        for(int id : backgroundResourceIds){
            backgrounds.add(new Background(id));
        }
        return backgrounds;
    }
}
