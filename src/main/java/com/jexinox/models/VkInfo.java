package com.jexinox.models;

public class VkInfo {
    private final String vkId;
    private final String gender;
    private final String city;
    private final String birthDate;

    public VkInfo(String gender, String city, String birthDate, String vkId) {
        this.gender = gender;
        this.city = city;
        this.birthDate = birthDate;
        this.vkId = vkId;
    }

    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getVkId() {
        return vkId;
    }
}
