package com.diro.ift2255.service;

import com.diro.ift2255.model.Student;
import java.util.*;

public class StudentService {
    private Map<String, Student> students = new HashMap<>();

    public StudentService() {
        students.put("20280089", new Student("20280089", "Aya Dair", "Informatique", Arrays.asList("IFT1015","IFT1025","ift1065","IFT1575")));
        students.put("20283304", new Student("20283304", "Gabriel Viana", "Info", new ArrayList<>()));
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