package com.exercise.mathplan.parsers;

import java.util.List;

import com.exercise.mathplan.model.ClassBooking;

public interface TimetableFileParser {

	public List<ClassBooking> parseTimetableFile(String fileURL);
}
