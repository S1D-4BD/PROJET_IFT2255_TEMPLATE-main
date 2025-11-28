package com.diro.ift2255.controller;

import com.diro.ift2255.model.ComparaisonResult;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.util.ResponseUtil;

import io.javalin.http.Context;

public class ComparaisonController {

    private final CourseService service;

    public ComparaisonController(CourseService service) {
        this.service = service;
    }

    public void compareCourses(Context ctx) {
        try {
            String courseA = ctx.queryParam("courseA");
            String courseB = ctx.queryParam("courseB");

            if (courseA == null || courseB == null) {
                ctx.status(400).json(ResponseUtil.formatError("Vous devez fournir courseA et courseB en paramètres."));
                return;
            }

            if (!service.validateCourses(courseA, courseB)) {
                ctx.status(404).json(ResponseUtil.formatError("Un ou les deux cours n'existent pas."));
                return;
            }

            ComparaisonResult result = service.compareCourses(courseA, courseB);
            ctx.json(result);

        } catch (Exception e) {
            ctx.status(500).json(ResponseUtil.formatError("Erreur lors de la comparaison des cours: " + e.getMessage()));
        }
    }
}

