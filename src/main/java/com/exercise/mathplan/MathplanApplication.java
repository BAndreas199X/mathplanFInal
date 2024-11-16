package com.exercise.mathplan;

import java.util.Collections;
import java.util.List;

import com.exercise.mathplan.model.ClassBooking;
import com.exercise.mathplan.model.Conflict;
import com.exercise.mathplan.model.CurricularConflict;
import com.exercise.mathplan.model.RoomConflict;
import com.exercise.mathplan.parsers.TimetableFileParser;
import com.exercise.mathplan.parsers.TimetableFileParserImpl;
import com.exercise.mathplan.parsers.ScheduleConflictParser;
import com.exercise.mathplan.parsers.ScheduleConflictParserImpl;

public class MathplanApplication {
	
	private static final String TIMETABLE_URL = "./src/main/resources/timetable.xml";
	
	private static final TimetableFileParser TTFILEPARSER = new TimetableFileParserImpl();
	
	private static String ROOMCONFLICT_MSG = """
		%s: Courses '%s' and '%s' have a conflict for room '%s' 
		""";
	
	private static String CURRICULARCONFLICT_MSG = """
		%s: Courses '%s' and '%s' have a conflict for curriculum/a '%s'
		""";
	
	public static void main(String[] args) {
		
		List<ClassBooking> cbList = TTFILEPARSER.parseTimetableFile(TIMETABLE_URL);
		
		if(cbList != null) {
			ScheduleConflictParser schedConflPars = new ScheduleConflictParserImpl();
			resultPresentation(schedConflPars.findSchedulingConflicts(cbList));
		}
	}

	
	private static void resultPresentation(List<Conflict> conflictMap) {
		
		Collections.sort(conflictMap);
		
		List<RoomConflict> rcList = conflictMap.stream().filter(RoomConflict.class::isInstance)
			.map(RoomConflict.class::cast).toList();
		List<CurricularConflict> ccList = conflictMap.stream()
			.filter(CurricularConflict.class::isInstance).map(CurricularConflict.class::cast)
			.toList();
		
		System.out.printf("%nThere were %d room conflicts discovered: %n", rcList.size());
		rcList.forEach(st -> printRoomConflict(st));
		
		System.out.printf("%nThere were %d curricular conflicts discovered: %n", ccList.size());
		ccList.forEach(st -> printCurricularConflict(st));
	}

	
	private static void printRoomConflict(RoomConflict roomConfl) {
		
		System.out.printf(ROOMCONFLICT_MSG, roomConfl.getOverlap().toString(), 
			roomConfl.getClassBookingA().getId(), roomConfl.getClassBookingB().getId(), 
			roomConfl.getRoom());
	}
	
	
	private static void printCurricularConflict(CurricularConflict currConfl) {
		
		System.out.printf(CURRICULARCONFLICT_MSG, currConfl.getOverlap().toString(), 
				currConfl.getClassBookingA().getId(), currConfl.getClassBookingB().getId(), 
				currConfl.getSubject());
	}
}
