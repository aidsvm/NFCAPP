package com.example.nfcapp.model;

public class ObjectDto {
    private String objectName;
    private String objectDesc;
    private String objectLocation;
    private String nfcId;
    private Long adminId;

    public ObjectDto(String objectName, String objectDesc, String objectLocation, String nfcId, Long adminId) {
        this.objectName = objectName;
        this.objectDesc = objectDesc;
        this.objectLocation = objectLocation;
        this.nfcId = nfcId;
        this.adminId = adminId;
    }

    private Long getAdminId() {
        return adminId;
    }

    private void setAdminId(Long adminId) {
        this.adminId = adminId;
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
