package com.editor.image.models;

import com.editor.image.views.EditorTextView;

public class History {
    private Type type;
    private Filter filter;
    private EditorTextView editorTextView;

    public History(Type type, Filter filter, EditorTextView editorTextView) {
        this.type = type;
        this.filter = filter;
        this.editorTextView = editorTextView;
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

    public enum Type {
        FILTER,
        TEXT
    }
}
