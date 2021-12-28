package com.jexinox.parsers;

import com.jexinox.models.Student;
import com.jexinox.models.Theme;

import java.util.List;

public class StudentParser implements CSVCourseElementParser<Student> {
    private final CSVCourseElementParser<List<Theme>> themesParser;

    public StudentParser(CSVCourseElementParser<List<Theme>> themesParser) {
        this.themesParser = themesParser;
    }

    public Student Parse(List<String[]> csvLines, int line) {
        var csvLine = csvLines.get(line);

        var split = csvLine[0].split(" ", 2);

        var name = split[1].trim();
        var surname = split[0].trim();
        var group = csvLine[1];
        var grade = Integer.parseInt(csvLine[2]);
        var themes = themesParser.Parse(csvLines, line);

        return new Student(name, surname, group, grade, themes);
    }
}
