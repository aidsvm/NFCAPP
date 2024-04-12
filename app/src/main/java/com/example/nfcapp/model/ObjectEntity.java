package com.example.nfcapp.model;

public class ObjectEntity {
    private String objectName;
    private String objectDesc;
    private String objectLocation;
    private String nfcUID;

    public ObjectEntity() {}

    public ObjectEntity(String objectName, String objectDesc, String objectLocation, String nfcUID) {
        this.objectName = objectName;
        this.objectDesc = objectDesc;
        this.objectLocation = objectLocation;
        this.nfcUID = nfcUID;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectDesc() {
        return objectDesc;
    }

    public void setObjectDesc(String objectDesc) {
        this.objectDesc = objectDesc;
    }

    public String getObjectLocation() {
        return objectLocation;
    }

    public void setObjectLocation(String objectLocation) {
        this.objectLocation = objectLocation;
    }

    public String getNfcUID() {
        return nfcUID;
    }

    public void setNfcUID(String nfcUID) {
        this.nfcUID = nfcUID;
    }
}
