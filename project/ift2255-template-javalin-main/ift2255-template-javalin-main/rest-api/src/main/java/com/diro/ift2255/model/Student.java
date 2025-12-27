package com.diro.ift2255.model;

import java.util.List;

public class Student {
    private String matricule;
    private String name;
    private String program;
    private String cycle; //sinn on va struggle pr l eligibilte
    private List<String> completedCourses;

    public Student() {}

    public Student(String matricule, String name, String program,String cycle ,List<String> completedCourses) {
        this.matricule = matricule;
        this.name = name;
        this.program = program;
        this.cycle = cycle;
        this.completedCourses = completedCourses;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getName() {
        return name;
    }
    q
    public void setName(String name) {
        this.name = name;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public List<String> getCompletedCourses() {
        return completedCourses;
    }
    public void setCompletedCourses(List<String> completedCourses) {
        this.completedCourses = completedCourses;
    }
}