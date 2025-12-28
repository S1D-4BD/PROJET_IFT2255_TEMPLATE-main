package com.diro.ift2255.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.service.CourseService;

import io.javalin.http.Context;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    private CourseService mockService;

    @Mock
    private Context mockContext;

    private CourseController controller;

    private long testStartTime;

    @BeforeAll
    static void printHeader() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("CourseController Tests");
        System.out.println("=".repeat(80));
    }

    @BeforeEach
    void setup(TestInfo testInfo) {
        controller = new CourseController(mockService);
        testStartTime = System.currentTimeMillis();

        System.out.println("\nTEST: " + testInfo.getDisplayName());
        System.out.println("    Method: " + testInfo.getTestMethod().get().getName());
        System.out.println("    Assertions:");
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        long duration = System.currentTimeMillis() - testStartTime;
        System.out.println("    Duration: " + duration + " ms");
    }

    @Test
    @DisplayName("Get all courses should return all courses when no query parameters")
    void testGetAllCoursesWithoutQueryParams() {
        List<Course> mockCourses = Arrays.asList(
                new Course("IFT1015", "Programmation I", "Description 1"),
                new Course("IFT1025", "Programmation II", "Description 2"));

        when(mockContext.queryParamMap()).thenReturn(new HashMap<>());
        when(mockService.getAllCourses(any())).thenReturn(mockCourses);

        controller.getAllCourses(mockContext);

        verify(mockContext).queryParamMap();
        verify(mockService).getAllCourses(any(Map.class));
        verify(mockContext).json(mockCourses);
    }

    @Test
    @DisplayName("Get all courses should pass query parameters to service")
    void testGetAllCoursesWithQueryParameters() {
        Map<String, List<String>> queryParamMap = new HashMap<>();
        queryParamMap.put("session", Arrays.asList("A2025"));

        List<Course> mockCourses = Arrays.asList(
                new Course("IFT1015", "Programmation I", "Description"));

        when(mockContext.queryParamMap()).thenReturn(queryParamMap);
        when(mockService.getAllCourses(any())).thenReturn(mockCourses);

        controller.getAllCourses(mockContext);

        verify(mockService).getAllCourses(argThat(params ->
                params.containsKey("session") &&
                params.get("session").equals("A2025")));
        verify(mockContext).json(mockCourses);
    }

    @Test
    @DisplayName("Get course by ID should return 404 when course not found")
    void testGetCourseByIdWhenCourseNotFound() {
        String courseId = "IFT1234";

        when(mockContext.pathParam("id")).thenReturn(courseId);
        when(mockService.getCourseById(courseId)).thenReturn(Optional.empty());
        when(mockContext.status(404)).thenReturn(mockContext);

        controller.getCourseById(mockContext);

        verify(mockService).getCourseById(courseId);
        verify(mockContext).status(404);
        verify(mockContext).json(argThat(map -> map instanceof Map &&
                ((Map<?, ?>) map).containsKey("error")));
    }

    @Test
    @DisplayName("Get course by ID should return 400 when ID is null")
    void testGetCourseByIdWhenIdIsNull() {
        when(mockContext.pathParam("id")).thenReturn(null);
        when(mockContext.status(400)).thenReturn(mockContext);

        controller.getCourseById(mockContext);

        verify(mockContext).status(400);
        verify(mockContext).json(argThat(map -> map instanceof Map &&
                ((Map<?, ?>) map).containsKey("error")));
        verify(mockService, never()).getCourseById(any());
    }

    @Test
    @DisplayName("Get course by ID should return 400 when ID is empty string")
    void testGetCourseByIdWhenIdIsEmpty() {
        when(mockContext.pathParam("id")).thenReturn("");
        when(mockContext.status(400)).thenReturn(mockContext);

        controller.getCourseById(mockContext);

        verify(mockContext).status(400);
        verify(mockService, never()).getCourseById(any());
    }

    @AfterAll
    static void printFooter() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("COMPLETED: CourseController Tests");
        System.out.println("=".repeat(80) + "\n");
    }
}