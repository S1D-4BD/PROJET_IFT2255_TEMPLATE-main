

////////////////////////////////////
/*
package com.diro.ift2255.config;

import com.diro.ift2255.controller.CourseController;
import com.diro.ift2255.controller.StudentController;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.util.HttpClientApi;
import io.javalin.Javalin;

public class Routes {

    public static void register(Javalin app) {
        registerStudentRoutes(app);
        registerCourseRoutes(app);
    }

    private static void registerStudentRoutes(Javalin app) {
        StudentController studentController = new StudentController();

        app.get("/students", studentController::getAll);
        app.get("/students/{matricule}", studentController::getByMatricule);
        //app.get("/teachers/{numemploye}", teacherController::getOneTeacher)
    }

    private static void registerCourseRoutes(Javalin app) {
        CourseService courseService = new CourseService(new HttpClientApi());
        CourseController courseController = new CourseController(courseService);

        app.get("/courses", courseController::getAllCourses);
        app.get("/courses/{id}", courseController::getCourseById);
    }
}
*/
package com.diro.ift2255.config;

import com.diro.ift2255.controller.CourseController;
import com.diro.ift2255.controller.StudentController;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.util.HttpClientApi;
import io.javalin.Javalin;

import java.util.Map;
import java.util.*;

public class Routes {

    private static Map<String, List<Map<String, String>>> comments = new HashMap<>();

    public static void register(Javalin app) {
        registerStudentRoutes(app);
        registerCourseRoutes(app);
        registerCommentRoutes(app);
        //maybe ajouter le truc avec les tgde / prof??
    }

    private static void registerStudentRoutes(Javalin app) {
        StudentController studentController = new StudentController();
        app.get("/students", studentController::getAll);
        app.get("/students/{matricule}", studentController::getByMatricule);
    }

    private static void registerCourseRoutes(Javalin app) {
        CourseService courseService = new CourseService(new HttpClientApi());
        CourseController courseController = new CourseController(courseService);


        app.get("/courses/{id}/full", courseController::getCourseWithSchedule);
        app.get("/courses", courseController::getAllCourses);

/*
        app.get("/courses/{id}", ctx -> {
            String id = ctx.pathParam("id");
            var course = courseService.getCourseById(id);

            if (course.isPresent()) {
                var c = course.get();

                Map<String, Object> response = new HashMap<>();
                response.put("id", c.getId());
                response.put("name", c.getName());
                response.put("description", c.getDescription());
                response.put("prerequis", c.getPrerequisites());


                String courseId = id.toUpperCase();
                List<Map<String, String>> courseComments = comments.getOrDefault(courseId, new ArrayList<>());
                response.put("comments", courseComments);

                ctx.json(response);
            } else {
                ctx.status(404).json(Map.of("error", "Cours non trouvé"));
            }
        });*/
        app.get("/courses/{id}", ctx -> {
            String id = ctx.pathParam("id");  //get le param id du url
            var course = courseService.getCourseById(id);

            if (course.isPresent()) {
                var c = course.get();

                Map<String, Object> response = new HashMap<>();
                response.put("id", c.getId());
                response.put("name", c.getName());
                response.put("description", c.getDescription());
                response.put("prerequis", c.getPrerequisites());
                response.put("credits", c.getCredits());

                //ca ct juste pr le prof omfg
                try {
                    String url = "https://planifium-api.onrender.com/api/v1/courses/" + id + "?include_schedule=true&schedule_semester=A25";
                    var apiResponse = new HttpClientApi().get(java.net.URI.create(url));
                    Map data = new com.fasterxml.jackson.databind.ObjectMapper().readValue(apiResponse.getBody(), Map.class);
                    response.put("schedules", data.get("schedules"));
                    response.put("prerequisite_courses", data.get("prerequisite_courses"));
                } catch (Exception e) {
                    response.put("schedules", null);
                    response.put("prerequisite_courses", null);
                }
                String courseId = id.toUpperCase();
                List<Map<String, String>> courseComments = comments.getOrDefault(courseId, new ArrayList<>());
                response.put("comments", courseComments);

                ctx.json(response);
            } else {
                ctx.status(404).json(Map.of("error", "Cours non trouvé"));
            }
        });
    }

    private static void registerCommentRoutes(Javalin app) {

        app.post("/comments", ctx -> {
            Map<String, String> comment = ctx.bodyAsClass(Map.class);
            String courseId = comment.get("courseId").toUpperCase();

            comments.putIfAbsent(courseId, new ArrayList<>());
            comments.get(courseId).add(comment);

            ctx.status(201).json(Map.of("status", "ok", "courseId", courseId));
        });


        app.get("/comments/{courseId}", ctx -> {
            String courseId = ctx.pathParam("courseId").toUpperCase();
            List<Map<String, String>> courseComments = comments.getOrDefault(courseId, new ArrayList<>());
            ctx.json(courseComments);
        });
    }
}