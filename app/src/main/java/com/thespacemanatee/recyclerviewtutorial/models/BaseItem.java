package com.thespacemanatee.recyclerviewtutorial.models;

public abstract class BaseItem {
    public static int VIEW_TYPE_SECTION_TITLE = 0;
    public static int VIEW_TYPE_POKEMON = 1;

    public abstract int getViewType();
}
