package com.jexinox.models;

import java.util.List;

public class Student {
    private final String name;
    private final String surname;
    private final int grade;
    private final String group;
    private final List<Theme> themes;
    private VkInfo vkInfo = null;

    public Student(String name, String surname, String group, int grade, List<Theme> themes) {
        this.name = name;
        this.surname = surname;
        this.grade = grade;
        this.group = group;
        this.themes = themes;
    }

    public Student WithVkInfo(VkInfo vkInfo) {
        var newStudent = new Student(name, surname, group, grade, themes);
        newStudent.vkInfo = vkInfo;
        return newStudent;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGroup() {
        return group;
    }

    public int getGrade() {
        return grade;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public VkInfo getVkInfo() {
        return vkInfo;
    }
}
