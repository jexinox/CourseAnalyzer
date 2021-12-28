package com.jexinox.parsers;

import java.util.List;

public interface CSVCourseElementParser<T> {
    T Parse(List<String[]> csvLines, int line);
}
