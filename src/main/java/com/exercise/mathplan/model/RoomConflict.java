package com.exercise.mathplan.model;

import java.util.Objects;

public class RoomConflict extends Conflict {

	private final String room;

	public RoomConflict(ClassBooking classBookingA, ClassBooking classBookingB, String room) {
		super(classBookingA, classBookingB);
		this.room = room;
	}

	public String getRoom() {
		return room;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(room);
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
		RoomConflict other = (RoomConflict) obj;
		return Objects.equals(room, other.room);
	}

	@Override
	public String toString() {
		return "Room"+super.toString()+", room=" + room + "]";
	}
}
