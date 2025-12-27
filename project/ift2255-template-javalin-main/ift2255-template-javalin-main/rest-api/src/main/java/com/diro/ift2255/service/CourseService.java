package com.diro.ift2255.service;

import com.diro.ift2255.model.Comparaison;
import com.diro.ift2255.model.ComparaisonResult;
import com.diro.ift2255.model.Course;
import com.diro.ift2255.util.HttpClientApi;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URI;
import java.util.*;

public class CourseService {
    private final HttpClientApi clientApi;
    private static final String BASE_URL = "https://planifium-api.onrender.com/api/v1/courses";

    public CourseService(HttpClientApi clientApi) {
        this.clientApi = clientApi;
    }

    public List<Course> getAllCourses(Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;

        URI uri = HttpClientApi.buildUri(BASE_URL, params);
        List<Course> courses = clientApi.get(uri, new TypeReference<List<Course>>() {});

        return courses;
    }

    public Optional<Course> getCourseById(String courseId) {
        return getCourseById(courseId, null);
    }

    public Optional<Course> getCourseById(String courseId, Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;
        URI uri = HttpClientApi.buildUri(BASE_URL + "/" + courseId, params);

        try {
            Course course = clientApi.get(uri, Course.class);
            return Optional.of(course);
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public ComparaisonResult compareCourses(String courseIdA, String courseIdB) {
        Optional<Course> courseAOpt = getCourseById(courseIdA);
        Optional<Course> courseBOpt = getCourseById(courseIdB);

        if (courseAOpt.isEmpty() || courseBOpt.isEmpty()) {
            throw new IllegalArgumentException("Un ou les deux cours sont introuvables.");
        }

        Course courseA = courseAOpt.get();
        Course courseB = courseBOpt.get();

        Comparaison comparaison = new Comparaison(courseA, courseB);
        return comparaison.buildResult();
    }

    public boolean validateCourses(String courseIdA, String courseIdB) {
        return getCourseById(courseIdA).isPresent()
            && getCourseById(courseIdB).isPresent();
    }

    public List<Course> searchCourses(String sigle, String keyword) {

        Map<String, String> params = new HashMap<>();

        // Recherche par keywors
        if (keyword != null && !keyword.isBlank()) {
            params.put("name", keyword.toLowerCase());
        }


        URI uri = HttpClientApi.buildUri(BASE_URL, params);
        List<Course> courses = clientApi.get(uri, new TypeReference<List<Course>>() {});

        // filtrage ici
        if (sigle != null && !sigle.isBlank()) {
            String sigleUpper = sigle.toUpperCase();
            courses = courses.stream()
                    .filter(c -> c.getId() != null && c.getId().startsWith(sigleUpper))
                    .toList();
        }

        return courses;
    }
}
