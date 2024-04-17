package com.example.nfcapp.api;

public class ObjectDto {
    private String objectName;
    private String objectDesc;
    private String objectLocation;

    public ObjectDto(String objectName, String objectDesc, String objectLocation) {
        this.objectName = objectName;
        this.objectDesc = objectDesc;
        this.objectLocation = objectLocation;
    }
}
