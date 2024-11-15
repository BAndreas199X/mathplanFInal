package com.exercise.mathplan.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Conflict implements Comparable<Conflict> {
	
	private final ClassBooking classBookingA;
	private final ClassBooking classBookingB;
	private final DayOfWeek weekday;
	private final String overlap;
	private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("HH:mm");
	
	protected Conflict(ClassBooking classBookingA, ClassBooking classBookingB) {
		
		this.classBookingA = classBookingA;
		this.classBookingB = classBookingB;
		this.weekday = classBookingA.getWeekday();
		this.overlap = determineOverlap(classBookingA, classBookingB);
	}
	
	public ClassBooking getClassBookingA() {
		return classBookingA;
	}

	public ClassBooking getClassBookingB() {
		return classBookingB;
	}

	public DayOfWeek getWeekday() {
		return weekday;
	}
	
	public String getOverlap() {
		return overlap;
	}

	
	private String determineOverlap(ClassBooking classBookingA, 
		ClassBooking classBookingB) {
		
		List<LocalTime> localTimeList = Arrays.asList(
			classBookingA.getBeginTime(), classBookingA.getEndTime(), 
			classBookingB.getBeginTime(), classBookingB.getEndTime());
		
		Collections.sort(localTimeList);
		
		return new StringBuilder().append(localTimeString(localTimeList.get(1)))
			.append(" - ").append(localTimeList.get(2)).toString();
	}
	
	
	private String localTimeString(LocalTime lt) {
		
		return lt.format(dtFormatter);
	}
	
	
	@Override
	public int compareTo(Conflict other) {
		
		if(this.weekday != other.weekday) {
			return this.weekday.compareTo(other.weekday);
		}
		
		if(!this.overlap.equals(other.overlap)) {
			return this.overlap.compareTo(other.overlap);
		}
		
		return this.classBookingA.getId().compareTo(other.classBookingA.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(classBookingA, classBookingB, overlap, weekday);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conflict other = (Conflict) obj;
		return Objects.equals(classBookingA, other.classBookingA) && Objects.equals(classBookingB, other.classBookingB)
				&& Objects.equals(overlap, other.overlap) && weekday == other.weekday;
	}

	@Override
	public String toString() {
		return "Conflict [classBookingA=" + classBookingA + ", classBookingB=" + classBookingB + ", weekday=" + weekday
				+ ", overlap=" + overlap;
	}
	
	
}
