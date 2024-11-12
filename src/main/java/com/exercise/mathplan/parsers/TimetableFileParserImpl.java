package com.exercise.mathplan.parsers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;

import com.exercise.mathplan.model.ClassBooking;

import generated.Booking;
import generated.Curriculum;
import generated.Lecture;
import generated.Roombookings;
import generated.University;

public class TimetableFileParserImpl implements TimetableFileParser {

	private JAXBContext jaxbContext;
	final DateTimeFormatter WEEKDAYFORMATTER = DateTimeFormatter.ofPattern("E", Locale.US);
	
	
	public List<ClassBooking> parseTimetableFile(String fileURL) {
		
		try {
			University university = (University) JAXBIntrospector.getValue(
				getJaxbContext().createUnmarshaller().unmarshal(new FileReader(fileURL)));
			
			return parseLectures(university);
		} catch (JAXBException e) {
			
			System.out.println("Error: The TimeTable-file could not be parsed!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			System.out.println("Error: The TimeTable-file was not found!");
			e.printStackTrace();
		}	
		
		return null;
	}
	
	
	private JAXBContext getJaxbContext() throws JAXBException {
		
		if(jaxbContext == null) {
			
			jaxbContext = JAXBContext.newInstance(University.class);
		}
		
		return jaxbContext;
	}
	
	
	private Map<String, List<String>> parseCurriculum(University university) {
		
		Map<String,List<String>> curriculumMap = new HashMap<>();
		
		university.getCurricula().getCurriculum().forEach(
			curr -> curriculumMap.put(curr.getName(), 
				buildStringPresentationOfCurriculum(curr)));
		
		return curriculumMap;
	}
	
	
	private List<String> buildStringPresentationOfCurriculum(Curriculum curr) {   
		
	    List<Lecture> newCurriculum =
	        curr.getLecture().stream().map(entry -> (Lecture) entry.getValue()).toList();
	    
	    return newCurriculum.stream().
	        map(Lecture::getId).toList();
	}
	

	private List<ClassBooking> parseLectures(University university) {

		Map<String, List<String>> curriculumMap = parseCurriculum(university);
		
		List<List<ClassBooking>> cbList = university.getLectures().getLecture()
			.stream().map(lecture -> parseBookings(lecture.getId(), 
				lecture.getRoombookings(), curriculumMap)).toList();
		
		
		return new ArrayList<>(cbList.stream().flatMap(Collection::stream).toList());
	}
	
	
	private List<ClassBooking> parseBookings(String id, 
			Roombookings roomBookings, Map<String, List<String>> curriculumMap)  {
		
		List<ClassBooking> classBookings = new ArrayList<>();
		
		for (Booking b: roomBookings.getBooking()) {
			
			classBookings.add(new ClassBooking(id, b.getRoom(),
					getWeekday(b.getWeekday()),
					LocalTime.of(b.getStartTime().getHour(), b.getStartTime().getMinute()),
					LocalTime.of(b.getEndTime().getHour(), b.getEndTime().getMinute()),
					addCurriculum(id, curriculumMap)));
		}
		
		return classBookings;
	}
	
	
	private Set<String> addCurriculum(String courseID, 
		Map<String, List<String>> curriculumMap) {
		
		return curriculumMap.entrySet().stream()
			.filter(degree -> degree.getValue().contains(courseID)).map(Entry::getKey)
			.collect(Collectors.toSet());
	}
	

	private DayOfWeek getWeekday(String weekday) {
		
		if ("Thur".equals(weekday)) {
			
			return DayOfWeek.THURSDAY;
		}
		
		return DayOfWeek.from(WEEKDAYFORMATTER.parse(weekday));
	}
}
