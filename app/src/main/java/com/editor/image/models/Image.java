package com.editor.image.models;

import android.net.Uri;

import java.io.Serializable;

public class Image implements Serializable {
    private String url;
    private String created;
    private String updated;

    public Image(String url, String created, String updated) {

        this.url = url;
        this.created = created;
        this.updated = updated;
    }

    public String getUrl() {
        return url;
    }

    public String getCreated() {
        return created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getFormattedCreatedDate() {
        return "";
    }

    public String getFormattedUpdatedDate() {
        return "";
    }

    public Uri getImageUri() {
        return Uri.parse(url);
    }
}
