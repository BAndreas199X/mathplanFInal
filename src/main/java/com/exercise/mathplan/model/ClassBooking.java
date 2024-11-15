package com.exercise.mathplan.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

public class ClassBooking {

	private final String id;
	private final String room;
	private final DayOfWeek weekday;
	private final LocalTime beginTime;
	private final LocalTime endTime;
	private final Set<String> inCurriculum;
	
	public ClassBooking(String id, String room, DayOfWeek weekday, LocalTime beginTime, 
			LocalTime endTime, Set<String> inCurriculum) {
		
		this.id = id;
		this.room = room;
		this.weekday = weekday;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.inCurriculum = inCurriculum;
	}
	
	public String getId() {
		return id;
	}

	public String getRoom() {
		return room;
	}

	public DayOfWeek getWeekday() {
		return weekday;
	}

	public LocalTime getBeginTime() {
		return beginTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public Set<String> getInCurriculum() {
		return inCurriculum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(beginTime, endTime, id, inCurriculum, room, weekday);
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
		return Objects.equals(beginTime, other.beginTime) && Objects.equals(endTime, other.endTime)
				&& Objects.equals(id, other.id) && Objects.equals(inCurriculum, other.inCurriculum)
				&& Objects.equals(room, other.room) && weekday == other.weekday;
	}

	@Override
	public String toString() {
		return "ClassBooking [id=" + id + ", room=" + room + ", weekday=" + weekday + ", beginTime=" + beginTime
				+ ", endTime=" + endTime + ", inCurriculum=" + inCurriculum + "]";
	}
}
