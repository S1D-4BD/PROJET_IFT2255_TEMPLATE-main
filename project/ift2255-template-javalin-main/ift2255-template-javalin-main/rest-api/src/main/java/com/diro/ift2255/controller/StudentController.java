package com.diro.ift2255.controller;

import io.javalin.http.Context;
import com.diro.ift2255.service.StudentService;
import java.util.Map;
import java.util.List; //pr eligibilite

public class StudentController {
    
    private StudentService service = new StudentService();

    public void getAll(Context ctx) {
        ctx.json(service.getAll());
    }

    public void getByMatricule(Context ctx) {
        String m = ctx.pathParam("matricule");
        var student = service.getByMatricule(m);
        if (student.isPresent()) {
            ctx.json(student.get());
        } else {
            ctx.status(404).json(Map.of("error", "Not found"));
        }
    }

    public void checkEligibility(Context ctx) {
        String matricule = ctx.pathParam("matricule");
        String courseId = ctx.queryParam("courseId");

        if (courseId == null || courseId.isBlank()) {
            ctx.status(400).json(Map.of("error", "Le paramètre courseId est requis"));
            return;
        }

        var student = service.getByMatricule(matricule);
        if (student.isEmpty()) {
            ctx.status(404).json(Map.of("error", "Étudiant non trouvé"));
            return;
        }

        try {
            String url = "https://planifium-api.onrender.com/api/v1/courses/" + courseId;
            var response = new com.diro.ift2255.util.HttpClientApi().get(java.net.URI.create(url));
            Map<String, Object> courseData = new com.fasterxml.jackson.databind.ObjectMapper().readValue(response.getBody(), Map.class);

            //List<String> prerequis = (List<String>) courseData.get("prerequisites");
            List<String> prerequis = (List<String>) courseData.get("prerequisite_courses");
            //boolean eligible = service.isEligible(student.get(), prerequis);
            boolean eligible = service.isEligible(student.get(), prerequis, courseId); //pr les cyles
            ctx.json(Map.of(
                "eligible", eligible,
                "matricule", matricule,
                "courseId", courseId,
                "completedCourses", student.get().getCompletedCourses(),
                "prerequis", prerequis != null ? prerequis : List.of()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", e.getMessage()));
        }
    }
}