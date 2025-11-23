package com.diro.ift2255.model;

import java.util.List;

public class Student {
    private String matricule;
    private String name;
    private String program;
    private List<String> completedCourses;

    public Student() {}

    public Student(String matricule, String name, String program, List<String> completedCourses) {
        this.matricule = matricule;
        this.name = name;
        this.program = program;
        this.completedCourses = completedCourses;
    }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public List<String> getCompletedCourses() { return completedCourses; }
    public void setCompletedCourses(List<String> completedCourses) { this.completedCourses = completedCourses; }
}