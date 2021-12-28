package com.jexinox.models;

public class Task {
    private final String name;
    private final TaskType taskType;
    private final int maxGrade;
    private final int studentsGrade;

    public Task(String name, TaskType taskType, int maxGrade, int studentsGrade) {
        this.name = name;
        this.taskType = taskType;
        this.maxGrade = maxGrade;
        this.studentsGrade = studentsGrade;
    }

    public String getName() {
        return name;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public int getMaxGrade() {
        return maxGrade;
    }

    public int getStudentsGrade() {
        return studentsGrade;
    }
}
