package com.jexinox;

import com.jexinox.db.MongoReader;
import com.jexinox.db.MongoWriter;
import com.jexinox.handlers.VkApiHandler;
import com.jexinox.parsers.CourseParser;
import com.jexinox.parsers.StudentParser;
import com.jexinox.parsers.StudentsTasksParser;
import com.jexinox.parsers.StudentsThemesParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, CsvException, ClientException, ApiException, InterruptedException {
        var parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        var fileReader = new FileReader(
                "C:\\Users\\jexin\\IdeaProjects\\CourseAnalyzer\\src\\main\\resources\\java-rtf.csv");

        var reader = new CSVReaderBuilder(fileReader)
                .withCSVParser(parser)
                .build();

        var csv = reader.readAll();

        var vkHandler = new VkApiHandler("iot_second_urfu");

        var taskParser = new StudentsTasksParser();

        var themesParser = new StudentsThemesParser(taskParser);

        var studentsParser = new StudentParser(themesParser);

        var courseParser = new CourseParser(studentsParser, vkHandler);

        var course = courseParser.Parse(csv, "java");

        var mongo = new MongoWriter("localhost", 27017, "courseAnalyzer");

        mongo.WriteCourse(course);

        var mongoRead = new MongoReader("localhost", 27017, "courseAnalyzer");
    }
}
