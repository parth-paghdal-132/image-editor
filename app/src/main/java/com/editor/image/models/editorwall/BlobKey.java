package com.editor.image.models.editorwall;

import java.io.Serializable;

public class BlobKey implements Serializable {
    private String blobKey;

    public BlobKey(String blobKey) {
        this.blobKey = blobKey;
    }

    public String getBlobKey() {
        return blobKey;
    }

    public void setBlobKey(String blobKey) {
        this.blobKey = blobKey;
    }
}
