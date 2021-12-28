package com.jexinox.parsers;

import com.jexinox.models.Task;
import com.jexinox.models.Theme;

import java.util.ArrayList;
import java.util.List;

public class StudentsThemesParser implements CSVCourseElementParser<List<Theme>> {
    private final CSVCourseElementParser<List<Task>> tasksParser;

    public StudentsThemesParser(CSVCourseElementParser<List<Task>> tasksParser) {
        this.tasksParser = tasksParser;
    }

    public List<Theme> Parse(List<String[]> csvLines, int studentLine) {
        var themes = new ArrayList<Theme>();
        var tasks = tasksParser.Parse(csvLines, studentLine);

        var hwNames = csvLines.get(1);

        var lastHwCount = 0;
        var hwCount = 0;
        var maxGrade = 0;
        var name = "";
        var studentGrade = 0;

        var csvLine = csvLines.get(studentLine);

        for (var i = 3; i < csvLine.length; ++i) {
            if (!hwNames[i].equals("ДЗ")) {
                ++hwCount;
                continue;
            }
            if (!name.isBlank())
                themes.add(new Theme(name, maxGrade, studentGrade, tasks.subList(lastHwCount, lastHwCount + hwCount)));

            lastHwCount = hwCount;
            hwCount = 0;
            maxGrade = parseMaxGrade(csvLines, i);
            name = parseName(csvLines, i);
            studentGrade = Integer.parseInt(csvLine[i]);
        }

        themes.add(new Theme(name, maxGrade, studentGrade, tasks.subList(lastHwCount, lastHwCount + hwCount)));

        return themes;
    }

    private int parseMaxGrade(List<String[]> csvLines, int column) {
        var maxGrades = csvLines.get(2);

        return Integer.parseInt(maxGrades[column]);
    }

    private String parseName(List<String[]> csvLines, int column) {
        var names = csvLines.get(0);

        return names[column];
    }
}
