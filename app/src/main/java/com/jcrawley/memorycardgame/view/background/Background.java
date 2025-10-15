package com.jcrawley.memorycardgame.view.background;

public record Background(int resourceId, int thumbnailResourceId) {

    public Background(int resourceId){
        this(resourceId, resourceId);
    }
}
