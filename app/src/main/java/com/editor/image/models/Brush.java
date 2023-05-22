package com.editor.image.models;

import java.io.Serializable;

public class Brush implements Serializable {
    private int radius;
    private int color;

    public Brush(int radius, int color) {
        this.radius = radius;
        this.color = color;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
