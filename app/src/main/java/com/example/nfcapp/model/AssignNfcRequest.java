package com.example.nfcapp.model;

public class AssignNfcRequest {
    private ObjectEntity objectEntity;
    private String nfcUID;

    // Constructors
    public AssignNfcRequest(ObjectEntity objectEntity, String nfcUid) {
        this.objectEntity = objectEntity;
        this.nfcUID = nfcUID;
    }

    // Getters and setters
    public ObjectEntity getObjectEntity() {
        return objectEntity;
    }

    public void setObjectEntity(ObjectEntity objectEntity) {
        this.objectEntity = objectEntity;
    }

    public String getNfcUid() {
        return nfcUID;
    }

    public void setNfcUid(String nfcUID) {
        this.nfcUID = nfcUID;
    }
}
