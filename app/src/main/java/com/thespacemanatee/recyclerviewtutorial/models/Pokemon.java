package com.thespacemanatee.recyclerviewtutorial.models;

import androidx.annotation.NonNull;

import java.util.Objects;

// Regular Java POJO, nothing special here
public class Pokemon extends BaseItem {
    private final String name;
    private final int resId;
    private final int viewType = VIEW_TYPE_POKEMON;

    public Pokemon(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public int getResId() {
        return resId;
    }

    @Override
    public int getViewType() {
        return viewType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return resId == pokemon.resId && name.equals(pokemon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, resId, viewType);
    }

    @NonNull
    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", resId=" + resId +
                ", viewType=" + viewType +
                '}';
    }
}
