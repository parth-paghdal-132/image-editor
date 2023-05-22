package com.editor.image.models.editorwall;

import java.io.Serializable;

public class User implements Serializable {
    private Raw raw;

    public User(Raw raw) {
        this.raw = raw;
    }

    public Raw getRaw() {
        return raw;
    }

    public void setRaw(Raw raw) {
        this.raw = raw;
    }
}
