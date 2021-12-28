package com.jexinox.db;

import com.jexinox.models.*;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class MongoReader implements DBReader {
    private final MongoDatabase db;

    public MongoReader(String host, int port, String dbName) {
        var client = new MongoClient(host, port);

        db = client.getDatabase(dbName);
    }

    public Course ReadCourse(String courseName) {
        var collection = db.getCollection("courses");

        var getQuery = new BasicDBObject();

        getQuery.append("name", courseName);

        var course = collection.find(getQuery).first();

        return DeserializeCourse(course);
    }

    public List<Student> ReadStudents(List<ObjectId> studentsIds) {
        var students = new ArrayList<Student>();

        for (var id : studentsIds) {
            students.add(ReadStudent(id));
        }

        return students;
    }

    public Student ReadStudent(ObjectId studentId) {
        var collection = db.getCollection("students");

        var getQuery = new BasicDBObject();
        getQuery.put("_id", studentId);

        var student = collection.find(getQuery).first();

        return DeserializeStudent(student);
    }

    public Student ReadStudent(String name, String surname) {
        var collection = db.getCollection("students");

        var getQuery = new BasicDBObject();
        getQuery.put("name", name);
        getQuery.put("surname", surname);

        var student = collection.find(getQuery).first();

        return DeserializeStudent(student);
    }

    public VkInfo ReadVkInfo(ObjectId vkInfoId) {
        var collection = db.getCollection("vkInfo");

        var getQuery = new BasicDBObject();
        getQuery.put("_id", vkInfoId);

        var vkInfo = collection.find(getQuery).first();

        return DeserializeVkInfo(vkInfo);
    }

    public List<Theme> ReadThemes(List<ObjectId> themesIds) {
        var themes = new ArrayList<Theme>();

        for (var themeId : themesIds) {
            themes.add(ReadTheme(themeId));
        }

        return themes;
    }

    public Theme ReadTheme(ObjectId themeId) {
        var collection = db.getCollection("studentsThemes");

        var getQuery = new BasicDBObject();
        getQuery.put("_id", themeId);

        var theme = collection.find(getQuery).first();

        return DeserializeTheme(theme);
    }

    public List<Theme> ReadThemes(String themeName) {
        var collection = db.getCollection("studentsThemes");

        var getQuery = new BasicDBObject();
        getQuery.put("name", themeName);

        var themesDocs = collection.find(getQuery);

        var themes = new ArrayList<Theme>();

        for (var theme : themesDocs) {
            themes.add(DeserializeTheme(theme));
        }

        return themes;
    }

    public List<Task> ReadTasks(List<ObjectId> tasksIds) {
        var tasks = new ArrayList<Task>();

        for (var taskId : tasksIds) {
            tasks.add(ReadTask(taskId));
        }

        return tasks;
    }

    public Task ReadTask(ObjectId taskId) {
        var collection = db.getCollection("studentsTasks");

        var getQuery = new BasicDBObject();
        getQuery.put("_id", taskId);

        var task = collection.find(getQuery).first();

        return DeserializeTask(task);
    }

    private Task DeserializeTask(Document task) {
        if (task == null) return null;

        var name = task.get("name").toString();
        var maxGrade = Integer.parseInt(task.get("max_grade").toString());
        var currentGrade = Integer.parseInt(task.get("student_grade").toString());
        var taskType = TaskType.valueOf(task.get("task_type").toString());

        return new Task(name, taskType, maxGrade, currentGrade);
    }

    private Course DeserializeCourse(Document course) {
        if (course == null) return null;
        var name = course.get("name").toString();

        var students = ReadStudents(course.getList("students", ObjectId.class));

        return new Course(name, students);
    }

    private Student DeserializeStudent(Document student) {
        if (student == null) return null;

        var name = student.get("name").toString();
        var surname = student.get("surname").toString();
        var grade = Integer.parseInt(student.get("grade").toString());
        var group = student.get("group").toString();
        var themes = ReadThemes(student.getList("themes", ObjectId.class));
        var vkInfo = ReadVkInfo(student.get("vk_info", ObjectId.class));

        return new Student(name, surname, group, grade, themes).WithVkInfo(vkInfo);
    }

    private VkInfo DeserializeVkInfo(Document vkInfo) {
        if (vkInfo == null) return null;

        var city = vkInfo.get("city") == null ? "null" : vkInfo.get("city").toString();
        var gender = vkInfo.get("gender") == null ? "null" : vkInfo.get("gender").toString();
        var id = vkInfo.get("vk_id").toString();
        var birthDate = vkInfo.get("birth_date") == null ? "null" : vkInfo.get("birth_date").toString();

        return new VkInfo(gender, city, birthDate, id);
    }

    private Theme DeserializeTheme(Document theme) {
        if (theme == null) return null;

        var name = theme.get("name").toString();
        var maxGrade = Integer.parseInt(theme.get("max_grade").toString());
        var currentGrade = Integer.parseInt(theme.get("student_grade").toString());
        var tasks = ReadTasks(theme.getList("tasks", ObjectId.class));

        return new Theme(name, maxGrade, currentGrade, tasks);
    }
}
