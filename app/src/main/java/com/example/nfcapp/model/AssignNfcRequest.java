package com.example.nfcapp.model;

import com.example.nfcapp.api.ObjectDto;

public class AssignNfcRequest {
    private ObjectDto objectDto;
    private String nfcUID;

    // Constructors
    public AssignNfcRequest(ObjectDto objectDto, String nfcUid) {
        this.objectDto = objectDto;
        this.nfcUID = nfcUID;
    }

    // Getters and setters
    public ObjectDto getObjectEntity() {
        return objectDto;
    }

    public void setObjectEntity(ObjectDto objectDto) {
        this.objectDto = objectDto;
    }

    public String getNfcUid() {
        return nfcUID;
    }

    public void setNfcUid(String nfcUID) {
        this.nfcUID = nfcUID;
    }
}
