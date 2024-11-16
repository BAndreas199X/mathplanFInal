package com.exercise.mathplan;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exercise.mathplan.model.ClassBooking;
import com.exercise.mathplan.model.Conflict;
import com.exercise.mathplan.model.CurricularConflict;
import com.exercise.mathplan.model.RoomConflict;
import com.exercise.mathplan.model.WeekTime;
import com.exercise.mathplan.parsers.ScheduleConflictParser;
import com.exercise.mathplan.parsers.ScheduleConflictParserImpl;

import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

class ScheduleConflictParserTest {
	
	final String ID1 = "l-1";
	final String ID2 = "l-2";
	final String ID3 = "l-3";
	final String ID4 = "l-4";
	final String ROOM = "GENERIC_ROOM";
	final String ALTROOM = "ANOTHER ROOM";
	final DayOfWeek WEEKDAY = DayOfWeek.MONDAY;
	final Set<String> CURRICULUM = Set.of("Maths", "Physics");
	final LocalTime BEGIN = LocalTime.of(12,0,0);
	final LocalTime END = LocalTime.of(18,0,0);
	final List<String> POSSIBLERES = List.of("Maths, Physics", "Physics, Maths");
	final Set<String> ALTCURRICULUM = Set.of("English");
	final WeekTime WEEKTIME = new WeekTime(WEEKDAY, BEGIN, END);
	
	
	final ClassBooking CLASS_BOOKING = new ClassBooking(ID1, WEEKTIME, ROOM, CURRICULUM);
	
	private static List<ClassBooking> classBookingList;
	private  ScheduleConflictParser schedConflPars;
	
	@BeforeEach
	void setUp() {

		classBookingList = new ArrayList<>();
		classBookingList.add(CLASS_BOOKING);
		schedConflPars = new ScheduleConflictParserImpl();
	}

	@Test
	void testFindSchedulingConflictsNoOverlap() {
		//different weekday
		classBookingList.add(new ClassBooking(ID2, new WeekTime(DayOfWeek.TUESDAY, BEGIN, END), 
			ROOM, CURRICULUM));
		
		//after first class ends
		final LocalTime lBEGIN = LocalTime.of(19,0,0);
		final LocalTime lEND = LocalTime.of(20,0,0);
		classBookingList.add(new ClassBooking(ID3, new WeekTime(WEEKDAY, lBEGIN, lEND), ROOM,
				CURRICULUM));
		
		//before first class begins
		final LocalTime lBEGIN2 = LocalTime.of(10,0,0);
		final LocalTime lEND2 = LocalTime.of(11,0,0);
		classBookingList.add(new ClassBooking(ID4, new WeekTime(WEEKDAY, lBEGIN2, lEND2), ROOM,
				CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(0, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(0, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
	}
	
	@Test
	void testFindSchedulingConflictsOverlapNoConflict() {
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, BEGIN, END), 
				ALTROOM, ALTCURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(0, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(0, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
	}
	
	@Test
	void testFindSchedulingConflictsNoSchedule() {
		
		List<ClassBooking> emptyCB = new ArrayList<>();
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(emptyCB);
		
		assertEquals(0, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(0, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		
		
		List<Conflict> conflictList2 = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(0, conflictList2.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(0, conflictList2.stream().filter(CurricularConflict.class::isInstance).count());
	}
	
	@Test
	void testFindSchedulingConflictsRoomOverlap() {
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, BEGIN, END), 
				ROOM, ALTCURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(1, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(0, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		
		RoomConflict currConfl = (RoomConflict) conflictList.get(0);
		
		assertEquals(ROOM, currConfl.getRoom());
		assertEquals("MONDAY 12:00 - 18:00", currConfl.getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsSingleCurriculumOverlap() {
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, BEGIN, END), 
				ALTROOM, Set.of("Maths")));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(0, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		
		CurricularConflict currConfl = (CurricularConflict) conflictList.get(0);
		
		assertEquals("Maths", currConfl.getSubject());
		assertEquals("MONDAY 12:00 - 18:00", currConfl.getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsDoubleCurriculumOverlap() {
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, BEGIN, END), 
				ALTROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(0, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		
		CurricularConflict currConfl = (CurricularConflict) conflictList.get(0);
		
		assertTrue(POSSIBLERES.contains(currConfl.getSubject()));
		assertEquals("MONDAY 12:00 - 18:00", currConfl.getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsBeginEndBefore() {
		
		final LocalTime lBEGIN = LocalTime.of(9,0,0);
		final LocalTime lEND = LocalTime.of(15,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, lBEGIN, lEND), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(1, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		
		assertEquals("MONDAY 12:00 - 15:00", conflictList.get(0).getOverlap().toString());
		assertEquals("MONDAY 12:00 - 15:00", conflictList.get(1).getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsBeginEndAfter() {
		
		final LocalTime lBEGIN = LocalTime.of(15,0,0);
		final LocalTime lEND = LocalTime.of(21,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, lBEGIN, lEND), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(1, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
	
		assertEquals("MONDAY 15:00 - 18:00", conflictList.get(0).getOverlap().toString());
		assertEquals("MONDAY 15:00 - 18:00", conflictList.get(1).getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsBeginAfterEndBefore() {
		
		final LocalTime lBEGIN = LocalTime.of(14,0,0);
		final LocalTime lEND = LocalTime.of(16,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, lBEGIN, lEND), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(1, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		assertEquals("MONDAY 14:00 - 16:00", conflictList.get(0).getOverlap().toString());
		assertEquals("MONDAY 14:00 - 16:00", conflictList.get(1).getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsBeginBeforeEndAfter() {
		
		final LocalTime lBEGIN = LocalTime.of(10,0,0);
		final LocalTime lEND = LocalTime.of(20,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, lBEGIN, lEND), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(1, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		assertEquals("MONDAY 12:00 - 18:00", conflictList.get(0).getOverlap().toString());
		assertEquals("MONDAY 12:00 - 18:00", conflictList.get(1).getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsBeginAfterEndSame() {
		
		final LocalTime lBEGIN = LocalTime.of(15,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, lBEGIN, END), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(1, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		assertEquals("MONDAY 15:00 - 18:00", conflictList.get(0).getOverlap().toString());
		assertEquals("MONDAY 15:00 - 18:00", conflictList.get(1).getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsBeginSameEndBefore() {
		
		final LocalTime lEND = LocalTime.of(15,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, BEGIN, lEND), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(1, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		assertEquals("MONDAY 12:00 - 15:00", conflictList.get(0).getOverlap().toString());
		assertEquals("MONDAY 12:00 - 15:00", conflictList.get(1).getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsBeginSameEndAfter() {
		final LocalTime lEND = LocalTime.of(20,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, BEGIN, lEND), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(1, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		assertEquals("MONDAY 12:00 - 18:00", conflictList.get(0).getOverlap().toString());
		assertEquals("MONDAY 12:00 - 18:00", conflictList.get(1).getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsBeginBeforeEndSame() {
		
		final LocalTime lBEGIN = LocalTime.of(10,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, lBEGIN, END), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(1, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
	}
	
	@Test
	void testFindSchedulingConflictsSameTimes() {
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, BEGIN, END), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(1, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(1, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
		assertEquals("MONDAY 12:00 - 18:00", conflictList.get(0).getOverlap().toString());
		assertEquals("MONDAY 12:00 - 18:00", conflictList.get(1).getOverlap().toString());
	}
	
	@Test
	void testFindSchedulingConflictsSeveralConflicts() {
		
		final LocalTime lBEGIN = LocalTime.of(9,0,0);
		final LocalTime lEND = LocalTime.of(16,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, lBEGIN, lEND), 
				ROOM, CURRICULUM));
		
		final LocalTime lBEGIN2 = LocalTime.of(14,0,0);
		final LocalTime lEND2 = LocalTime.of(21,0,0);
		
		classBookingList.add(new ClassBooking(ID3, new WeekTime(WEEKDAY, lBEGIN2, lEND2), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(3, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(3, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
	}
	
	@Test
	void testFindSchedulingConflictsOddTimes() {
		
		final LocalTime lBEGIN = LocalTime.of(9,01,0);
		final LocalTime lEND = LocalTime.of(16,45,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, lBEGIN, lEND), 
				ROOM, CURRICULUM));
		
		final LocalTime lBEGIN2 = LocalTime.of(14,15,0);
		final LocalTime lEND2 = LocalTime.of(21,59,0);
		
		classBookingList.add(new ClassBooking(ID3, new WeekTime(WEEKDAY, lBEGIN2, lEND2), 
				ROOM, CURRICULUM));
		
		final LocalTime lBEGIN3 = LocalTime.of(12,30,0);
		final LocalTime lEND3 = LocalTime.of(17,30,0);
		
		classBookingList.add(new ClassBooking(ID4, new WeekTime(WEEKDAY, lBEGIN3, lEND3), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(6, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(6, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
	}
	
	@Test
	void testFindSchedulingConflictsNoConflictWithSubsequent() {
		
		final LocalTime lBEGIN = LocalTime.of(9,0,0);
		final LocalTime lEND = LocalTime.of(12,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, lBEGIN, lEND), 
				ROOM, CURRICULUM));
		
		final LocalTime lBEGIN2 = LocalTime.of(18,0,0);
		final LocalTime lEND2 = LocalTime.of(20,0,0);
		
		classBookingList.add(new ClassBooking(ID2, new WeekTime(WEEKDAY, lBEGIN2, lEND2), 
				ROOM, CURRICULUM));
		
		List<Conflict> conflictList = 
				schedConflPars.findSchedulingConflicts(classBookingList);
		
		assertEquals(0, conflictList.stream().filter(RoomConflict.class::isInstance).count());
		assertEquals(0, conflictList.stream().filter(CurricularConflict.class::isInstance).count());
	}
}
