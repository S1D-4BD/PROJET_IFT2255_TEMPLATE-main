package com.diro.ift2255.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.util.HttpClientApi;
import com.diro.ift2255.util.ResponseUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.Context;

public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    public void getAllCourses(Context ctx) {
        Map<String, String> queryParams = extractQueryParams(ctx);

        List<Course> courses = service.getAllCourses(queryParams);
        ctx.json(courses);
    }

    public void getCourseById(Context ctx) {
        String id = ctx.pathParam("id");

        if (!validateCourseId(id)) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre id n'est pas valide."));
            return;
        }

        Optional<Course> course = service.getCourseById(id);
        if (course.isEmpty()) {
            ctx.status(404).json(ResponseUtil.formatError("Aucun cours ne correspond à l'ID: " + id));
            return;
        }

        var c = course.get();
        Map<String, Object> response = new HashMap<>();
        response.put("id", c.getId());
        response.put("name", c.getName());
        response.put("description", c.getDescription());
        response.put("prerequis", c.getPrerequisites());
        response.put("credits", c.getCredits());

        try {
            String semester = ctx.queryParam("schedule_semester");
            if (semester == null || semester.isBlank()) {
                semester = ctx.queryParam("semester");
            }
            if (semester == null || semester.isBlank()) {
                semester = "A25";
            }

            String url = "https://planifium-api.onrender.com/api/v1/courses/" + id
                    + "?include_schedule=true&schedule_semester=" + semester.toLowerCase();

            var apiResponse = new HttpClientApi().get(java.net.URI.create(url));
            Map datarecue = new com.fasterxml.jackson.databind.ObjectMapper().readValue(apiResponse.getBody(), Map.class);

            response.put("schedules", datarecue.get("schedules"));
            response.put("prerequisite_courses", datarecue.get("prerequisite_courses"));
        } catch (Exception e) {
            response.put("schedules", null);
            response.put("prerequisite_courses", null);
        }

        ctx.json(response);
    }

    public void getCourseWithSchedule(Context ctx) {
        String id = ctx.pathParam("id");
        String semester = ctx.queryParam("schedule_semester");

        if (semester == null || semester.isBlank()) {
            semester = ctx.queryParam("semester");
        }
        if (semester == null || semester.isBlank()) {
            semester = "A25"; //valeur par defaut
        }

        try {
            String url = "https://planifium-api.onrender.com/api/v1/courses/" + id+ "?include_schedule=true&schedule_semester=" + semester.toLowerCase();
            var response = new HttpClientApi().get(java.net.URI.create(url));
            ctx.contentType("application/json").result(response.getBody());
        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", e.getMessage()));
        }
    }

    public void getCourseFull(Context ctx) {
        String id = ctx.pathParam("id");


        String semester = ctx.queryParam("schedule_semester");
        if (semester == null || semester.isBlank()) {
            semester = ctx.queryParam("semester");
        }
        if (semester == null || semester.isBlank()) {
            semester = "A25";
        }

        try {
            String url = "https://planifium-api.onrender.com/api/v1/courses/" + id+ "?include_schedule=true&schedule_semester=" + semester.toLowerCase();
            var response = new HttpClientApi().get(java.net.URI.create(url));

            ctx.contentType("application/json");
            ctx.result(response.getBody());
        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", e.getMessage()));
        }
    }

    public void getCourseScheduleOnly(Context ctx) {
        String id = ctx.pathParam("id");


        String semester = ctx.queryParam("schedule_semester");
        if (semester == null || semester.isBlank()) {
            semester = ctx.queryParam("semester");
        }
        if (semester == null || semester.isBlank()) {
            semester = "A25";
        }

        try {
            String url = "https://planifium-api.onrender.com/api/v1/courses/" + id
                       + "?include_schedule=true&schedule_semester=" + semester.toLowerCase();

            var response = new HttpClientApi().get(java.net.URI.create(url));


            // la ref https://jenkov.com/tutorials/java-json/jackson-jsonnode.html
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            JsonNode schedulesNode = rootNode.get("schedules");
            if (schedulesNode != null) {
                ctx.json(schedulesNode);
            } else {
                ctx.json(Map.of("schedules", List.of()));
            }
        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", e.getMessage()));
        }
    }

    private boolean validateCourseId(String courseId) {
        return courseId != null && courseId.trim().length() >= 6;
    }

    private Map<String, String> extractQueryParams(Context ctx) {
        Map<String, String> queryParams = new HashMap<>();

        ctx.queryParamMap().forEach((key, values) -> {
            if (!values.isEmpty()) {
                queryParams.put(key, values.get(0));
            }
        });

        return queryParams;
    }

    public void searchCourses(Context ctx) {

        String sigle = ctx.queryParam("sigle");
        String keyword = ctx.queryParam("keyword");

        List<Course> courses = service.searchCourses(sigle, keyword);

        if (courses.isEmpty()) {
            ctx.status(404).json(Map.of("error", "Cours non trouvé"));
        } else {
            ctx.json(courses);
        }
    }
}
