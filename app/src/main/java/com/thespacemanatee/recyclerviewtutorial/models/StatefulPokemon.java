package com.thespacemanatee.recyclerviewtutorial.models;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.UUID;

// Regular Java POJO that stores its selected state
public class StatefulPokemon extends Pokemon {
    private final UUID id;
    private Boolean isSelected;

    public StatefulPokemon(String name, Integer resId, Boolean isSelected) {
        super(name, resId);
        this.id = UUID.randomUUID();
        this.isSelected = isSelected;
    }

    public UUID getId() {
        return id;
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
        if (!super.equals(o)) return false;
        StatefulPokemon that = (StatefulPokemon) o;
        return id.equals(that.id) && getName().equals(that.getName()) &&
                getResId().equals(that.getResId()) && isSelected.equals(that.isSelected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, getName(), getResId(), isSelected);
    }

    @NonNull
    @Override
    public String toString() {
        return "StatefulPokemon{" +
                "id=" + id +
                ", name=" + getName() +
                ", resId=" + getResId() +
                ", isSelected=" + isSelected +
                '}';
    }
}
