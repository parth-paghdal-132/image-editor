package com.editor.image.models.editorwall;

import com.editor.image.utils.Constants;

import java.io.Serializable;

public class EditorWall implements Serializable {
    private Long id;
    private User user;
    private OriginalImage originalImage;
    private BlobKey blobKey;
    private String created;
    private String updated;

    public EditorWall(Long id, User user, OriginalImage originalImage, BlobKey blobKey, String created, String updated) {
        this.id = id;
        this.user = user;
        this.originalImage = originalImage;
        this.blobKey = blobKey;
        this.created = created;
        this.updated = updated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OriginalImage getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(OriginalImage originalImage) {
        this.originalImage = originalImage;
    }

    public BlobKey getBlobKey() {
        return blobKey;
    }

    public void setBlobKey(BlobKey blobKey) {
        this.blobKey = blobKey;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }


    public String getOriginalImageUrl() {
        return getOriginalImage().getRaw().getName();
    }

    public String getEditedImageUrl() {
        return Constants.BASE_URL + "srv/" + getBlobKey().getBlobKey();
    }
}
