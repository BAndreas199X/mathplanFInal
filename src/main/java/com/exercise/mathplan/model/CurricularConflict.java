package com.exercise.mathplan.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CurricularConflict extends Conflict{

	final String subject;
	
	public CurricularConflict(ClassBooking classBookingA, ClassBooking classBookingB, 
			String subject) {
		super(classBookingA, classBookingB);
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}
	
	public static String getConflictingMajor(ClassBooking cb1, ClassBooking cb2) {
		
		Set<String> copyCurriculum1 = new HashSet<>(cb1.getInCurriculum());
		copyCurriculum1.retainAll(cb2.getInCurriculum());
		
		return copyCurriculum1.stream().collect(Collectors.joining(", "));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(subject);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurricularConflict other = (CurricularConflict) obj;
		return Objects.equals(subject, other.subject);
	}

	@Override
	public String toString() {
		return "CurricularConflict [subject=" + subject + ", classBookingA=" + classBookingA + ", classBookingB="
				+ classBookingB + ", weekday=" + weekday + "]";
	}
}
