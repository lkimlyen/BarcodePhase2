package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Skull on 13/01/2018.
 */

public class UploadEntity {
    @Expose
    private long ImageID;

    public long getImageID() {
        return ImageID;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }
}
