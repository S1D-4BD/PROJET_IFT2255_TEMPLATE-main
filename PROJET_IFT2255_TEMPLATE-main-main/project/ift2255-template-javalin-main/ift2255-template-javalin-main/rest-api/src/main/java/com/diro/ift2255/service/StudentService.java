package com.diro.ift2255.service;
import com.diro.ift2255.model.Student;

import java.util.*;

public class StudentService {
    private Map<String, Student> students = new HashMap<>();

    public StudentService() {
        students.put("20280089", new Student("20280089", "Aya Dair", "Informatique","baccaleauréat",Arrays.asList("IFT1015","IFT1005","IFT1025","IFT1065","IFT1575","IFT2015","IFT2115","IFT2905","MAT1600","MAT1400","MAT1978","IFT3355")));
        //students.put("20283304", new Student("20283304", "Gabriel Viana", "Info", new ArrayList<>()));
        students.put("20283304", new Student("20283304", "Gabriel Viana", "Informatique", "baccaleauréat",Arrays.asList("IFT1015","IFT1005","IFT1025","IFT1065","IFT1575","IFT2015","IFT2115","IFT2905","MAT1600","MAT1400","MAT1978","IFT2035","IFT1227")));
        students.put("20279666", new Student("20279666", "Celina Sid Abdelkader", "Mathématiques-Informatique", "Baccaleauréat",Arrays.asList("IFT1005","IFT1015","IFT1005","IFT1025","IFT1065","IFT1575","IFT2015","IFT2115","IFT2905","MAT1600","MAT1400","MAT1978","IFT2035","IFT1227","IFT370","IFT3225")));
        students.put("12345678", new Student("12345678", "test Testy", "Mathématiques-Informatique", "baccaleauréat",Arrays.asList("IFT3225")));
        }

    public Optional<Student> getByMatricule(String m) {
        return Optional.ofNullable(students.get(m));
    }

    public List<Student> getAll() {
        return new ArrayList<>(students.values());
    }

    public boolean isEligible(Student s, List<String> prereq, String courseId) {
        char premierchiffre = courseId.charAt(3);
        if ((premierchiffre == '6' || premierchiffre == '7') && s.getCycle().toLowerCase().contains("bac") ) {
            return false;
        }
        if (prereq == null || prereq.isEmpty()) return true;
        return s.getCompletedCourses().containsAll(prereq);
    }

}