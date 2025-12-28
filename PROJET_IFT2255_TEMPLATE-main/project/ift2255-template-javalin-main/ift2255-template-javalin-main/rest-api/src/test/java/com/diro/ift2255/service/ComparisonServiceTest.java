package com.diro.ift2255.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.diro.ift2255.model.ComparaisonResult;
import com.diro.ift2255.model.Course;

@ExtendWith(MockitoExtension.class)
class ComparaisonServiceTest {

    @Mock
    private CourseService courseService;

    private ComparaisonService comparaisonService;

    @BeforeEach
    void setUp() {
        comparaisonService = new ComparaisonService(courseService);
    }

    @Test
    void testeCompareCourses_whenBothCoursesExist() {
        String idA = "IFT1015";
        String idB = "IFT1025";

        Course courseA = mock(Course.class);
        Course courseB = mock(Course.class);



/////////SINN ON USE LE MOCK C PAS UN VRAI OBJET ON transmet le id sigle
        when(courseA.getId()).thenReturn(idA);

        when(courseB.getId()).thenReturn(idB);

        when(courseService.getCourseById(idA)).thenReturn(Optional.of(courseA));
        when(courseService.getCourseById(idB)).thenReturn(Optional.of(courseB));

        ComparaisonResult result = comparaisonService.compareCourses(idA, idB);

        assertNotNull(result, "Le resultat de comparaison ne devrait pas etre null");
        verify(courseService).getCourseById(idA);
        verify(courseService).getCourseById(idB);
    }

    @Test
    void testeCompareCourses_whenOneCourseDontExist() {
        String idA = "IFT1015";
        String idB = "IFT9999";

        when(courseService.getCourseById(idA)).thenReturn(Optional.of(mock(Course.class)));
        when(courseService.getCourseById(idB)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> comparaisonService.compareCourses(idA, idB),
                "On s'attend a une exception si un des cours est introuvable"
        );

        assertTrue(ex.getMessage().contains("introuvables"));

        verify(courseService).getCourseById(idA);
        verify(courseService).getCourseById(idB);
    }

    @Test
    void testValidateCourses_whenBothCoursesExist() {
        when(courseService.getCourseById("A")).thenReturn(Optional.of(mock(Course.class)));
        when(courseService.getCourseById("B")).thenReturn(Optional.of(mock(Course.class)));

        boolean result = comparaisonService.validateCourses("A", "B");

        assertTrue(result, "Les deux cours existent, on s'attend a true");
    }

    @Test
    void testCompareCourses_whenBothCoursesDontExist() {
        when(courseService.getCourseById("A")).thenReturn(Optional.empty());
        when(courseService.getCourseById("B")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> comparaisonService.compareCourses("A", "B")
        );

        assertTrue(ex.getMessage().contains("introuvables"));

        verify(courseService).getCourseById("A");
        verify(courseService).getCourseById("B");
    }

    @Test
    void testValidateCourses_whenOneCourseDoesNotExist() {
        when(courseService.getCourseById("A")).thenReturn(Optional.of(mock(Course.class)));
        when(courseService.getCourseById("B")).thenReturn(Optional.empty());

        boolean result = comparaisonService.validateCourses("A", "B");

        assertFalse(result);

        verify(courseService).getCourseById("A");
        verify(courseService).getCourseById("B");
    }
}