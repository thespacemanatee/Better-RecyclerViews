package com.thespacemanatee.recyclerviewtutorial.models;

import androidx.annotation.NonNull;

import java.util.Objects;

// Regular Java POJO, nothing special here
public class SectionItem extends BaseItem {
    private String title;
    private final int viewType = VIEW_TYPE_SECTION_TITLE;

    public SectionItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getViewType() {
        return viewType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionItem that = (SectionItem) o;
        return title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, viewType);
    }

    @NonNull
    @Override
    public String toString() {
        return "SectionItem{" +
                "title=" + title +
                "viewType=" + viewType +
                '}';
    }
}
