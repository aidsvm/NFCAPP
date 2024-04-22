package com.example.nfcapp.model;

public class AssignNfcRequest {
    private Long objectId;
    private String nfcUID;

    // Constructor, getters, and setters
    public AssignNfcRequest(Long objectId, String nfcUid) {
        this.objectId = objectId;
        this.nfcUID = nfcUid;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getNfcUid() {
        return nfcUID;
    }

    public void setNfcUid(String nfcUID) {
        this.nfcUID = nfcUID;
    }
}
