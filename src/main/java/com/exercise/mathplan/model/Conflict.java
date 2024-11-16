package com.exercise.mathplan.model;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Conflict implements Comparable<Conflict> {
	
	private final ClassBooking classBookingA;
	private final ClassBooking classBookingB;
	private final WeekTime overlap;
	
	protected Conflict(ClassBooking classBookingA, ClassBooking classBookingB) {
		
		this.classBookingA = classBookingA;
		this.classBookingB = classBookingB;
		this.overlap = createOverlap(classBookingA.getWeekTime(), classBookingB.getWeekTime());
	}
	
	private WeekTime createOverlap(WeekTime wtA, WeekTime wtB) {
		
		List<LocalTime> timesList = Arrays.asList(wtA.getBeginTime(), wtA.getEndTime(),
			wtB.getBeginTime(), wtB.getEndTime());
		
		Collections.sort(timesList);
		return new WeekTime(wtA.getWeekday(), timesList.get(1), timesList.get(2));
	}

	public ClassBooking getClassBookingA() {
		return classBookingA;
	}

	public ClassBooking getClassBookingB() {
		return classBookingB;
	}

	public WeekTime getOverlap() {
		return overlap;
	}
	
	@Override
	public int compareTo(Conflict other) {
		
		if(!overlap.equals(other.overlap)) {
			return overlap.compareTo(other.overlap);
		}
		
		if(this.classBookingA.getId().equals(other.classBookingA.getId())) {
			return this.classBookingA.getId().compareTo(other.classBookingA.getId());
		}
		
		return this.classBookingB.getId().compareTo(other.classBookingB.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(classBookingA, classBookingB, overlap);
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
				&& Objects.equals(overlap, other.overlap);
	}

	@Override
	public String toString() {
		return "Conflict [classBookingA=" + classBookingA + ", classBookingB=" + classBookingB + ", overlap=" + overlap
				+ "]";
	}
}
