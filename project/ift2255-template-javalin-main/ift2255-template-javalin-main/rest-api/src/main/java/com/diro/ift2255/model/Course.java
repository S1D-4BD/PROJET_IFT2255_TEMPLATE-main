package com.diro.ift2255.model;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {
    private String id;
    private String name;
    private String description;
    private List<String> prerequis;
    private String credits;
    private String teacher;

    public Course() {}

    public Course(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.description = desc;
    }

    public List<String> getPrerequisites() {return prerequis; }
    public void setPrerequisites(List<String> p) {  this.prerequis = p; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String email) { this.description = email; }

    public String getCredits(){
        return credits; //??? dafk
    }
}
