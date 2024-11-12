package com.exercise.mathplan.parsers;

import java.util.List;

import com.exercise.mathplan.model.ClassBooking;
import com.exercise.mathplan.model.Conflict;

public interface ScheduleConflictParser {

	public List<Conflict> findSchedulingConflicts(List<ClassBooking> cbList);
}
