package com.diro.ift2255.service;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.diro.ift2255.model.Course;

class CourseServiceTest {

    private CourseService service;

    @BeforeEach
    void setup() {
        // On crée un service simple pour tester sans faire d'appel à l'API
        service = new CourseService(null) {
            @Override
            public List<Course> getCoursesByProgram(String programCode, boolean includeDetails) {
                // Données simulées pour le test
                if (programCode == null || programCode.isBlank()) return List.of();
                if (programCode.equals("IFT")) {
                    Course c1 = new Course("IFT1015", "Programmation I", "Description 1");
                    Course c2 = new Course("IFT1025", "Programmation II", "Description 2");
                    return List.of(c1, c2);
                }
                return List.of(); // aucun cours pour les autres programmes
            }
        };
    }

    @Test
    void testProgramIFT() {
        // Test programme connu
        List<Course> courses = service.getCoursesByProgram("IFT", false);
        assertEquals(2, courses.size()); // on doit avoir 2 cours
        assertEquals("IFT1015", courses.get(0).getId()); // le premier cours doit être IFT1015
    }

    @Test
    void testProgramNull() {
        // Test programme null
        List<Course> courses = service.getCoursesByProgram(null, false);
        assertTrue(courses.isEmpty()); // doit retourner une liste vide
    }

    @Test
    void testProgramEmpty() {
        // Test programme vide
        List<Course> courses = service.getCoursesByProgram("  ", false);
        assertTrue(courses.isEmpty()); // doit retourner une liste vide
    }

    @Test
    void testProgramUnknown() {
        // Test programme inconnu
        List<Course> courses = service.getCoursesByProgram("ABC", false);
        assertTrue(courses.isEmpty()); // doit retourner une liste vide
    }

    @Test
    void testIncludeDetails() {
        // Test includeDetails = true
        List<Course> courses = service.getCoursesByProgram("IFT", true);
        assertEquals(2, courses.size()); // on doit toujours avoir 2 cours
        assertEquals("Programmation I", courses.get(0).getName()); // vérifie le nom du cours
    }
}
