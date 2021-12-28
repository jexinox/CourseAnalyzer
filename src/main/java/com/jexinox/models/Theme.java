package com.jexinox.models;

import java.util.List;

public class Theme {
    private final String name;
    private final int maxGrade;
    private final int studentGrade;
    private final List<Task> tasks;

    public Theme(String name, int maxGrade, int studentGrade, List<Task> tasks) {
        this.name = name;
        this.maxGrade = maxGrade;
        this.studentGrade = studentGrade;
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public int getMaxGrade() {
        return maxGrade;
    }

    public int getStudentGrade() {
        return studentGrade;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
