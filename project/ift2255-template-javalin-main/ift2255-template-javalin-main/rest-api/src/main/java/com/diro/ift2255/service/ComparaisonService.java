package com.diro.ift2255.service;

import java.util.Optional;

import com.diro.ift2255.model.Comparaison;
import com.diro.ift2255.model.ComparaisonResult;
import com.diro.ift2255.model.Course;

/**
 * Service dédié à la comparaison de cours.
 */
public class ComparaisonService {

    private final CourseService courseService;

    public ComparaisonService(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Compare deux cours selon les critères définis (crédits, avis, sessions, prérequis).
     * @param courseIdA ID du premier cours
     * @param courseIdB ID du second cours
     * @return Résultat de la comparaison
     */
    public ComparaisonResult compareCourses(String courseIdA, String courseIdB) {
        Optional<Course> courseAOpt = courseService.getCourseById(courseIdA);
        Optional<Course> courseBOpt = courseService.getCourseById(courseIdB);

        if (courseAOpt.isEmpty() || courseBOpt.isEmpty()) {
            throw new IllegalArgumentException("Un ou les deux cours sont introuvables.");
        }

        Course courseA = courseAOpt.get();
        Course courseB = courseBOpt.get();

        Comparaison comparaison = new Comparaison(courseA, courseB);
        return comparaison.buildResult();
    }

    /**
     * Vérifie que les deux cours existent.
     */
    public boolean validateCourses(String courseIdA, String courseIdB) {
        return courseService.getCourseById(courseIdA).isPresent()
            && courseService.getCourseById(courseIdB).isPresent();
    }
}
