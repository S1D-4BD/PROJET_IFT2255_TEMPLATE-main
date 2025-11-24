package com.diro.ift2255.service;

import com.diro.ift2255.model.Student;
import java.util.*;

public class StudentService {
    private Map<String, Student> students = new HashMap<>();

    public StudentService() {
        students.put("20280089", new Student("20280089", "Aya Dair", "Informatique", Arrays.asList("IFT1015","IFT1005","IFT1025","IFT1065","IFT1575","IFT2015","IFT2115","IFT2905","MAT1600","MAT1400","MAT1978","IFT3355")));
        //students.put("20283304", new Student("20283304", "Gabriel Viana", "Info", new ArrayList<>()));
        students.put("20283304", new Student("20283304", "Gabriel Viana", "Info", Arrays.asList("IFT1015","IFT1005","IFT1025","IFT1065","IFT1575","IFT2015","IFT2115","IFT2905","MAT1600","MAT1400","MAT1978","IFT2035","IFT1227")));
        students.put("20279666", new Student("20279666", "Celina Sid Abdelkader", "Informatique", Arrays.asList("IFT1005","IFT1015","IFT1005","IFT1025","IFT1065","IFT1575","IFT2015","IFT2115","IFT2905","MAT1600","MAT1400","MAT1978","IFT2035","IFT1227","IFT370","IFT3225")));

    }

    public Optional<Student> getByMatricule(String m) {
        return Optional.ofNullable(students.get(m));
    }

    public List<Student> getAll() {
        return new ArrayList<>(students.values());
    }

    public boolean isEligible(Student s, List<String> prereq) {
        if (prereq == null || prereq.isEmpty()) return true;
        return s.getCompletedCourses().containsAll(prereq);
    }
}