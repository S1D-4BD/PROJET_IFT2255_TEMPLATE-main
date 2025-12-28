package com.diro.ift2255.controller;

import io.javalin.http.Context;
import com.diro.ift2255.service.CommentService;
import java.util.Map;

public class CommentController {
  
    private CommentService service = new CommentService();

    public void getByCourse(Context ctx) {
        String courseId = ctx.pathParam("courseId");
        ctx.json(service.getByCourse(courseId));
    }

    public void create(Context ctx) {
        Map<String, String> comment = ctx.bodyAsClass(Map.class);
        service.create(comment);
        ctx.status(201).json(Map.of("status", "ok", "courseId", comment.get("courseId").toUpperCase()));
    }
}