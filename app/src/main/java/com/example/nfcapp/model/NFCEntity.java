package com.example.nfcapp.model;

public class NFCEntity {
    private String nfcId;

    public NFCEntity() {}

    public NFCEntity(String nfcId) { // Change parameter name to match the field
        this.nfcId = nfcId; // Correct assignment
    }

    // Getter
    public String getNfcId() {
        return nfcId;
    }

    // Setter
    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }
}
