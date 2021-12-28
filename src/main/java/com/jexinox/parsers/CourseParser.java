package com.jexinox.parsers;

import com.jexinox.handlers.VkApiHandler;
import com.jexinox.models.Course;
import com.jexinox.models.Student;
import com.jexinox.models.VkInfo;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.util.ArrayList;
import java.util.List;

public class CourseParser implements CSVCourseParser {
    private final CSVCourseElementParser<Student> studentParser;
    private final VkApiHandler vkApiHandler;

    public CourseParser(CSVCourseElementParser<Student> studentParser, VkApiHandler vkApiHandler) {
        this.studentParser = studentParser;
        this.vkApiHandler = vkApiHandler;
    }

    @Override
    public Course Parse(List<String[]> csvLines, String courseName)
            throws ClientException, InterruptedException, ApiException {
        var students = (List<Student>) new ArrayList<Student>();

        for (var i = 3; i < csvLines.size(); ++i) {
            students.add(studentParser.Parse(csvLines, i));
        }

        addVkInfo(students);

        return new Course(courseName, students);
    }

    private void addVkInfo(List<Student> students) throws ClientException, InterruptedException, ApiException {
        var vkApiHandleResults = vkApiHandler.getUsers();

        for (var i = 0; i < students.size(); ++i) {
            var student = students.get(i);
            for (var apiStudent : vkApiHandleResults) {
                if (apiStudent
                        .get("first_name")
                        .getAsString()
                        .equals(student.getName()) &&
                        apiStudent
                                .get("last_name")
                                .getAsString()
                                .equals(student.getSurname())) {
                    var genderEnum = apiStudent.has("sex") ?
                            apiStudent.get("sex").getAsString() : null;
                    var gender = genderEnum != null ? genderEnum.equals("1") ? "female" : "male" : null;

                    var city = apiStudent.has("city") ? apiStudent.get("city").getAsJsonObject()
                            .get("title").getAsString() : null;

                    var birthDate = apiStudent.has("bdate") ?
                            apiStudent.get("bdate").getAsString() : null;

                    var id = apiStudent.get("id").getAsString();

                    var vkInfo = new VkInfo(gender, city, birthDate, id);
                    students.set(i, student.WithVkInfo(vkInfo));
                    break;
                }
            }
        }
    }
}
