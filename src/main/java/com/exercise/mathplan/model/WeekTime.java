package com.exercise.mathplan.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class WeekTime implements Comparable<WeekTime> {
	
	private final DayOfWeek weekday;
	private final LocalTime beginTime;
	private final LocalTime endTime;
	private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("HH:mm");
	
	public WeekTime(DayOfWeek weekday, LocalTime beginTime, LocalTime endTime) {
		this.weekday = weekday;
		this.beginTime = beginTime;
		this.endTime = endTime;
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

	@Override
	public int hashCode() {
		return Objects.hash(beginTime, endTime, weekday);
	}
	
	public boolean hasOverlap(WeekTime other) {
		
		return (this.weekday == other.weekday) &&
			(((this.beginTime.isBefore(other.beginTime) || 
					this.beginTime.equals(other.beginTime)) && 
					this.endTime.isAfter(other.beginTime)) || 
			((this.beginTime.isAfter(other.beginTime) ||
				this.beginTime.equals(other.beginTime)) && 
				this.beginTime.isBefore(other.endTime)));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeekTime other = (WeekTime) obj;
		return Objects.equals(beginTime, other.beginTime) && Objects.equals(endTime, other.endTime)
				&& weekday == other.weekday;
	}
	
	private String localTimeString(LocalTime lt) {
		
		return lt.format(dtFormatter);
	}

	@Override
	public String toString() {
		
		return String.format("%s %s - %s", weekday.toString(), 
			localTimeString(beginTime), localTimeString(endTime));
	}

	@Override
	public int compareTo(WeekTime other) {
		
		if(this.weekday != other.weekday) {
			return this.weekday.compareTo(other.weekday);
		}
		
		if(!this.beginTime.equals(other.beginTime)) {
			return this.beginTime.compareTo(other.beginTime);
		}
		
		if(!this.endTime.equals(other.endTime)) {
			return this.endTime.compareTo(other.endTime);
		}
		
		return 0;
	}

}
