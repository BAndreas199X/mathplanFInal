package com.exercise.mathplan.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.exercise.mathplan.model.ClassBooking;
import com.exercise.mathplan.model.Conflict;
import com.exercise.mathplan.model.CurricularConflict;
import com.exercise.mathplan.model.RoomConflict;

public class ScheduleConflictParserImpl implements ScheduleConflictParser {
	
	private List<Conflict> conflictList = new ArrayList<>();

	
	public List<Conflict> findSchedulingConflicts(List<ClassBooking> cbList) {
		
		findOverlaps(cbList);
		
		return conflictList;
	}
	
	
	private void findOverlaps(List<ClassBooking> cbList) {
		
		for (int i = 0;i<cbList.size();i++) {
			
			ClassBooking currCB = cbList.get(i);
			
			List<ClassBooking> overlaps = IntStream.range(i + 1, cbList.size())
				.filter(j -> currCB.getWeekTime().hasOverlap(cbList.get(j).getWeekTime()))
				.mapToObj(cbList::get).toList();
			
			findConflicts(currCB, overlaps);
		}
	}
	
	
	private void findConflicts(ClassBooking currentCB, 
			List<ClassBooking> overlaps) {
		
		for (ClassBooking cb: overlaps) {
			
			if (currentCB.hasRoomOverlap(cb)) {
				
				conflictList.add(new RoomConflict(currentCB, cb, currentCB.getRoom()));
			}
			
			String scheduleConflicts = currentCB.conflictingMajors(cb);
			
			if (!"".equals(scheduleConflicts)) {
				
				conflictList.add(new CurricularConflict(currentCB, cb, scheduleConflicts));
			}
		}
	}
}
