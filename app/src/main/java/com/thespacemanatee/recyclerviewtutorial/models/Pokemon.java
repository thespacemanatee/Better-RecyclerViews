package com.thespacemanatee.recyclerviewtutorial.models;

import androidx.annotation.NonNull;

import java.util.Objects;

// Regular Java POJO, nothing special here
public class Pokemon {
    private final String name;
    private final Integer resId;

    public Pokemon(String name, Integer resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public Integer getResId() {
        return resId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return name.equals(pokemon.name) && resId.equals(pokemon.resId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, resId);
    }

    @NonNull
    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", resId=" + resId +
                '}';
    }
}
