package com.thespacemanatee.recyclerviewtutorial.models;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.UUID;

// Regular Java POJO that stores its selected state
public class StatefulPokemon extends Pokemon {
    private final UUID id;
    private boolean isSelected;

    public StatefulPokemon(String name, int resId, boolean isSelected) {
        super(name, resId);
        this.id = UUID.randomUUID();
        this.isSelected = isSelected;
    }

    public UUID getId() {
        return id;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StatefulPokemon that = (StatefulPokemon) o;
        return isSelected == that.isSelected && id.equals(that.id) && getName().equals(that.getName()) &&
                getResId() == that.getResId() && getViewType() == that.getViewType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, isSelected, getName(), getResId(), getViewType());
    }
}
