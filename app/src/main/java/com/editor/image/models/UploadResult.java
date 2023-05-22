package com.editor.image.models;

public class UploadResult {
    private String status;

    public UploadResult(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
