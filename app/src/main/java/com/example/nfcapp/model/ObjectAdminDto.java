package com.example.nfcapp.model;

public class ObjectAdminDto {

    private ObjectDto objectDto;
    private AdminDto adminDto;

    public ObjectAdminDto(ObjectDto objectDto, AdminDto adminDto) {
        this.objectDto = objectDto;
        this.adminDto = adminDto;
    }

    public ObjectDto getObjectDto() {
        return objectDto;
    }

    public AdminDto getAdminDto() {
        return adminDto;
    }
}
