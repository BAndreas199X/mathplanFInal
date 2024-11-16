package com.exercise.mathplan.model;

import java.util.Objects;

public class CurricularConflict extends Conflict{

	private final String subject;
	
	public CurricularConflict(ClassBooking classBookingA, ClassBooking classBookingB, 
			String subject) {
		
		super(classBookingA, classBookingB);
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
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
		return "Curricular"+super.toString()+", subject=" + subject + "]";
	}
}
