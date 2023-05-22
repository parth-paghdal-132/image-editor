package com.editor.image.models;

import android.graphics.Bitmap;

import com.editor.image.views.DrawingView;
import com.editor.image.views.EditorTextView;

public class History {
    private Type type;
    private Filter filter;
    private EditorTextView editorTextView;
    private Bitmap bitmap;
    private Bitmap previousBitmap;
    private DrawingView drawingView;


    public History(Type type, Filter filter, EditorTextView editorTextView, Bitmap bitmap, Bitmap previousBitmap, DrawingView drawingView) {
        this.type = type;
        this.filter = filter;
        this.editorTextView = editorTextView;
        this.bitmap = bitmap;
        this.previousBitmap = previousBitmap;
        this.drawingView = drawingView;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public EditorTextView getEditorTextView() {
        return editorTextView;
    }

    public void setEditorTextView(EditorTextView editorTextView) {
        this.editorTextView = editorTextView;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getPreviousBitmap() {
        return previousBitmap;
    }

    public void setPreviousBitmap(Bitmap previousBitmap) {
        this.previousBitmap = previousBitmap;
    }

    public DrawingView getDrawingView() {
        return drawingView;
    }

    public void setDrawingView(DrawingView drawingView) {
        this.drawingView = drawingView;
    }

    public enum Type {
        FILTER,
        TEXT,
        CROP,
        BRUSH
    }
}
