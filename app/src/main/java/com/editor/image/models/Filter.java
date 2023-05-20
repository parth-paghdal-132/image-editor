package com.editor.image.models;

public class Filter {
    private int imageId;
    private String filterTitle;
    private FilterName filterName;

    public Filter(int imageId, String filterTitle, FilterName filterName) {
        this.imageId = imageId;
        this.filterTitle = filterTitle;
        this.filterName = filterName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getFilterTitle() {
        return filterTitle;
    }

    public void setFilterTitle(String filterTitle) {
        this.filterTitle = filterTitle;
    }

    public FilterName getFilterName() {
        return filterName;
    }

    public void setFilterName(FilterName filterName) {
        this.filterName = filterName;
    }

    public enum FilterName {
        BLACK_AND_WHITE,
        BRIGHTNESS,
        CONTRAST,
        EMBOSS,
        GAUSSIAN,
        GRAYSCALE,
        HUE,
        INVERT,
        POSTERIZE,
        SATURATION,
        SEPIA,
        VIGNETTE
    }
}
