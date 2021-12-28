package com.jexinox.parsers;

import com.jexinox.models.Course;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.util.List;

public interface CSVCourseParser {
    Course Parse(List<String[]> csvLines, String courseName)
            throws ClientException, InterruptedException, ApiException;
}
