package com.exercise.mathplan.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassBooking {

	private final String id;
	private final String room;
	private final WeekTime weektime;
	private final Set<String> inCurriculum;
	
	public ClassBooking(String id, WeekTime weektime, String room, Set<String> inCurriculum) {
		
		this.id = id;
		this.weektime = weektime;
		this.room = room;
		this.inCurriculum = inCurriculum;
	}
	
	public String getId() {
		return id;
	}

	public String getRoom() {
		return room;
	}

	public Set<String> getInCurriculum() {
		return inCurriculum;
	}
	
	public WeekTime getWeekTime() {
		return this.weektime;
	}
	
	public boolean hasRoomOverlap(ClassBooking other) {
		
		return this.room.equals(other.room);
	}
	
	public String conflictingMajors(ClassBooking other) {
		
		return setOverlap(this, other).stream().collect(Collectors.joining(", "));
	}
	
	private Set<String> setOverlap(ClassBooking cb1, ClassBooking cb2){
		
		Set<String> copyCurriculum = new HashSet<>(cb1.getInCurriculum());
		copyCurriculum.retainAll(cb2.getInCurriculum());
		
		return copyCurriculum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, inCurriculum, room, weektime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassBooking other = (ClassBooking) obj;
		return Objects.equals(id, other.id) && Objects.equals(inCurriculum, other.inCurriculum)
				&& Objects.equals(room, other.room) && Objects.equals(weektime, other.weektime);
	}

	@Override
	public String toString() {
		return "ClassBooking [id=" + id + ", room=" + room + ", weektime=" + weektime + ", inCurriculum=" + inCurriculum
				+ "]";
	}
}
