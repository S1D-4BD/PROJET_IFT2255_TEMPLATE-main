

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

 ////////////// pr comments now en test
import com.diro.ift2255.controller.AvisController;
///
import com.diro.ift2255.controller.CommentController;
import com.diro.ift2255.controller.ComparaisonController;
import com.diro.ift2255.controller.CourseController;
import com.diro.ift2255.controller.StudentController;
import com.diro.ift2255.service.CourseService;
 ////////////// pr comments now en test
import com.diro.ift2255.util.HttpClientApi;

///
import io.javalin.Javalin;

public class Routes {

    private static Map<String, List<Map<String, String>>> comments = new HashMap<>();

    public static void register(Javalin app) {
        registerStudentRoutes(app);
        registerCourseRoutes(app);
        registerCommentRoutes(app);
        registerAvisRoutes(app);
        registerComparaisonRoutes(app);
        //maybe ajouter le truc avec les tgde / prof??

    }

    private static void registerStudentRoutes(Javalin app) {
        StudentController studentController = new StudentController();
        app.get("/students", studentController::getAll);
        app.get("/students/{matricule}", studentController::getByMatricule);
        ////////////////////////////////////////////////////////////////////////////////////
        app.get("/students/{matricule}/eligibility", studentController::checkEligibility); /////
    }



    private static void registerCourseRoutes(Javalin app) {
        CourseService courseService = new CourseService(new HttpClientApi());
        CourseController courseController = new CourseController(courseService);

        app.get("/courses/{id}/full", courseController::getCourseWithSchedule);
        app.get("/courses/{id}/schedule", courseController::getCourseScheduleOnly);
        app.get("/courses", courseController::getAllCourses);
        app.get("/courses/search", courseController::searchCourses);
        app.get("/courses/program", courseController::getCoursesByProgram);
        app.get("/courses/{id}", courseController::getCourseById);
        
    }

    private static void registerCommentRoutes(Javalin app) {
        CommentController commentController = new CommentController();

        app.get("/comments/{courseId}", commentController::getByCourse);
        app.post("/comments", commentController::create);
    }

    private static void registerAvisRoutes(Javalin app) {
        AvisController avisController = new AvisController();

        app.get("/avis", avisController::getAll);
        app.get("/avis/{courseId}", avisController::getByCourse);
        app.get("/avis/{courseId}/stats", avisController::getStats);
        app.post("/avis", avisController::create);
    }

    private static void registerComparaisonRoutes(Javalin app) {
        CourseService courseService = new CourseService(new HttpClientApi());
        ComparaisonController comparaisonController = new ComparaisonController(courseService);

        app.get("/compare", comparaisonController::compareCourses);
    }
}