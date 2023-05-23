package com.editor.image.models;

import java.io.Serializable;
import java.util.Date;

public class EditorWallSimplified implements Serializable {
    private String originalImageUrl;
    private String editedImageUrl;
    private Date lastUpdatedOn;

    public EditorWallSimplified(String originalImageUrl, String editedImageUrl, Date lastUpdatedOn) {
        this.originalImageUrl = originalImageUrl;
        this.editedImageUrl = editedImageUrl;
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getOriginalImageUrl() {
        return originalImageUrl;
    }

    public void setOriginalImageUrl(String originalImageUrl) {
        this.originalImageUrl = originalImageUrl;
    }

    public String getEditedImageUrl() {
        return editedImageUrl;
    }

    public void setEditedImageUrl(String editedImageUrl) {
        this.editedImageUrl = editedImageUrl;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
