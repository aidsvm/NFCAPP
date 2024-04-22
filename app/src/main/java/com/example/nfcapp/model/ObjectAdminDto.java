package com.example.nfcapp.model;

public class ObjectAdminDto {
    private ObjectDto objectDto;
    private AdminDto adminDto;

    // Constructor to ensure both DTOs are initialized
    public ObjectAdminDto(ObjectDto objectDto, AdminDto adminDto) {
        this.objectDto = objectDto;
        this.adminDto = adminDto;
    }

    // Getters for encapsulation
    public ObjectDto getObjectDto() {
        return objectDto;
    }

    public AdminDto getAdminDto() {
        return adminDto;
    }


}




