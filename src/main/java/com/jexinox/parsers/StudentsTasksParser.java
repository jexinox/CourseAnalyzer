package com.jexinox.parsers;

import com.jexinox.models.Task;
import com.jexinox.models.TaskType;

import java.util.ArrayList;
import java.util.List;

public class StudentsTasksParser implements CSVCourseElementParser<List<Task>> {
    public List<Task> Parse(List<String[]> csvLines, int studentLine) {
        var tasks = new ArrayList<Task>();

        var names = csvLines.get(1);

        var csvLine = csvLines.get(studentLine);

        for (var i = 4; i < csvLine.length; i++) {
            if (names[i].equals("ДЗ")) {
                continue;
            }

            var name = parseName(csvLines, i);
            var type = identifyType(name);
            var maxGrade = parseMaxGrade(csvLines, i);

            tasks.add(new Task(name, type, maxGrade, Integer.parseInt(csvLine[i])));
        }

        return tasks;
    }

    private String parseName(List<String[]> csvLines, int column) {
        var names = csvLines.get(1);

        return names[column];
    }

    private TaskType identifyType(String name) {
        return name.contains("вопрос") ? TaskType.Question : TaskType.Coding;
    }

    private int parseMaxGrade(List<String[]> csvLines, int column) {
        var maxGrades = csvLines.get(2);
        return Integer.parseInt(maxGrades[column]);
    }
}
