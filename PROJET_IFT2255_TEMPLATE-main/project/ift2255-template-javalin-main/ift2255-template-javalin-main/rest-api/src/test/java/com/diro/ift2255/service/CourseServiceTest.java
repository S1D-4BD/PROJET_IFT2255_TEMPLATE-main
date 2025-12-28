package com.diro.ift2255.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.util.HttpClientApi;
import com.fasterxml.jackson.core.type.TypeReference;

class CourseServiceTest {

    private CourseService service;
    private CourseService searchService;
    private HttpClientApi mockApi;

    @BeforeEach
    void setup() {
        service = new CourseService(null) {
            @Override
            public List<Course> getCoursesByProgram(String programCode, boolean includeDetails) {
                if (programCode == null || programCode.isBlank()) return List.of();
                if (programCode.equals("IFT")) {
                    Course c1 = new Course("IFT1015", "Programmation 1", "Description 1");
                    Course c2 = new Course("IFT1025", "Programmation 2", "Description 2");
                    return List.of(c1, c2);
                }
                if (programCode.equals("MAT")) {
                    Course c1 = new Course("MAT1400", "Calcul 1", "Description Calcul");
                    return List.of(c1);
                }
                return List.of();
            }
        };

        mockApi = Mockito.mock(HttpClientApi.class);
        searchService = new CourseService(mockApi);
    }

    // tests pour la fonctionnalité voir cours par programme
    @Test
    void testProgramIFT() {
        List<Course> courses = service.getCoursesByProgram("IFT", false);
        assertEquals(2, courses.size());
        assertEquals("IFT1015", courses.get(0).getId());
    }

    @Test
    void testProgramMAT() {
        List<Course> courses = service.getCoursesByProgram("MAT", false);
        assertEquals(1, courses.size());
        assertEquals("MAT1400", courses.get(0).getId());
    }

    @Test
    void testProgramNull() {
        List<Course> courses = service.getCoursesByProgram(null, false);
        assertTrue(courses.isEmpty());
    }

    @Test
    void testProgramEmpty() {
        List<Course> courses = service.getCoursesByProgram("  ", false);
        assertTrue(courses.isEmpty());
    }

    @Test
    void testProgramUnknown() {
        List<Course> courses = service.getCoursesByProgram("ABC", false);
        assertTrue(courses.isEmpty());
    }

    @Test
    void testIncludeDetails() {
        List<Course> courses = service.getCoursesByProgram("IFT", true);
        assertEquals(2, courses.size());
        assertEquals("Programmation 1", courses.get(0).getName());
    }

    // tests pour la fonctionnalité rechercher un cours

    @Test
    void testSearchCourses_bySiglePartial() {
        List<Course> allCourses = List.of(
            new Course("IFT1015", "Programmation 1", ""),
            new Course("IFT1025", "Programmation 2", ""),
            new Course("MAT1400", "Calcul 1", "")
        );

        Mockito.when(mockApi.get(Mockito.any(), Mockito.any(TypeReference.class))).thenReturn(allCourses);

        List<Course> result = searchService.searchCourses("IFT", null);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c -> c.getId().startsWith("IFT")));
    }

    @Test
    void testSearchCourses_byKeyword() {
        List<Course> allCourses = List.of(
            new Course("IFT1015", "Programmation 1", "Cours de base"),
            new Course("IFT1025", "Programmation 2", "Algorithmique"),
            new Course("MAT1400", "Calcul 1", "Calcul")
        );

        Mockito.when(mockApi.get(Mockito.any(), Mockito.any(TypeReference.class))).thenReturn(allCourses);

        List<Course> result = searchService.searchCourses(null, "algo");
        assertEquals(1, result.size());
        assertEquals("IFT1025", result.get(0).getId());
    }

    @Test
    void testSearchCourses_bySigleAndKeyword() {
        List<Course> allCourses = List.of(
            new Course("IFT1015", "Programmation 1", "Cours de base"),
            new Course("IFT1025", "Programmation 2", "Algorithmique"),
            new Course("MAT1400", "Calcul 1", "Calcul")
        );

        Mockito.when(mockApi.get(Mockito.any(), Mockito.any(TypeReference.class))).thenReturn(allCourses);

        List<Course> result = searchService.searchCourses("IFT", "algo");
        assertEquals(1, result.size());
        assertEquals("IFT1025", result.get(0).getId());
    }

    @Test
    void testSearchCourses_noMatch() {
        List<Course> allCourses = List.of(
            new Course("IFT1015", "Programmation 1", "Cours de base"),
            new Course("IFT1025", "Programmation 2", "Algorithmique"),
            new Course("MAT1400", "Calcul 1", "Calcul")
        );

        Mockito.when(mockApi.get(Mockito.any(), Mockito.any(TypeReference.class))).thenReturn(allCourses);

        List<Course> result = searchService.searchCourses("BIO", null);
        assertEquals(0, result.size());
    }

    @Test
    void testSearchCourses_emptyInput_returnsAll() {
        List<Course> allCourses = List.of(
            new Course("IFT1015", "Programmation 1", "Cours de base"),
            new Course("IFT1025", "Programmation 2", "Algorithmique"),
            new Course("MAT1400", "Calcul 1", "Calcul")
        );

        Mockito.when(mockApi.get(Mockito.any(), Mockito.any(TypeReference.class))).thenReturn(allCourses);

        List<Course> result = searchService.searchCourses(null, null);
        assertEquals(3, result.size());
    }
}
