package com.editor.image.models;

import android.graphics.Color;

import java.io.Serializable;

public class Text implements Serializable {
    private String text;
    private int color;

    public Text(String text, int color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
