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
					.filter(j -> hasOverLap(currCB, cbList.get(j)))
					.mapToObj(cbList::get).toList();
			
			findConflicts(currCB, overlaps);
		}
	}
	
	
	private void findConflicts(ClassBooking currentCB, 
			List<ClassBooking> overlaps) {
		
		for (ClassBooking cb: overlaps) {
			
			if (currentCB.getRoom().equals(cb.getRoom())) {
				
				conflictList.add(new RoomConflict(currentCB, cb, currentCB.getRoom()));
			}
			
			String scheduleConflicts = CurricularConflict.getConflictingMajor(currentCB, cb);
			
			if (!"".equals(scheduleConflicts)) {
				
				conflictList.add(new CurricularConflict(currentCB, cb, scheduleConflicts));
			}
		}
	}
	
	
	private boolean hasOverLap(ClassBooking cb1, ClassBooking cb2) {
		
		return (cb1.getWeekday() == cb2.getWeekday()) &&
			(((cb1.getBeginTime().isBefore(cb2.getBeginTime()) || 
			cb1.getBeginTime().equals(cb2.getBeginTime())) && 
			cb1.getEndTime().isAfter(cb2.getBeginTime())) || 
			((cb1.getBeginTime().isAfter(cb2.getBeginTime()) ||
			cb1.getBeginTime().equals(cb2.getBeginTime())) && 
			cb1.getBeginTime().isBefore(cb2.getEndTime())));
	}
}
