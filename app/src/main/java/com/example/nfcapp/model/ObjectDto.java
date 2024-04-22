package com.example.nfcapp.model;

public class ObjectDto {
    private String objectName;
    private String objectDesc;
    private String objectLocation;
    private String nfcId;

    public ObjectDto(String objectName, String objectDesc, String objectLocation, String nfcId) {
        this.objectName = objectName;
        this.objectDesc = objectDesc;
        this.objectLocation = objectLocation;
        this.nfcId = nfcId;
    }


    public String getObjectName() {
        return objectName;
    }

    public String getObjectDesc() {
        return objectDesc;
    }

    public String getObjectLocation() {
        return objectLocation;
    }

    public String getNfcId() {
        return nfcId;
    }

}
