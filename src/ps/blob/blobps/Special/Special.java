package ps.blob.blobps.Special;

import java.util.HashMap;

import ps.blob.blobps.Blob.Blob;

public class Special {
    private String specialName;
    private String specialDescription;

    public Special(String name, String description) {
        this.specialName = name;
        this.specialDescription = description;
    }

    public String getName() {
        return specialName;
    }

    public String getDescription() {
        return specialDescription;
    }
}