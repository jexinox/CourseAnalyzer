package com.jexinox.db;

import com.jexinox.models.Course;
import org.bson.types.ObjectId;

public interface DBWriter {
    ObjectId WriteCourse(Course course);
}
