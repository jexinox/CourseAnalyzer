package com.jexinox.db;

import com.jexinox.models.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class MongoWriter implements DBWriter {
    private final MongoDatabase db;

    public MongoWriter(String host, int port, String dbName) {
        var client = new MongoClient(host, port);

        db = client.getDatabase(dbName);

        db.drop();
    }

    public ObjectId WriteCourse(Course course) {
        var collection = db.getCollection("courses");

        var insertValue = new Document("_id", new ObjectId()).append("name", course.getName());

        var id = new ObjectId(insertValue.get("_id").toString());

        var students = WriteStudents(course.getStudents(), id);
        insertValue.append("students", students);

        collection.insertOne(insertValue);

        return id;
    }

    private List<ObjectId> WriteStudents(List<Student> students, ObjectId courseId) {
        var collection = db.getCollection("students");
        var resultIds = new ArrayList<ObjectId>();

        for (var student : students) {
            var insertValue = new Document("_id", new ObjectId())
                    .append("name", student.getName())
                    .append("surname", student.getSurname())
                    .append("grade", student.getGrade())
                    .append("group", student.getGroup())
                    .append("course_id", courseId);

            var id = new ObjectId(insertValue.get("_id").toString());
            resultIds.add(id);

            var vkInfo = WriteVkInfo(student.getVkInfo(), id);
            insertValue.append("vk_info", vkInfo);

            var themes = WriteThemes(student.getThemes(), id);
            insertValue.append("themes", themes);

            collection.insertOne(insertValue);
        }

        return resultIds;
    }

    private ObjectId WriteVkInfo(VkInfo vkInfo, ObjectId studentId) {
        if (vkInfo == null) return null;

        var collection = db.getCollection("vkInfo");

        var insertValue = new Document("_id", new ObjectId())
                .append("student_id", studentId)
                .append("vk_id", vkInfo.getVkId())
                .append("city", vkInfo.getCity())
                .append("birth_date", vkInfo.getBirthDate())
                .append("gender", vkInfo.getGender());

        collection.insertOne(insertValue);

        return new ObjectId(insertValue.get("_id").toString());
    }

    private List<ObjectId> WriteThemes(List<Theme> themes, ObjectId studentId) {
        if (themes == null) return null;
        var collection = db.getCollection("studentsThemes");
        var resultIds = new ArrayList<ObjectId>();

        for (var theme : themes) {
            var insertValue = new Document("_id", new ObjectId())
                    .append("name", theme.getName())
                    .append("max_grade", theme.getMaxGrade())
                    .append("student_grade", theme.getStudentGrade())
                    .append("student_id", studentId);

            var id = new ObjectId(insertValue.get("_id").toString());
            resultIds.add(id);

            var tasks = WriteTasks(theme.getTasks(), id);
            insertValue.append("tasks", tasks);

            collection.insertOne(insertValue);
        }
        return resultIds;
    }

    private List<ObjectId> WriteTasks(List<Task> tasks, ObjectId themeId) {
        if (tasks == null) return null;
        var collection = db.getCollection("studentsTasks");
        var resultIds = new ArrayList<ObjectId>();

        for (var task : tasks) {
            var insertValue = new Document("_id", new ObjectId())
                    .append("name", task.getName())
                    .append("max_grade", task.getMaxGrade())
                    .append("student_grade", task.getStudentsGrade())
                    .append("task_type", task.getTaskType().toString())
                    .append("theme_id", themeId);

            var id = new ObjectId(insertValue.get("_id").toString());
            resultIds.add(id);

            collection.insertOne(insertValue);
        }

        return resultIds;
    }
}
