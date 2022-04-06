package com.thespacemanatee.recyclerviewtutorial.models;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.UUID;

// Regular Java POJO that stores its selected state
public class StatefulPokemon {
    private final UUID id;
    private final String name;
    private final Integer resId;
    private Boolean isSelected;

    public StatefulPokemon(String name, Integer resId, Boolean isSelected) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.resId = resId;
        this.isSelected = isSelected;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getResId() {
        return resId;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatefulPokemon that = (StatefulPokemon) o;
        return name.equals(that.name) && resId.equals(that.resId) && isSelected.equals(that.isSelected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, resId, isSelected);
    }

    @NonNull
    @Override
    public String toString() {
        return "StatefulPokemon{" +
                "name='" + name + '\'' +
                ", resId=" + resId +
                ", isSelected=" + isSelected +
                '}';
    }
}
