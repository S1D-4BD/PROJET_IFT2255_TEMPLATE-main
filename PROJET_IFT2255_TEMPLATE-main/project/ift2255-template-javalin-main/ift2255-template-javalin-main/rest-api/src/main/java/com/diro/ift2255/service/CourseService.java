package com.diro.ift2255.service;

import com.diro.ift2255.model.Comparaison;
import com.diro.ift2255.model.ComparaisonResult;
import com.diro.ift2255.model.Course;
import com.diro.ift2255.util.HttpClientApi;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URI;
import java.util.*;

/**
 * Service responsable de la gestion et de la récupération des données liées aux cours
*/
public class CourseService {
    private final HttpClientApi clientApi;
    private static final String BASE_URL = "https://planifium-api.onrender.com/api/v1/courses";

    public CourseService(HttpClientApi clientApi) {
        this.clientApi = clientApi;
    }

    /**
     * Récupère tous les cours, avec possibilité d'appliquer des paramètres de filtrage.
     */
    public List<Course> getAllCourses(Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;

        URI uri = HttpClientApi.buildUri(BASE_URL, params);
        List<Course> courses = clientApi.get(uri, new TypeReference<List<Course>>() {});

        return courses;
    }

    /**
     * Récupère un cours selon son identifiant.
     */
    public Optional<Course> getCourseById(String courseId) {
        return getCourseById(courseId, null);
    }

    /**
     * Récupère un cours selon son identifiant, avec paramètres supplémentaires.
     */
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

    /**
     * Compare deux cours selon plusieurs critères (crédits, avis, sessions..)
     */
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

    /**
     * Vérifie si deux cours existent.
     * @param courseIdA
     * @param courseIdB
     * @return
     */
    public boolean validateCourses(String courseIdA, String courseIdB) {
        return getCourseById(courseIdA).isPresent()
            && getCourseById(courseIdB).isPresent();
    }

    /**
     * Recherche des cours selon un sigle ou un mot-clé.
     */
    public List<Course> searchCourses(String sigle, String keyword) {

        // Récupération de tous les cours depuis l'API
        Map<String, String> params = new HashMap<>();
        URI uri = HttpClientApi.buildUri(BASE_URL, params);
        List<Course> courses = clientApi.get(uri, new TypeReference<List<Course>>() {});

        // Filtrage par mot-clé (titre ou description)
        if (keyword != null && !keyword.isBlank()) {
            String lowerKeyword = keyword.toLowerCase();
            courses = courses.stream()
                    .filter(c -> (c.getName() != null && c.getName().toLowerCase().contains(lowerKeyword))
                            || (c.getDescription() != null && c.getDescription().toLowerCase().contains(lowerKeyword)))
                    .toList();
        }

        // Filtrage par sigle
        if (sigle != null && !sigle.isBlank()) {
            String sigleUpper = sigle.toUpperCase();
            courses = courses.stream()
                    .filter(c -> c.getId() != null && c.getId().startsWith(sigleUpper))
                    .toList();
        }

        return courses;
    }

    /**
     * Récupère les cours associés à un programme donné.
     */
    public List<Course> getCoursesByProgram(String programCode, boolean includeDetails) {
        if (programCode == null || programCode.isBlank()) {
            return List.of();
        }

        String url = "https://planifium-api.onrender.com/api/v1/programs?programs_list=" + programCode;
        if (includeDetails) {
            url += "&include_courses_detail=true";
        }

        try {
            Map<String, Object> response = clientApi.get(URI.create(url), new TypeReference<Map<String, Object>>() {});
            if (response.containsKey("courses")) {
            
                List<Course> courses = new ArrayList<>();
                List<Map<String, Object>> rawCourses = (List<Map<String, Object>>) response.get("courses");
                for (Map<String, Object> c : rawCourses) {
                    Course course = new Course();

                    course.setId((String) c.get("id")); 
                    course.setName((String) c.get("name"));
                    course.setDescription((String) c.get("description"));
                    course.setPrerequisites((List<String>) c.getOrDefault("prerequisites", List.of()));
                    courses.add(course);
                }
                return courses;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
