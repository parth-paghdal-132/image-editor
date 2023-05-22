package com.editor.image.models.editorwall;

import java.io.Serializable;

public class Raw implements Serializable {
    private String kind;
    private int id;
    private String name;

    public Raw(String kind, int id, String name) {
        this.kind = kind;
        this.id = id;
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
