package com.example.nfcapp.model;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.intellij.lang.annotations.Identifier;

public class ObjectEntity {

    private Long objectId;
    private String objectName;
    private String objectDesc;
    private String objectLocation;
    private String nfcId;


    public ObjectEntity() {}

    public ObjectEntity(String objectName, String objectDesc, String objectLocation, String nfcId) {
        this.objectName = objectName;
        this.objectDesc = objectDesc;
        this.objectLocation = objectLocation;
        this.nfcId = nfcId;
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

    public String getNfcId() {
        return nfcId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setNfcId(String nfcUID) {
        this.nfcId = nfcId;
    }
}
